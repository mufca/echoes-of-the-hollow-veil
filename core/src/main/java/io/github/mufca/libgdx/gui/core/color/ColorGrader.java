package io.github.mufca.libgdx.gui.core.color;

import static com.badlogic.gdx.math.MathUtils.clamp;

import com.badlogic.gdx.graphics.Color;

public class ColorGrader {

    private static final String TAG = "[#%s]";
    private static final float MIN_SATURATION = 0f;
    private static final float MAX_SATURATION = 1f;

    public static String tag(Color color, int current, int max) {
        float[] hueSaturationValue = new float[3];
        float factor = (float) current / max;
        color.toHsv(hueSaturationValue);

        hueSaturationValue[1] = clamp(factor, MIN_SATURATION, MAX_SATURATION);

        Color result = new Color().fromHsv(
            hueSaturationValue[0],
            hueSaturationValue[1],
            hueSaturationValue[2]);

        result.a = 1.0f;

        return TAG.formatted(result.toString());
    }
}
