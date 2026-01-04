package io.github.mufca.libgdx.util;

import static io.github.mufca.libgdx.constant.ColorPalette.PINK_70A;
import static io.github.mufca.libgdx.constant.PathConstants.COMPUTER_SAYS_NO_MSDF;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.tommyettinger.textra.Font;
import io.github.mufca.libgdx.datastructure.lowlevel.Pair;
import io.github.mufca.libgdx.gui.core.lowlevel.FilledColor;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class UIHelper {

    private final static Set<FilledColor> colorTextures = new HashSet<>();
    public final static TextureRegionDrawable DEBUG_TEXTURE = getFilledColor(PINK_70A);
    private static Font defaultFont;

    public static Font getDefaultFont() {
        if (defaultFont == null) {
            defaultFont = new Font(COMPUTER_SAYS_NO_MSDF, false);
            defaultFont.scale(0.40f);
            defaultFont.setCrispness(5f);
        }
        return defaultFont;
    }

    public static TextureRegionDrawable getFilledColor(Color color) {
        if (containsAColor(color)) {
            return colorTextures.stream().filter(getFilledColorPredicate(color)).findAny().orElseThrow().drawable();
        } else {
            FilledColor filledColor = new FilledColor(color);
            colorTextures.add(filledColor);
            return filledColor.drawable();
        }
    }

    private static boolean containsAColor(Color color) {
        return colorTextures.stream().anyMatch(getFilledColorPredicate(color));
    }

    private static Predicate<? super FilledColor> getFilledColorPredicate(Color color) {
        return filledColor -> filledColor.color().equals(color);
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
