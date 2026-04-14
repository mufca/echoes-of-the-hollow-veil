package io.github.mufca.libgdx.gui.screen.gameplay;

import static com.badlogic.gdx.math.MathUtils.clamp;

import com.badlogic.gdx.graphics.Color;
import io.github.mufca.libgdx.datastructure.character.logic.components.CharacterStats;
import io.github.mufca.libgdx.datastructure.character.logic.components.StatTag;
import io.github.mufca.libgdx.gui.core.color.ColorGrader;
import io.github.mufca.libgdx.system.ink.InkFunctions;
import java.util.EnumMap;
import lombok.Getter;

public class StatRegistry {

    private final CharacterStats stats;
    private final EnumMap<StatTag, String[]> coloredThresholds = new EnumMap<>(StatTag.class);
    private final int[] currentLevels = new int[StatTag.values().length];
    private final String[] cachedColoredLevels = new String[StatTag.values().length];
    @Getter
    private boolean anyChanged = false;

    public StatRegistry(CharacterStats stats, InkFunctions functions) {
        this.stats = stats;
        for (StatTag tag : StatTag.values()) {
            var thresholds = functions.getStatFlavorParts(tag.name()).split(",");
            for (int i = 0; i < thresholds.length; i++) {
                var color = ColorGrader.tag(Color.GREEN, i, thresholds.length);
                thresholds[i] = color + thresholds[i];
            }
            coloredThresholds.put(tag, thresholds);
        }
        update();
    }

    public void update() {
        for (StatTag tag : StatTag.values()) {
            var tagIndex = tag.ordinal();
            var value = stats.byTag(tag);
            var maxValue = Math.max(1f, stats.maxByTag(tag));
            var percentage = value / maxValue;
            String[] thresholds = coloredThresholds.get(tag);
            var newIndex = clamp((int) (percentage * (thresholds.length - 1)), 0, thresholds.length - 1);
            if (currentLevels[tagIndex] != newIndex) {
                currentLevels[tagIndex] = newIndex;
                cachedColoredLevels[tagIndex] = coloredThresholds.get(tag)[newIndex];
                anyChanged = true;
            }
        }
    }

    public String[] coloredLevels() {
        anyChanged = false;
        return cachedColoredLevels;
    }
}
