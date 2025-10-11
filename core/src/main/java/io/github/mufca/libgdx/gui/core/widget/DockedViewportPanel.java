package io.github.mufca.libgdx.gui.core.widget;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;

@Getter
public class DockedViewportPanel {

    protected final ScreenViewport viewport;
    protected final OrthographicCamera camera;
    protected final Stage stage;

    public DockedViewportPanel() {
        this.viewport = new ScreenViewport();
        this.camera = (OrthographicCamera) viewport.getCamera();
        this.stage = new Stage(viewport);
    }

    public void setBounds(int x, int y, int width, int height) {
        viewport.setScreenBounds(x, y, width, height);
        viewport.setWorldSize(width, height);
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    public void apply(SpriteBatch batch) {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
    }

    public void apply(ShapeRenderer shapeRenderer) {
        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    public void apply() {
        viewport.apply(true);
    }

    public void update(float delta) {
        stage.act(delta);
    }

    public void draw() {
        stage.draw();
    }
}
