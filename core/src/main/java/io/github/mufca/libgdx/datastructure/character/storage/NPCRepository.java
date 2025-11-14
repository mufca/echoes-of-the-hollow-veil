package io.github.mufca.libgdx.datastructure.character.storage;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.mufca.libgdx.datastructure.character.jsondata.CharacterDefinitionRepository;
import io.github.mufca.libgdx.datastructure.character.logic.NPC;
import io.github.mufca.libgdx.datastructure.location.logic.BaseLocation;
import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import io.github.mufca.libgdx.gui.core.portrait.PortraitRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

public final class NPCRepository {

    private static final String PLAYER_INTO_REPOSITORY_ERROR = "Can't put player into NPC repository!";
    private static final String MISSING_CHARACTER_DEFINITION = "Missing character definition for %s id";

    private final CharacterDefinitionRepository definitionRepository;
    private final IdProvider idProvider;
    private final PortraitRepository portraitRepository;
    private final Supplier<BaseLocation> currentLocation;

    private final HashMap<Long, NPC> importantNPCs = new HashMap<>();
    private final Cache<Long, NPC> commonNPCs = Caffeine.newBuilder()
        .maximumSize(1_000L)
        .build();


    public NPCRepository(IdProvider idProvider, PortraitRepository portraitRepository,
        Supplier<BaseLocation> currentLocation) throws IOException {
        this.definitionRepository = new CharacterDefinitionRepository();
        this.idProvider = idProvider;
        this.portraitRepository = portraitRepository;
        this.currentLocation = currentLocation;
    }

    public long create(String npcId) {
        var data = definitionRepository.get(npcId)
            .orElseThrow(() -> new IllegalArgumentException(MISSING_CHARACTER_DEFINITION.formatted(npcId)));
        long characterId = idProvider.generateUniqueId();
        NPC newNpc = NPCFactory.giveBirth(characterId, currentLocation.get().targetId(), data, portraitRepository);
        put(characterId, newNpc);
        return characterId;
    }

    public Optional<NPC> get(long characterId) {
        if (importantNPCs.containsKey(characterId)) {
            return Optional.of(importantNPCs.get(characterId));
        }
        return Optional.ofNullable(commonNPCs.getIfPresent(characterId));
    }

    private void put(long characterId, NPC npc) {
        switch (npc.base().type()) {
            case IMPORTANT_NPC, COMPANION -> importantNPCs.put(characterId, npc);
            case NPC, ANIMAL, MONSTER -> commonNPCs.put(characterId, npc);
            case PLAYER -> throw new UnsupportedOperationException(PLAYER_INTO_REPOSITORY_ERROR);
        }
    }
}
