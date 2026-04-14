package io.github.mufca.libgdx.datastructure.character.logic.components;

import io.github.mufca.libgdx.gui.core.portrait.PortraitContainer;
import lombok.Getter;

@Getter
public class NPCMetadata {

    private final long characterId;
    private final String npcId;
    private final Integer respawningTicks;
    private final String enumName;
    private final PortraitContainer portraits;


    public NPCMetadata(long characterId, String npcId, Integer respawningTicks,
        String enumName, PortraitContainer portraits) {
        this.characterId = characterId;
        this.npcId = npcId;
        this.respawningTicks = respawningTicks;
        this.enumName = enumName;
        this.portraits = portraits;
    }
}
