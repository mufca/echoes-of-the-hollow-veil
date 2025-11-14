package io.github.mufca.libgdx.datastructure.character.storage;

import static io.github.mufca.libgdx.datastructure.lowlevel.RandomUtils.pickFromRange;
import static io.github.mufca.libgdx.gui.core.portrait.PortraitFile.SMALL;

import io.github.mufca.libgdx.datastructure.character.jsondata.CharacterData;
import io.github.mufca.libgdx.datastructure.character.logic.presets.AppearancePreset;
import io.github.mufca.libgdx.datastructure.character.logic.components.AppearanceTraits;
import io.github.mufca.libgdx.datastructure.character.logic.components.BaseCharacter;
import io.github.mufca.libgdx.datastructure.character.logic.components.CharacterType;
import io.github.mufca.libgdx.datastructure.character.logic.NPC;
import io.github.mufca.libgdx.datastructure.character.logic.components.NPCMetadata;
import io.github.mufca.libgdx.datastructure.character.logic.components.PrimaryStatistics;
import io.github.mufca.libgdx.datastructure.character.logic.components.SecondaryStatistics;
import io.github.mufca.libgdx.datastructure.lowlevel.Pair;
import io.github.mufca.libgdx.datastructure.lowlevel.RandomUtils;
import io.github.mufca.libgdx.gui.core.portrait.PortraitContainer;
import io.github.mufca.libgdx.gui.core.portrait.PortraitHandler;
import io.github.mufca.libgdx.gui.core.portrait.PortraitRepository;
import java.util.EnumSet;

public final class NPCFactory {

    public static final String MISSING_APPEARANCE = "No %s available for appearance";

    public static NPC giveBirth(Long characterId, String currentLocationId, CharacterData characterData,
        PortraitRepository portraitRepository) {
        var characterType = CharacterType.valueOf(characterData.type());

        var base = new BaseCharacter(characterId, characterType, characterData.name());

        Pair<String> appearances = getAppearances(characterData);
        var appearance = new AppearanceTraits(
            characterId,
            appearances.first(),
            appearances.second(),
            characterData.appearance().race());

        var primaryStatsData = characterData.primaryStatistics();
        var primaryStats = new PrimaryStatistics(characterId,
            adjustStatistic(primaryStatsData.strength(), characterData),
            adjustStatistic(primaryStatsData.dexterity(), characterData),
            adjustStatistic(primaryStatsData.constitution(), characterData),
            adjustStatistic(primaryStatsData.intelligence(), characterData),
            adjustStatistic(primaryStatsData.wisdom(), characterData),
            adjustStatistic(primaryStatsData.charisma(), characterData)
        );

        var secondaryStatsData = characterData.secondaryStatistics();
        var secondaryStats = new SecondaryStatistics(characterId,
            secondaryStatsData.hitPoints(),
            secondaryStatsData.stamina(),
            secondaryStatsData.mana());

        var portraitFiles = EnumSet.of(SMALL);
        var portraitHandler = new PortraitHandler(characterId, characterData.enumReferenceName().toLowerCase()
            , portraitRepository);
        portraitHandler.loadAndRegister(portraitFiles);
        var portraitContainer = new PortraitContainer(characterId, portraitRepository);
        portraitContainer.add(SMALL);
        var meta = new NPCMetadata(characterId, characterData.id(), characterData.respawningTicks(),
            characterData.enumReferenceName(), currentLocationId, portraitContainer);
        return new NPC(base, appearance, primaryStats, secondaryStats, meta);
    }

    private static Pair<String> getAppearances(CharacterData data) {
        var firstTrait = data.appearance().firstTrait();
        var secondTrait = data.appearance().secondTrait();
        var appearancePreset = AppearancePreset.valueOf(data.enumReferenceName());
        if (firstTrait == null || firstTrait.isEmpty()) {
            firstTrait = appearancePreset.firstTrait();
        }
        if (secondTrait == null || secondTrait.isEmpty()) {
            secondTrait = appearancePreset.secondTrait();
        }
        return new Pair<>(
            RandomUtils.pickFromList(firstTrait)
                .orElseThrow(() -> new IllegalStateException(MISSING_APPEARANCE.formatted("firstTrait"))),
            RandomUtils.pickFromList(secondTrait)
                .orElseThrow(() -> new IllegalStateException(MISSING_APPEARANCE.formatted("secondTrait"))));
    }

    private static float adjustStatistic(float statistic, CharacterData data) {
        return statistic + pickFromRange(0, data.statsRandomAdjustment());
    }
}
