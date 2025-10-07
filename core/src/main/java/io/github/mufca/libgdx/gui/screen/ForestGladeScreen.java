package io.github.mufca.libgdx.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.mufca.libgdx.datastructure.location.BaseLocation;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.util.LogHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ForestGladeScreen extends ScreenAdapter {

    private static final Map<Integer, String> DIRECTION_MAP = new HashMap<>();
    public static final int MINIMAP_SIZE = 350;
    public static final int MINIMAP_OFFSET = 10;

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

    private final ScreenViewport minimapViewport = new ScreenViewport();
    private final ScreenViewport mainViewport = new ScreenViewport();
    private final LazyLocationLoader loader;
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font = new BitmapFont();
    private final MapRenderer mapRenderer;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private BaseLocation currentLocation;

    public ForestGladeScreen() throws IOException {
        loader = new LazyLocationLoader();
        currentLocation = loader.getLocation("forest_glade_0001");

        mapRenderer = new MapRenderer(loader, minimapViewport);
        mapRenderer.computePositions(loader.getMapCache().get("forest_glade_0001"));

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.setAutoShapeType(true);
    }

    @Override
    public void render(float delta) {
        handleInput();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);
        mainViewport.apply();

        batch.begin();
        font.draw(batch, "Location: " + currentLocation.getShortDescription(), 50, 400);
        font.draw(batch, currentLocation.getLongDescription(), 50, 370);

        StringBuilder exits = new StringBuilder("Exits: ");
        for (Exit e : currentLocation.getExits()) {
            exits.append(e.name()).append(" ");
        }
        font.draw(batch, exits.toString(), 50, 340);
        batch.end();

        minimapViewport.apply();
        mapRenderer.render(loader.getMapCache(), loader.getMapLocation(currentLocation));

        drawMinimapBorder();

    }

    private void drawMinimapBorder() {
        mainViewport.apply();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(
            minimapViewport.getScreenX(),
            minimapViewport.getScreenY(),
            minimapViewport.getScreenWidth(),
            minimapViewport.getScreenHeight()
        );
        shapeRenderer.end();
    }

    private void handleInput() {
        for (Map.Entry<Integer, String> entry : DIRECTION_MAP.entrySet()) {
            if (Gdx.input.isKeyJustPressed(entry.getKey())) {
                String direction = entry.getValue();
                Exit targetExit = currentLocation.getExits().stream()
                    .filter(e -> e.name().equalsIgnoreCase(direction))
                    .findFirst()
                    .orElse(null);

                if (targetExit != null) {
                    try {
                        currentLocation = loader.getLocation(targetExit.targetId());
                    } catch (IOException e) {
                        LogHelper.error(this,"Failed to load location: " + targetExit.targetId());
                    }
                } else {
                    LogHelper.info(this, "No exit in direction: " + direction);
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void resize (int width, int height) {
        mainViewport.setScreenBounds(0,0, width, height);
        minimapViewport.setScreenBounds(
            width- MINIMAP_SIZE - MINIMAP_OFFSET,
            height - MINIMAP_SIZE - MINIMAP_OFFSET,
            MINIMAP_SIZE, MINIMAP_SIZE);
    }
}
