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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe, async-loading portrait repository with many-to-many characterId <-> portraitId mapping.
 */
public final class PortraitRepository {

    private static final int MAX_CACHE_SIZE = 70;

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

    public CompletableFuture<Void> loadPortraitAsync(Long characterId, FileHandle file, PortraitFile type) {
        Long existingId = pathToPortraitId.get(file.path());
        if (existingId != null) {
            addRelation(characterId, existingId);
            return CompletableFuture.completedFuture(null);
        }

        long newId = idProvider.generateUniqueId();
        CompletableFuture<Void> future = new CompletableFuture<>();

        CompletableFuture
            .supplyAsync(() -> {
                try {
                    return new Pixmap(file);
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            })
            .thenAcceptAsync(pixmap -> {
                try {
                    Texture texture = new Texture(pixmap);
                    pixmap.dispose();
                    TextureRegion region = new TextureRegion(texture);
                    PortraitEntry entry = new PortraitEntry(newId, type, file.path(), texture, region);

                    portraits.put(newId, entry);
                    pathToPortraitId.put(file.path(), newId);
                    addRelation(characterId, newId);

                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }, runnable -> Gdx.app.postRunnable(runnable));

        return future;
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
