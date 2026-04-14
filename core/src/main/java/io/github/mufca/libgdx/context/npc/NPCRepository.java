package io.github.mufca.libgdx.context.npc;

import io.github.mufca.libgdx.constant.ErrorConstants;
import io.github.mufca.libgdx.datastructure.character.jsondata.CharacterDefinitionRepository;
import io.github.mufca.libgdx.datastructure.character.logic.NPC;
import io.github.mufca.libgdx.datastructure.location.logic.BaseLocation;
import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import io.github.mufca.libgdx.gui.core.portrait.PortraitRepository;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.Getter;

public final class NPCRepository {

    private static final String MISSING_CHARACTER_DEFINITION = "Missing character definition for %s id";

    private final CharacterDefinitionRepository definitionRepository;
    private final IdProvider idProvider;
    private final PortraitRepository portraitRepository;
    private final Supplier<BaseLocation> currentLocation;
    private final EventBus eventBus;

    @Getter
    private final Map<Long, NPC> allNpcs = new HashMap<>();

    // Indexes
    private final Map<String, List<Long>> npcsByCurrentLocation = new HashMap<>();
    private final Map<String, List<Long>> npcsBySpawnLocation = new HashMap<>();

    public NPCRepository(IdProvider idProvider, EventBus eventBus, PortraitRepository portraitRepository,
        Supplier<BaseLocation> currentLocation) throws IOException {
        this.eventBus = eventBus;
        this.definitionRepository = new CharacterDefinitionRepository();
        this.idProvider = idProvider;
        this.portraitRepository = portraitRepository;
        this.currentLocation = currentLocation;
    }

    public long create(String definitionId) {
        Objects.requireNonNull(eventBus, ErrorConstants.CAN_T_BE_NULL.formatted("eventBus"));

        var data = definitionRepository.get(definitionId)
            .orElseThrow(() -> new IllegalArgumentException(MISSING_CHARACTER_DEFINITION.formatted(definitionId)));

        long characterId = idProvider.generateUniqueId();

        var spawnLocationId = currentLocation.get().targetId();
        NPC newNpc = NPCFactory.giveBirth(characterId, eventBus, spawnLocationId, data, portraitRepository);

        allNpcs.put(characterId, newNpc);

        npcsBySpawnLocation.computeIfAbsent(spawnLocationId, k -> new ArrayList<>()).add(characterId);
        npcsByCurrentLocation.computeIfAbsent(spawnLocationId, k -> new ArrayList<>()).add(characterId);

        return characterId;
    }

    public List<Long> getByCurrentLocation(String currentLocationId) {
        return npcsByCurrentLocation.getOrDefault(currentLocationId, Collections.emptyList());
    }

    public List<NPC> getBySpawnLocation(String definitionId, String spawnLocationId) {
        List<Long> ids = npcsBySpawnLocation.getOrDefault(spawnLocationId, Collections.emptyList());

        return ids.stream()
            .map(allNpcs::get)
            .filter(npc -> npc != null && npc.meta().npcId().equals(definitionId))
            .toList();
    }

    public Optional<NPC> get(long characterId) {
        return Optional.ofNullable(allNpcs.get(characterId));
    }


}