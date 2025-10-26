package io.github.mufca.libgdx.datastructure.statistics;

public class StatisticsUtils {

    private static final float horizon = 10000f;
    private static final float scalingBuffer = 300f;
    private static final float enduranceScalingForHitPoints = 0.62f;
    private static final float strengthScalingForHitPoints = 0.38f;
    private static final float enduranceScalingForStamina = 0.60f;
    private static final float willpowerScalingForStamina = 0.40f;
    private static final float charismaScalingForMana = 0.45f;
    private static final float intelligenceScalingForMana = 0.54f;

    private static float sinusBasedFormulaDefaultScalingBuffer(float a, float aWeight, float b, float bWeight) {
        float numerator = a * aWeight + b * bWeight;
        float denominator = a + b + scalingBuffer;

        float normalized = numerator / denominator;
        float angle = (float) (Math.PI / 2) * normalized;

        return horizon * (float) Math.sin(angle);
    }

    public static float calculateMaximumHitPoints(PrimaryStatistics stats) {
        return sinusBasedFormulaDefaultScalingBuffer(
            stats.endurance(), enduranceScalingForHitPoints,
            stats.strength(), strengthScalingForHitPoints);
    }

    public static float calculateMaximumStamina(PrimaryStatistics stats) {
        return sinusBasedFormulaDefaultScalingBuffer(
            stats.endurance(), enduranceScalingForStamina,
            stats.willpower(), willpowerScalingForStamina
        );
    }

    public static float calculateMaximumMana(PrimaryStatistics stats) {
        return sinusBasedFormulaDefaultScalingBuffer(
            stats.intelligence(), intelligenceScalingForMana,
            stats.charisma(), charismaScalingForMana
        );
    }
}
