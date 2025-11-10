package io.github.mufca.libgdx.utils.gdxextension;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;

@FunctionalInterface
public interface RenderCallback {

    void render(DockedViewportPanel panel, SpriteBatch batch, ShapeRenderer renderer, float delta);
}
