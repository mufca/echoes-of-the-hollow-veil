package io.github.mufca.libgdx.gui.core.lowlevel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "color")
public class FilledColor implements Disposable {

    private static final int size = 1;
    private final Texture texture;
    private final Color color;
    private final TextureRegionDrawable drawable;

    public FilledColor(Color color) {
        this.color = color;
        Pixmap pixel = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixel.setColor(this.color);
        pixel.fill();
        this.texture = new Texture(pixel);
        pixel.dispose();
        this.drawable = new TextureRegionDrawable(this.texture);
    }


    @Override
    public void dispose() {
        texture.dispose();
    }

    public boolean equalsColor(Color other) {
        return this.color.equals(other);
    }
}
