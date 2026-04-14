package io.github.mufca.libgdx.system.npc;

import io.github.mufca.libgdx.context.npc.NPCRepository;
import io.github.mufca.libgdx.datastructure.character.logic.NPC;
import io.github.mufca.libgdx.datastructure.location.CurrentLocationProvider;
import io.github.mufca.libgdx.system.movement.LocationChangedEvent;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NPCSystem {

    public static final String NPC_COUNT_MISMATCH = "NPC count mismatch in %s location for %s definition.";
    private final NPCRepository npcRepository;
    private final CurrentLocationProvider locationProvider;

    public NPCSystem(NPCRepository npcRepository, CurrentLocationProvider locationProvider) {
        this.npcRepository = npcRepository;
        this.locationProvider = locationProvider;
    }

    public void handleLocationChangedEvent(LocationChangedEvent lChEvent) {
        var definitions = locationProvider.currentLocation().npcDefinitions().stream()
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            ));
        definitions.forEach((definitionId, count) -> {
            List<NPC> existingNpc = npcRepository.getBySpawnLocation(definitionId, lChEvent.toLocationId());

            if (existingNpc.isEmpty()) {
                for (int i = 0; i < count; i++) {
                    npcRepository.create(definitionId);
                }
            } else {
                if (existingNpc.size() != count.intValue()) {
                    throw new IllegalStateException(NPC_COUNT_MISMATCH
                        .formatted(lChEvent.toLocationId(), definitionId));
                }
                existingNpc.forEach(NPC::resetIfNeeded);
            }
        });
    }

    public boolean areThereNPCsHere() {
        var locationId = locationProvider.currentLocation().targetId();
        var ids = npcRepository.getByCurrentLocation(locationId);
        if (ids.isEmpty()) return false;
        for (Long id : ids) {
            var npc = npcRepository.get(id).orElseThrow();
            if (npc.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public List<Long> getNPCs() {
        return npcRepository.getByCurrentLocation(locationProvider.currentLocation().targetId());
    }

    public NPC getNPC(Long id) {
        return npcRepository.get(id).orElseThrow();
    }
}
