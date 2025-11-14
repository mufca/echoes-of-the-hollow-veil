package io.github.mufca.libgdx.datastructure.character.logic.components;

import io.github.mufca.libgdx.gui.core.portrait.PortraitContainer;
import lombok.Getter;

@Getter
public class NPCMetadata {

    private final Long characterId;
    private final String npcId;
    private final Integer respawningTicks;
    private final String enumName;
    private final String spawnLocationId;
    private String currentLocationId;
    private final PortraitContainer portraits;


    public NPCMetadata(Long characterId, String npcId, Integer respawningTicks,
        String enumName, String spawnLocationId, PortraitContainer portraits) {
        this.characterId = characterId;
        this.npcId = npcId;
        this.respawningTicks = respawningTicks;
        this.enumName = enumName;
        this.spawnLocationId = spawnLocationId;
        this.currentLocationId = spawnLocationId;
        this.portraits = portraits;
    }
}
