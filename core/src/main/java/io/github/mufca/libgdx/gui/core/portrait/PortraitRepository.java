package io.github.mufca.libgdx.gui.core.portrait;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import io.github.mufca.libgdx.util.LogHelper;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Thread-safe, async-loading portrait repository with many-to-many characterId <-> portraitId mapping.
 */
public final class PortraitRepository {

    private static final int MAX_CACHE_SIZE = 70;
    private static final String FAILED_TO_CREATE_TEXTURE = "Failed to create texture: %s";
    private static final String START_LOADING_TEXTURE = "Starting loading texture: %d";
    private static final String END_LOADING_TEXTURE = "Ended loading texture: %d";

    private final Queue<Runnable> pendingUploads = new ConcurrentLinkedQueue<>();
    private final Map<String, Long> pathToPortraitId = new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> characterToPortraits = new ConcurrentHashMap<>();

    private final Cache<Long, PortraitEntry> portraits = Caffeine.newBuilder()
        .maximumSize(MAX_CACHE_SIZE)
        .removalListener((Long portraitId, PortraitEntry entry, RemovalCause cause) -> {
            Gdx.app.postRunnable(() -> {
                removePortraitFromCharacters(portraitId);
                pathToPortraitId.remove(entry.path());
                entry.dispose();
            });
        })
        .build();

    private final IdProvider idProvider;

    public PortraitRepository(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public CompletableFuture<Void> registerPortraitAsync(Long characterId, FileHandle file, PortraitFile type) {
        Long existingId = pathToPortraitId.get(file.path());
        if (existingId != null) {
            addRelation(characterId, existingId);
            return CompletableFuture.completedFuture(null);
        }

        long newId = idProvider.generateUniqueId();

        return CompletableFuture
            .supplyAsync(() -> {
                var pixmap = new Pixmap(file);
                LogHelper.debug(this, "Loaded pixmap at %d, thread: %s"
                    .formatted(System.nanoTime(), Thread.currentThread().getName()));
                return pixmap;
            })
            .thenAcceptAsync(pixmap -> {
                pendingUploads.add(() -> {
                    try {
                        Texture texture = new Texture(pixmap);
                        TextureRegion region = new TextureRegion(texture);
                        PortraitEntry entry = new PortraitEntry(newId, type, file.path(), texture, region);

                        portraits.put(newId, entry);
                        pathToPortraitId.put(file.path(), newId);
                        addRelation(characterId, newId);

                        LogHelper.debug(this, "Created texture and added relation at %d".formatted(System.nanoTime()));
                    } catch (RuntimeException e) {
                        LogHelper.error(this, FAILED_TO_CREATE_TEXTURE.formatted(e.getMessage()));
                    } finally {
                        pixmap.dispose();
                    }
                });
            });
    }

    public Optional<TextureRegion> getPortrait(Long characterId, PortraitFile type) {
        Set<Long> portraitIds = characterToPortraits.get(characterId);
        if (portraitIds == null) {
            return Optional.empty();
        }

        for (Long portraitId : portraitIds) {
            PortraitEntry portraitEntry = portraits.getIfPresent(portraitId);
            if (portraitEntry != null && portraitEntry.portraitFile() == type) {
                return Optional.of(portraitEntry.region());
            }
        }
        return Optional.empty();
    }

    private void removePortraitFromCharacters(Long portraitId) {
        characterToPortraits.entrySet()
            .removeIf(entry -> {
                entry.getValue().remove(portraitId);
                return entry.getValue().isEmpty();
            });
    }

    private void addRelation(Long characterId, Long portraitId) {
        characterToPortraits
            .computeIfAbsent(characterId, id -> ConcurrentHashMap.newKeySet())
            .add(portraitId);
    }

    public void processTextureUpload() {
        for (int i = 0; i < 5; i++) {
            Runnable task = pendingUploads.poll();
            if (task != null) {
                LogHelper.debug(this, START_LOADING_TEXTURE.formatted(System.nanoTime()));
                task.run();
                LogHelper.debug(this, END_LOADING_TEXTURE.formatted(System.nanoTime()));
            }
        }
    }

    public int getLoadingQueueSize() {
        return pendingUploads.size();
    }

    /**
     * Returns an immutable snapshot of the current character-to-portraits mapping.
     * <p>
     * This method is intended <strong>for testing and debug visualization only</strong>. The returned map is a deep,
     * unmodifiable copy — subsequent mutations in the repository will not affect this snapshot.
     *
     * @return an immutable map of character IDs to immutable sets of portrait IDs
     */
    public Map<Long, Set<Long>> snapshotCharacterToPortraits() {
        return characterToPortraits.entrySet().stream()
            .collect(Collectors.toUnmodifiableMap(
                Map.Entry::getKey,
                e -> Set.copyOf(e.getValue())
            ));
    }

    /**
     * Returns an immutable snapshot of all portrait entries currently loaded in the cache.
     * <p>
     * This method is intended <strong>for testing and debug visualization only</strong>. It captures a stable copy of
     * the cache contents at the time of invocation. The returned map is unmodifiable and detached from the live cache.
     *
     * @return an immutable map of portrait IDs to their corresponding {@link PortraitEntry}
     */
    public Map<Long, PortraitEntry> snapshotPortraits() {
        return Map.copyOf(portraits.asMap());
    }
}
