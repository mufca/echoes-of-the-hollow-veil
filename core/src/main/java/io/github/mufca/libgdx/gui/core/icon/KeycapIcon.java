package io.github.mufca.libgdx.gui.core.icon;

import static io.github.mufca.libgdx.util.SafeCast.floatToIntExact;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.Layout;
import io.github.mufca.libgdx.datastructure.keyprovider.KeyboardKey;
import io.github.mufca.libgdx.util.UIHelper;
import lombok.Getter;

public class KeycapIcon extends Actor {

    private static final float radius = 5f;
    private final Layout layout;
    @Getter
    private final KeyboardKey key;
    private final Font font;
    private final String sign;
    private Texture keycapTexture;

    public KeycapIcon(KeyboardKey key, float size) {
        this.font = UIHelper.getDefaultFont();
        this.key = key;
        this.sign = String.valueOf(key.displayedCharacter());
        setSize(size, size);
        this.layout = new Layout(font);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (keycapTexture == null) {
            generateKeycapTexture(floatToIntExact(getWidth()), floatToIntExact(getHeight()));
        }
        batch.setShader(null);

        batch.setColor(Color.WHITE);
        batch.draw(keycapTexture, getX(), getY(), getWidth(), getHeight());
        layout.clear();
        batch.setShader(font.shader);
        font.markup(sign, this.layout);
        font.drawGlyphs(batch, layout, centerX(), centerY());
    }

    private void drawRoundedRect(Pixmap pixmap, int x, int y, int w, int h, int r) {
        // Icon squares
        pixmap.fillRectangle(x + r, y, w - 2 * r, h); // middle
        pixmap.fillRectangle(x, y + r, r, h - 2 * r); // left
        pixmap.fillRectangle(x + w - r, y + r, r, h - 2 * r); // right

        // Icon edges
        pixmap.fillCircle(x + r, y + r, r); // left bottom
        pixmap.fillCircle(x + w - r - 1, y + r, r); // right bottom
        pixmap.fillCircle(x + r, y + h - r - 1, r); // left top
        pixmap.fillCircle(x + w - r - 1, y + h - r - 1, r); // right top
    }

    private float centerX() {
        Layout measure = font.markup(sign, new Layout());
        float measureWidth = measure.peekLine().width;
        return getX() + (getWidth() / 2f) - (measureWidth / 2f);
    }

    private float centerY() {
        return getY() + layout.getHeight() / 4f;
    }

    private void generateKeycapTexture(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);

        // Outer
        pixmap.setColor(Color.LIGHT_GRAY);
        drawRoundedRect(pixmap, 0, 0, width, height, floatToIntExact(KeycapIcon.radius));

        // Inner
        pixmap.setColor(Color.DARK_GRAY);
        drawRoundedRect(pixmap, 1, 1, width - 2, height - 2, floatToIntExact(KeycapIcon.radius - 1));

        keycapTexture = new Texture(pixmap);
        keycapTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();
    }
}
