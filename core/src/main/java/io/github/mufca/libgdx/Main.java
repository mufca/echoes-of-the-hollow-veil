package io.github.mufca.libgdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import io.github.mufca.libgdx.constant.TextureConstants;
import io.github.mufca.libgdx.gui.screen.Story;
import io.github.mufca.libgdx.gui.screen.YetAnotherTestScreen;

import java.util.Arrays;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    public static final Story storyHook = new Story();

    @Override
    public void create() {
        changeToBorderless();
        TextureConstants.init();
//        setScreen(new Story());
//        setScreen(new MainMenu());
//        setScreen(new ModalTestScreen());
        setScreen(new YetAnotherTestScreen());
    }

    private void changeToBorderless() {
        Graphics.DisplayMode displayMode = Arrays.stream(Gdx.graphics.getDisplayModes()).sequential()
            .reduce((first, second) -> second).orElseThrow();
        Gdx.graphics.setUndecorated(true);
        Gdx.graphics.setWindowedMode(displayMode.width, displayMode.height);
    }
}
