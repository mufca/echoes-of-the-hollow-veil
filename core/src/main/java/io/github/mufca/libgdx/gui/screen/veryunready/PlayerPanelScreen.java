package io.github.mufca.libgdx.gui.screen.veryunready;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL32;
import io.github.mufca.libgdx.system.gamecontext.GameContext;
import io.github.mufca.libgdx.gui.screen.gameplay.PlayerPanel;

public class PlayerPanelScreen implements Screen {

    private final PlayerPanel panel;

    public PlayerPanelScreen(GameContext context) {
        panel = new PlayerPanel(context);
        panel.setBounds(0, 0, 300, Gdx.graphics.getHeight());
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);
        panel.apply();
        panel.render(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
