package io.github.mufca.libgdx.utils.gdxextension;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;
import lombok.Setter;
import org.lwjgl.opengl.GL32;

public final class TestApplicationListener extends ApplicationAdapter {

    private DockedViewportPanel panel;
    private BitmapFont font;
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    @Setter
    private RenderCallback renderCallback;

    @Override
    public void create() {
        panel = new DockedViewportPanel();
        panel.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        font = new BitmapFont();
        panel.apply();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);
        batch.begin();
        float delta = Gdx.graphics.getDeltaTime();
        panel.update(delta);
        if (renderCallback != null) {
            renderCallback.render(panel, batch, renderer, delta);
        }
        font.setColor(Color.WHITE);
        font.draw(batch, Long.toString(System.nanoTime()), 30, Gdx.graphics.getHeight() - 30);
        batch.end();
    }

    @Override
    public void dispose() {
        panel.stage().dispose();
        font.dispose();
        batch.dispose();
        renderer.dispose();
    }
}
