package io.github.mufca.libgdx.gui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.mufca.libgdx.constant.AssetConstants;
import io.github.mufca.libgdx.gui.core.widget.CoreScreen;
import io.github.mufca.libgdx.gui.screen.cinematic.CinematicScreen;
import io.github.mufca.libgdx.gui.screen.cinematic.CinematicStep;
import io.github.mufca.libgdx.gui.screen.mainmenu.MainMenu;
import io.github.mufca.libgdx.shaders.ShaderFactory;

import java.util.List;

import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.UP;
import static io.github.mufca.libgdx.shaders.ShaderType.CINEMATIC_RISING_STAR;

public class ScreenSelector extends CoreScreen {

    private final Game game;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private final String[] options = {
        "MainMenu",
        "YetAnotherTestScreen",
        "CinematicScreen"
    };

    private int selected = 0;

    public ScreenSelector(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
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

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(UP)) {
            selected = (selected - 1 + options.length) % options.length;
        }
        if (Gdx.input.isKeyJustPressed(DOWN)) {
            selected = (selected + 1) % options.length;
        }
        if (Gdx.input.isKeyJustPressed(ENTER)) {
            switch (options[selected]) {
                case "MainMenu" -> game.setScreen(new MainMenu());
                case "YetAnotherTestScreen" -> game.setScreen(new YetAnotherTestScreen());
                case "CinematicScreen" -> game.setScreen(
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