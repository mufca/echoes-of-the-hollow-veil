package io.github.mufca.libgdx.gui.core.portrait;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Many-to-many characterId <-> portraitId
 */
public final class PortraitRepository {

    private static final int MAX_CACHE_SIZE = 70;

    private final Map<Long, PortraitEntry> portraits =
        new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, PortraitEntry> eldest) {
                if (size() > MAX_CACHE_SIZE) {
                    removeReferences(eldest.getKey(), eldest.getValue().path());
                    eldest.getValue().dispose();
                    return true;
                }
                return false;
            }
        };

    private final Map<Long, Set<Long>> characterToPortraits = new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> portraitToCharacters = new ConcurrentHashMap<>();
    private final Map<String, Long> pathToPortraitId = new ConcurrentHashMap<>();

    private final IdProvider idProvider;

    public PortraitRepository(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public synchronized TextureRegion getPortrait(Long characterId, PortraitFile type) {
        Set<Long> portraitIds = characterToPortraits.get(characterId);
        if (portraitIds == null) {
            return null;
        }
        return portraitIds.stream()
            .map(portraits::get)
            .filter(Objects::nonNull)
            .filter(entry -> entry.portraitFile() == type)
            .map(PortraitEntry::region)
            .findFirst()
            .orElse(null);
    }

    /**
     * Checks if portrait (filepath) already exists in repository if not loads a new texture and creates a new portrait
     * entry and updates relations
     */
    public synchronized void addOrReferencePortrait(Long characterId, FileHandle fileHandle, PortraitFile fileType) {
        Long existingId = findPortraitIdByPath(fileHandle.path());

        if (existingId != null) {
            addRelation(characterId, existingId);
            return;
        }

        // Create a new portrait
        long newPortraitId = idProvider.generateUniqueId();
        Texture texture = new Texture(fileHandle);
        TextureRegion region = new TextureRegion(texture);

        PortraitEntry entry = new PortraitEntry(newPortraitId, fileType, fileHandle.path(), texture, region);
        portraits.put(newPortraitId, entry);
        pathToPortraitId.put(fileHandle.path(), newPortraitId);
        addRelation(characterId, newPortraitId);
    }

    private Long findPortraitIdByPath(String path) {
        return pathToPortraitId.get(path);
    }

    private synchronized void removeReferences(Long portraitId, String path) {
        Set<Long> removedCharacters = portraitToCharacters.remove(portraitId);
        pathToPortraitId.remove(path);
        if (removedCharacters != null) {
            for (Long characterId : removedCharacters) {
                Set<Long> charactersWithRemovedPortrait = characterToPortraits.get(characterId);
                if (charactersWithRemovedPortrait != null) {
                    charactersWithRemovedPortrait.remove(portraitId);
                    if (charactersWithRemovedPortrait.isEmpty()) {
                        characterToPortraits.remove(characterId);
                    }
                }
            }
        }
    }

    /**
     * Removes characters ids and if corresponding portraits are orphaned removes them too
     */
    public synchronized void removeGivenCharacters(List<Long> characterIds) {
        for (Long characterId : characterIds) {
            Set<Long> portraitIds = characterToPortraits.remove(characterId);
            if (portraitIds == null) {
                continue;
            }

            for (Long portraitId : portraitIds) {
                Set<Long> chars = portraitToCharacters.get(portraitId);
                if (chars != null) {
                    chars.remove(characterId);
                    if (chars.isEmpty()) {
                        portraitToCharacters.remove(portraitId);
                        removePortrait(portraitId);
                    }
                }
            }
        }
    }

    private void removePortrait(Long portraitId) {
        PortraitEntry entry = portraits.remove(portraitId);
        if (entry != null) {
            pathToPortraitId.remove(entry.path());
            entry.dispose();
        }
    }

    private void addRelation(Long characterId, Long portraitId) {
        characterToPortraits.computeIfAbsent(characterId, ignored -> new HashSet<>()).add(portraitId);
        portraitToCharacters.computeIfAbsent(portraitId, ignored -> new HashSet<>()).add(characterId);
    }

    public synchronized void disposeAll() {
        for (PortraitEntry entry : portraits.values()) {
            entry.dispose();
        }
        portraits.clear();
        characterToPortraits.clear();
        portraitToCharacters.clear();
        pathToPortraitId.clear();
    }

}
