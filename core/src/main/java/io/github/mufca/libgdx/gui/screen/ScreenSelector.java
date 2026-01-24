package io.github.mufca.libgdx.gui.screen;

import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.UP;
import static io.github.mufca.libgdx.gui.shaders.ShaderType.CINEMATIC_RISING_STAR;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.mufca.libgdx.constant.AssetConstants;
import io.github.mufca.libgdx.system.gamecontext.GameContext;
import io.github.mufca.libgdx.gui.core.widget.CoreScreen;
import io.github.mufca.libgdx.gui.screen.cinematic.CinematicScreen;
import io.github.mufca.libgdx.gui.screen.cinematic.CinematicStep;
import io.github.mufca.libgdx.gui.screen.mainmenu.MainMenu;
import io.github.mufca.libgdx.gui.screen.map.MapScreen;
import io.github.mufca.libgdx.gui.screen.veryunready.PlayerPanelScreen;
import io.github.mufca.libgdx.gui.screen.veryunready.Story;
import io.github.mufca.libgdx.gui.screen.veryunready.YetAnotherTestScreen;
import io.github.mufca.libgdx.gui.shaders.ShaderFactory;
import java.io.IOException;
import java.util.List;
import lombok.SneakyThrows;

public class ScreenSelector extends CoreScreen {

    private static final String MAIN_MENU = "MainMenu";
    private static final String PLAYER_PANEL = "PlayerPanel";
    private static final String YET_ANOTHER_TEST_SCREEN = "YetAnotherTestScreen";
    private static final String CINEMATIC_SCREEN = "CinematicScreen";
    private static final String GAMEPLAY_SCREEN = "GameplayScreen";
    private static final String MAP_SCREEN = "MapScreen";
    private static final String STORY = "Story";
    private final Game game;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private final String[] options = {
        MAIN_MENU,
        YET_ANOTHER_TEST_SCREEN,
        CINEMATIC_SCREEN,
        GAMEPLAY_SCREEN,
        MAP_SCREEN,
        PLAYER_PANEL,
        STORY
    };

    private int selected = 0;

    public ScreenSelector(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
    }

    @SneakyThrows
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for (int i = 0; i < options.length; i++) {
            if (i == selected) {
                font.setColor(1, 1, 0, 1);
            } else {
                font.setColor(1, 1, 1, 1);
            }
            font.draw(batch, options[i], 100, 800 - i * 50);
        }
        batch.end();

        handleInput();
    }

    private void handleInput() throws IOException {
        if (Gdx.input.isKeyJustPressed(UP)) {
            selected = (selected - 1 + options.length) % options.length;
        }
        if (Gdx.input.isKeyJustPressed(DOWN)) {
            selected = (selected + 1) % options.length;
        }
        if (Gdx.input.isKeyJustPressed(ENTER)) {
            switch (options[selected]) {
                case MAIN_MENU -> game.setScreen(new MainMenu());
                case YET_ANOTHER_TEST_SCREEN -> game.setScreen(new YetAnotherTestScreen());
                case GAMEPLAY_SCREEN -> game.setScreen(new GameplayScreen());
                case STORY -> game.setScreen(new Story());
                case MAP_SCREEN -> game.setScreen(new MapScreen());
                case PLAYER_PANEL -> game.setScreen(new PlayerPanelScreen(new GameContext("forest_glade_0001")));
                case CINEMATIC_SCREEN -> game.setScreen(
                    new CinematicScreen(
                        List.of(
                            new CinematicStep(
                                new TextureRegion(AssetConstants.BACKGROUND_MAIN_MENU),
                                "Test",
                                null,
                                ShaderFactory.create(CINEMATIC_RISING_STAR),
                                false, 0f, null
                            ),
                            new CinematicStep(
                                new TextureRegion(AssetConstants.BACKGROUND_MAIN_MENU),
                                "Test 2",
                                null,
                                ShaderFactory.create(CINEMATIC_RISING_STAR),
                                false, 0f, null
                            )
                        )
                    )
                );
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}