package io.github.mufca.libgdx.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.mufca.libgdx.gui.core.GlobalInputProcessor;
import io.github.mufca.libgdx.gui.core.widget.CoreScreen;
import io.github.mufca.libgdx.gui.screen.gameplay.NPCCarousel;
import io.github.mufca.libgdx.gui.screen.gameplay.PlayerPanel;
import io.github.mufca.libgdx.gui.screen.gameplay.TextRenderer;
import io.github.mufca.libgdx.gui.screen.map.MapRenderer;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import io.github.mufca.libgdx.context.GameContext;
import io.github.mufca.libgdx.system.GameEngine;
import java.io.IOException;

public class GameplayScreen extends CoreScreen {

    private static final int MINIMAP_SIZE = 350;
    private static final int MINIMAP_OFFSET = 10;

    private final GameContext context;
    private final GameEngine engine;

    private final TextRenderer text = new TextRenderer();
    private final PlayerPanel playerPanel;
    private final MapRenderer minimap;
    private final NPCCarousel npcCarousel;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();


    public GameplayScreen() throws IOException {
        EventBus eventBus = new EventBus();
        context = new GameContext("forest_glade_0001", eventBus);
        engine = new GameEngine(context, eventBus);
        minimap = new MapRenderer(context.loader());
        minimap.computePositions(context.loader().mapCache().get("forest_glade_0001"));
        playerPanel = new PlayerPanel(context.player(), engine);
        npcCarousel = new NPCCarousel(engine.npcSystem(), context.portraitRepository());
        shapeRenderer.setAutoShapeType(true);
        Gdx.input.setInputProcessor(new GlobalInputProcessor(engine.eventBus()));
    }

    private void handleTextEvents() {
        if (engine.messageBuffer().hasMessages()) {
            engine.messageBuffer().drain().forEach(text::addText);
            engine.messageBuffer().clear();
        }
    }

    @Override
    public void show() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        text.show();
        engine.initialize();
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
        context.processTextureUpload();
        handleTextEvents();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);
        playerPanel.apply();
        playerPanel.render(delta);
        text.apply();
        text.render(minimap.viewport());
        text.update(delta);
        text.draw();

        minimap.apply();
        minimap.render(context.loader().mapCache(), context.loader().getMapLocation(context.currentLocation()), delta);
        npcCarousel.apply();
        npcCarousel.render(delta);
    }

    @Override
    public void dispose() {
        text.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        playerPanel.setBounds(0, 0, 300, height);
        text.setBounds(300, 0, width - 300, height);
        minimap.setBounds(
            width - MINIMAP_SIZE - MINIMAP_OFFSET,
            height - MINIMAP_SIZE - MINIMAP_OFFSET,
            MINIMAP_SIZE, MINIMAP_SIZE);
        npcCarousel.setBounds(width - MINIMAP_SIZE - MINIMAP_OFFSET,
            (int) (height - 1.5f * MINIMAP_SIZE - 2 * MINIMAP_OFFSET),
            MINIMAP_SIZE, (int) (0.5f * MINIMAP_SIZE)
        );
    }
}
