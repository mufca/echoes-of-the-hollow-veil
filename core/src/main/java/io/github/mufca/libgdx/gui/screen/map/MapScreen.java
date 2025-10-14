package io.github.mufca.libgdx.gui.screen.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL32;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.jsondata.MapLocation;
import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import io.github.mufca.libgdx.scheduler.event.EventBus;
import java.io.IOException;
import java.util.Map;

public class MapScreen implements Screen {

    private final MapRenderer mapRenderer;
    private final Map<String, MapLocation> world;
    private final MapLocation location;
    private final TimeSystem time;
    private final MessageRouter router;

    public MapScreen() throws IOException {
        time = new TimeSystem();
        router = new MessageRouter(new EventBus(), "forest_glade_0001");
        LazyLocationLoader loader = new LazyLocationLoader(time, router);
        world = loader.getMapCache();
        mapRenderer = new MapRenderer(loader);
        mapRenderer.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        location = loader.getMapCache().get(("forest_glade_0001"));
        mapRenderer.computePositions(location);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);
        mapRenderer.apply();
        mapRenderer.render(world, location);
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