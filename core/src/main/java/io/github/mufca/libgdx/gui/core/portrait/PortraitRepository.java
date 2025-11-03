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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe, async-loading portrait repository with many-to-many characterId <-> portraitId mapping.
 */
public final class PortraitRepository {

    private static final int MAX_CACHE_SIZE = 70;
    private static final String FAILED_TO_CREATE_TEXTURE = "Failed to create texture: %s";

    private final Map<String, Long> pathToPortraitId = new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> characterToPortraits = new ConcurrentHashMap<>();

    private final Cache<Long, PortraitEntry> portraits = Caffeine.newBuilder()
        .maximumSize(MAX_CACHE_SIZE)
        .removalListener((Long id, PortraitEntry entry, RemovalCause cause) -> {
            pathToPortraitId.remove(entry.path());
            entry.dispose();
        })
        .build();

    private final IdProvider idProvider;

    public PortraitRepository(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public void registerPortraitAsync(Long characterId, FileHandle file, PortraitFile type) {
        Long existingId = pathToPortraitId.get(file.path());
        if (existingId != null) {
            addRelation(characterId, existingId);
            return;
        }

        long newId = idProvider.generateUniqueId();

        CompletableFuture
            .supplyAsync(() -> new Pixmap(file))
            .thenAcceptAsync(pixmap -> {
                try {
                    Texture texture = new Texture(pixmap);
                    TextureRegion region = new TextureRegion(texture);
                    PortraitEntry entry = new PortraitEntry(newId, type, file.path(), texture, region);

                    synchronized (this) {
                        portraits.put(newId, entry);
                        pathToPortraitId.put(file.path(), newId);
                        addRelation(characterId, newId);
                    }
                } catch (RuntimeException e) {
                    LogHelper.error(this, FAILED_TO_CREATE_TEXTURE.formatted(e.getMessage()));
                } finally {
                    pixmap.dispose(); // always dispose, even if Texture throws
                }
            }, runnable -> Gdx.app.postRunnable(runnable));
    }

    public TextureRegion getPortrait(Long characterId, PortraitFile type) {
        Set<Long> portraitIds = characterToPortraits.get(characterId);
        if (portraitIds == null) {
            return null;
        }

        for (Long portraitId : portraitIds) {
            PortraitEntry portraitEntry = portraits.getIfPresent(portraitId);
            if (portraitEntry != null && portraitEntry.portraitFile() == type) {
                return portraitEntry.region();
            }
        }
        return null;
    }

    public void removeCharacters(List<Long> characterIds) {
        for (Long characterId : characterIds) {
            Set<Long> ids = characterToPortraits.remove(characterId);
            if (ids == null) {
                continue;
            }

            for (Long portraitId : ids) {
                boolean orphan = characterToPortraits.values().stream()
                    .noneMatch(set -> set.contains(portraitId));

                if (orphan) {
                    PortraitEntry removed = portraits.asMap().remove(portraitId);
                    if (removed != null) {
                        pathToPortraitId.remove(removed.path());
                    }
                }
            }
        }
    }

    public void disposeAll() {
        portraits.invalidateAll();
        characterToPortraits.clear();
        pathToPortraitId.clear();
    }

    private void addRelation(Long characterId, Long portraitId) {
        characterToPortraits
            .computeIfAbsent(characterId, id -> ConcurrentHashMap.newKeySet())
            .add(portraitId);
    }
}
