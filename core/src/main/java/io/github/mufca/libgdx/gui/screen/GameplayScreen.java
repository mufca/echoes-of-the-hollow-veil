package io.github.mufca.libgdx.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.logic.BaseLocation;
import io.github.mufca.libgdx.gui.screen.gameplay.TextRenderer;
import io.github.mufca.libgdx.gui.screen.map.MapRenderer;
import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import io.github.mufca.libgdx.scheduler.event.EventBus;
import io.github.mufca.libgdx.scheduler.event.TextEvent;
import io.github.mufca.libgdx.util.LogHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameplayScreen extends ScreenAdapter {

    private static final Map<Integer, String> DIRECTION_MAP = new HashMap<>();
    private static final int MINIMAP_SIZE = 350;
    private static final int MINIMAP_OFFSET = 10;

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

    private final TextRenderer text = new TextRenderer();
    private final LazyLocationLoader loader;
    private final MapRenderer minimap;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final TimeSystem time;
    private final EventBus eventBus;
    private final MessageRouter router;
    private BaseLocation currentLocation;

    public GameplayScreen() throws IOException {
        time = new TimeSystem();
        eventBus = new EventBus();
        router = new MessageRouter(eventBus, "forest_glade_0001");
        loader = new LazyLocationLoader(time, router);
        currentLocation = loader.getLocation("forest_glade_0001");
        eventBus.subscribe(TextEvent.class, this::handleTextEvent);
        minimap = new MapRenderer(loader);
        minimap.computePositions(loader.getMapCache().get("forest_glade_0001"));

        shapeRenderer.setAutoShapeType(true);
    }

    private void handleTextEvent(TextEvent textEvent) {
        text.addText(textEvent.textMessage());
    }

    @Override
    public void show() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        text.show();
        text.moveToLocation(currentLocation);
    }

    @Override
    public void render(float delta) {
        handleInput();
        time.update(delta);
        router.setCurrentLocationId(currentLocation.getTargetId());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);
        text.apply();
        text.render(minimap.getViewport());
        text.update(delta);
        text.draw();

        minimap.apply();
        minimap.render(loader.getMapCache(), loader.getMapLocation(currentLocation), delta);
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
                        text.moveToLocation(currentLocation);
                    } catch (IOException e) {
                        LogHelper.error(this, "Failed to load location: " + targetExit.targetId());
                    }
                } else {
                    LogHelper.info(this, "No exit in direction: " + direction);
                }
            }
        }
    }

    @Override
    public void dispose() {
        text.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        text.setBounds(300, 0, width - 300, height);
        minimap.setBounds(
            width - MINIMAP_SIZE - MINIMAP_OFFSET,
            height - MINIMAP_SIZE - MINIMAP_OFFSET,
            MINIMAP_SIZE, MINIMAP_SIZE);
    }
}
