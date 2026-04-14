package io.github.mufca.libgdx.datastructure.character.logic;

import io.github.mufca.libgdx.datastructure.character.logic.components.AppearanceTraits;
import io.github.mufca.libgdx.datastructure.character.logic.components.BaseCharacter;
import io.github.mufca.libgdx.datastructure.character.logic.components.CharacterStats;
import io.github.mufca.libgdx.datastructure.character.logic.components.NPCMetadata;
import io.github.mufca.libgdx.datastructure.character.logic.components.StatTag;

public record NPC(BaseCharacter base, AppearanceTraits appearance, CharacterStats characterStats, NPCMetadata meta) {

    public void resetIfNeeded() {
    }

    @Override
    public String toString() {
        return appearance().firstTrait() + " " +
            appearance().secondTrait() + " " +
            appearance().race();
    }

    public boolean isAlive() {
        return characterStats.byTag(StatTag.HIT_POINTS) > 0;
    }

    public boolean equals(Object o) {
      return o instanceof NPC npc && npc.base().characterId() == this.base().characterId();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(base().characterId());
    }
}
