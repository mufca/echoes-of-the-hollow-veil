package io.github.mufca.libgdx.datastructure.character.logic;

import io.github.mufca.libgdx.datastructure.character.logic.components.AppearanceTraits;
import io.github.mufca.libgdx.datastructure.character.logic.components.BaseCharacter;
import io.github.mufca.libgdx.datastructure.character.logic.components.NPCMetadata;
import io.github.mufca.libgdx.datastructure.character.logic.components.PrimaryStatistics;
import io.github.mufca.libgdx.datastructure.character.logic.components.SecondaryStatistics;
import lombok.Getter;

@Getter
public final class NPC {

    private final BaseCharacter base;
    private final AppearanceTraits appearance;
    private final PrimaryStatistics primaryStats;
    private final SecondaryStatistics secondaryStats;
    private final NPCMetadata meta;

    public NPC(BaseCharacter base, AppearanceTraits appearance, PrimaryStatistics primaryStats,
        SecondaryStatistics secondaryStats, NPCMetadata meta) {
        this.base = base;
        this.appearance = appearance;
        this.primaryStats = primaryStats;
        this.secondaryStats = secondaryStats;
        this.meta = meta;
    }
}
