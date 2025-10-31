package io.github.mufca.libgdx.gui.core.portrait;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public record PortraitEntry(Long portraitId, PortraitFile portraitFile, String path, Texture texture,
                            TextureRegion region) implements Disposable {

    @Override
    public void dispose() {
        texture.dispose();
    }
}
