package io.github.mufca.libgdx.datastructure.character;

import static io.github.mufca.libgdx.datastructure.statistics.StatisticsUtils.calculateMaximumHitPoints;
import static io.github.mufca.libgdx.datastructure.statistics.StatisticsUtils.calculateMaximumMana;
import static io.github.mufca.libgdx.datastructure.statistics.StatisticsUtils.calculateMaximumStamina;

import io.github.mufca.libgdx.datastructure.faction.Faction;
import io.github.mufca.libgdx.datastructure.inventory.Inventory;
import io.github.mufca.libgdx.datastructure.skill.Skill;
import io.github.mufca.libgdx.datastructure.statistics.PrimaryStatistics;
import io.github.mufca.libgdx.datastructure.statistics.SecondaryStatistic;
import io.github.mufca.libgdx.gui.core.portrait.Portrait;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

public class GameCharacter {

    private PrimaryStatistics primaryStatistics;
    private CharacteristicTraits characteristicTraits;
    private int statsSnapshotHash;

    private EnumMap<SecondaryStatistic, Float> currentSecondaryStats = new EnumMap<>(SecondaryStatistic.class);
    private EnumMap<SecondaryStatistic, Float> maximumSecondaryStats = new EnumMap<>(SecondaryStatistic.class);

    private float experience;

    private List<Skill> skills;

    private Portrait portrait;

    private List<Faction> guilds;

    public GameCharacter(CharacteristicTraits characteristicTraits, PrimaryStatistics primaryStatistics,
        List<Skill> skills, Portrait portrait, Inventory inventory, List<Faction> guilds) {
        this.primaryStatistics = primaryStatistics;
        this.characteristicTraits = characteristicTraits;
        updateAllSecondaryStats();
        this.skills = skills;
        this.portrait = portrait;
        this.guilds = guilds;
        this.experience = 0f;
    }

    private void updateAllSecondaryStats() {
        Arrays.stream(SecondaryStatistic.values()).forEach(
            statistic -> {
                float calculatedValue = getMaximumSecondaryStat(statistic);
                currentSecondaryStats.put(statistic, calculatedValue);
            });
        this.statsSnapshotHash = primaryStatistics.hashCode();
    }

    private float getMaximumSecondaryStat(SecondaryStatistic statistic) {
        if (!doesSnapshotMatch() || maximumSecondaryStats.get(statistic) == null) {
            float calculatedValue = switch (statistic) {
                case MANA -> calculateMaximumMana(primaryStatistics);
                case STAMINA -> calculateMaximumStamina(primaryStatistics);
                case HIT_POINTS -> calculateMaximumHitPoints(primaryStatistics);
            };
            maximumSecondaryStats.put(statistic, calculatedValue);
        }
        return maximumSecondaryStats.get(statistic);
    }

    private boolean doesSnapshotMatch() {
        return statsSnapshotHash == primaryStatistics.hashCode();
    }
}
