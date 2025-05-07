package io.github.mufca.libgdx.gui.core.widget;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public abstract class CoreScreen extends ApplicationAdapter implements Screen {
    protected Stage stage;

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void initializeStageAndInputs() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    public void defaultStageRender(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Black clear color
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }
}
