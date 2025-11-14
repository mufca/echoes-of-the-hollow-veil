package io.github.mufca.libgdx.datastructure.character.storage;

import io.github.mufca.libgdx.datastructure.character.logic.NPC;
import io.github.mufca.libgdx.datastructure.location.logic.BaseLocation;
import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import io.github.mufca.libgdx.gui.core.portrait.PortraitRepository;
import io.github.mufca.libgdx.util.LogHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import lombok.Getter;

public class NPCSystem {

    public static final String NUMBER_OF_SPAWNED_NPCS_EXCEEDS_NUMBER_OF_DEFINITIONS = "[SEVERE] Number of spawned Npcs "
        + "exceeds number of definitions for that location";
    private final NPCRepository npcRepository;
    @Getter
    private final Supplier<BaseLocation> currentLocation;
    private final Map<String, Set<Long>> currentLocationToCharacters = new HashMap<>();
    private final Map<String, Set<Long>> spawnLocationToCharacters = new HashMap<>();

    public NPCSystem(IdProvider idProvider, PortraitRepository portraitRepository,
        Supplier<BaseLocation> currentLocation)
        throws IOException {
        this.currentLocation = currentLocation;
        this.npcRepository = new NPCRepository(idProvider, portraitRepository, this.currentLocation);
    }

    public List<NPC> shouldSpawnIfMissing(List<String> npcIds) {
        var matchingNpcIds = new ArrayList<>(
            spawnLocationToCharacters.getOrDefault(currentLocationId(), Set.of()).stream()
                .map(npcRepository::get)
                .flatMap(Optional::stream)
                .filter(npc -> npcIds.contains(npc.meta().npcId()))
                .map(npc -> npc.meta().npcId())
                .toList());
        if (matchingNpcIds.size() > npcIds.size()) {
            LogHelper.warn(this, NUMBER_OF_SPAWNED_NPCS_EXCEEDS_NUMBER_OF_DEFINITIONS);
        }
        var missingNpcIds = npcIds.stream()
            .filter(npcId -> !matchingNpcIds.remove(npcId))
            .toList();
        for (String missingNpcId : missingNpcIds) {
            createNpc(missingNpcId);
        }
        return currentLocationToCharacters.getOrDefault(currentLocationId(), Set.of()).stream()
            .map(npcRepository::get)
            .flatMap(Optional::stream)
            .toList();
    }

    private void createNpc(String missingNpcId) {
        var characterId = npcRepository.create(missingNpcId);
        currentLocationToCharacters.computeIfAbsent(currentLocationId(), ignore -> new HashSet<>())
            .add(characterId);
        spawnLocationToCharacters.computeIfAbsent(currentLocationId(), ignore -> new HashSet<>())
            .add(characterId);
    }

    private String currentLocationId() {
        return currentLocation.get().targetId();
    }
}
