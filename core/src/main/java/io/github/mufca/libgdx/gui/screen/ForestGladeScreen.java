package io.github.mufca.libgdx.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.mufca.libgdx.datastructure.location.BaseLocation;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ForestGladeScreen extends ScreenAdapter {

    // mapowanie klawiszy numerycznych na kierunki
    private static final Map<Integer, String> DIRECTION_MAP = new HashMap<>();

    static {
        DIRECTION_MAP.put(Input.Keys.NUMPAD_7, "north-west");
        DIRECTION_MAP.put(Input.Keys.NUMPAD_8, "north");
        DIRECTION_MAP.put(Input.Keys.NUMPAD_9, "north-east");
        DIRECTION_MAP.put(Input.Keys.NUMPAD_4, "west");
        DIRECTION_MAP.put(Input.Keys.NUMPAD_6, "east");
        DIRECTION_MAP.put(Input.Keys.NUMPAD_1, "south-west");
        DIRECTION_MAP.put(Input.Keys.NUMPAD_2, "south");
        DIRECTION_MAP.put(Input.Keys.NUMPAD_3, "south-east");
    }

    private final LazyLocationLoader loader;
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font = new BitmapFont();
    private BaseLocation current;

    public ForestGladeScreen() throws IOException {
        loader = new LazyLocationLoader();
        current = loader.getLocation("forest_glade_0001");
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "Location: " + current.getShortDescription(), 50, 400);
        font.draw(batch, current.getLongDescription(), 50, 370);

        StringBuilder exits = new StringBuilder("Exits: ");
        for (Exit e : current.getExits()) {
            exits.append(e.name()).append(" ");
        }
        font.draw(batch, exits.toString(), 50, 340);
        batch.end();
    }

    private void handleInput() {
        for (Map.Entry<Integer, String> entry : DIRECTION_MAP.entrySet()) {
            if (Gdx.input.isKeyJustPressed(entry.getKey())) {
                String dir = entry.getValue();
                Exit targetExit = current.getExits().stream()
                    .filter(e -> e.name().equalsIgnoreCase(dir))
                    .findFirst()
                    .orElse(null);

                if (targetExit != null) {
                    try {
                        current = loader.getLocation(targetExit.targetId());
                    } catch (IOException e) {
                        System.err.println("Failed to load location: " + targetExit.targetId());
                    }
                } else {
                    System.out.println("No exit in direction: " + dir);
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
