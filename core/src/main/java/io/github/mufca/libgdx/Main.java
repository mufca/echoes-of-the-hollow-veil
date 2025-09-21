package io.github.mufca.libgdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import io.github.mufca.libgdx.constant.AssetConstants;
import io.github.mufca.libgdx.gui.screen.Story;
import io.github.mufca.libgdx.gui.screen.mainmenu.MainMenu;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 * Manages the game's screens and global resources.
 */
public class Main extends Game {
    public static final Story storyHook = new Story();
    private Screen currentScreen;

    @Override
    public void create() {
        AssetConstants.initialize();
        // Set the initial screen
//        setScreen(new YetAnotherTestScreen());
        setScreen(new MainMenu());
//        setScreen(new CinematicScreen(
//            List.of(new CinematicStep(new TextureRegion(AssetConstants.BACKGROUND_MAIN_MENU), "Test", null,
//                    ShaderFactory.create(CINEMATIC_RISING_STAR), false, 0f, null),
//                new CinematicStep(new TextureRegion(AssetConstants.BACKGROUND_MAIN_MENU), "Test 2", null,
//                    ShaderFactory.create(CINEMATIC_RISING_STAR), false, 0f, null)
//            )));
    }

    @Override
    public Screen getScreen() {
        return currentScreen;
    }

    @Override
    public void setScreen(Screen screen) {
        // Dispose the current screen if it exists
        if (this.currentScreen != null) {
            this.currentScreen.hide();
            this.currentScreen.dispose();   //TODO: decide if dispose is needed
        }

        // Set and show the new screen
        this.currentScreen = screen;
        if (screen != null) {
            screen.show();
        }
    }

    @Override
    public void render() {
        // Delegate rendering to the current screen
        if (currentScreen != null) {
            currentScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void resize(int width, int height) {
        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }

    @Override
    public void pause() {
        if (currentScreen != null) {
            currentScreen.pause();
        }
    }

    @Override
    public void resume() {
        if (currentScreen != null) {
            currentScreen.resume();
        }
    }

    @Override
    public void dispose() {
        // Clean up all screens and resources
        if (currentScreen != null) {
            currentScreen.hide();
            currentScreen.dispose();
        }
    }
}
