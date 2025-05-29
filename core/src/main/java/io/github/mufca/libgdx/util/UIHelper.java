package io.github.mufca.libgdx.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.tommyettinger.textra.Font;
import io.github.mufca.libgdx.datastructure.lowlevel.Pair;
import io.github.mufca.libgdx.gui.core.lowlevel.FilledColor;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static io.github.mufca.libgdx.constant.PathConstants.NOTO_SANS_FNT;
import static io.github.mufca.libgdx.constant.PathConstants.NOTO_SANS_PNG;

public class UIHelper {
    public final static Color BLACK_60A = new Color(0, 0, 0, 0.6f);
    private final static Set<FilledColor> colorTextures = new HashSet<>();
    private static Font defaultFont;

    public static Font getDefaultFont() {
        if (defaultFont == null) {
            defaultFont = new Font(NOTO_SANS_FNT, NOTO_SANS_PNG);
        }
        return defaultFont;
    }

    public static TextureRegionDrawable getFilledColor(Color color) {
        if (containsAColor(color)) {
            return colorTextures.stream().filter(getFilledColorPredicate(color)).findAny().orElseThrow().getDrawable();
        } else {
            FilledColor filledColor = new FilledColor(color);
            colorTextures.add(filledColor);
            return filledColor.getDrawable();
        }
    }

    private static boolean containsAColor(Color color) {
        return colorTextures.stream().anyMatch(getFilledColorPredicate(color));
    }

    private static Predicate<FilledColor> getFilledColorPredicate(Color color) {
        return filledColor -> filledColor.getColor().equals(color);
    }

    public static Pair<Float> getTopLeftPaddings(Stage stage, float top, float left) {
        float screenWidth = stage.getViewport().getScreenWidth();
        float screenHeight = stage.getViewport().getScreenHeight();
        float topPadding = screenHeight * top;
        float leftPadding = screenWidth * left;
        return new Pair<>(topPadding, leftPadding);
    }

    public static void doNothing() {

    }
}
