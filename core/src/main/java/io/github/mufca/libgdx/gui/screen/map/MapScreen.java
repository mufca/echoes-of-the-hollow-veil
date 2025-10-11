package io.github.mufca.libgdx.gui.screen.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.MapLocation;
import java.io.IOException;
import java.util.Map;

public class MapScreen implements Screen {

    private final MapRenderer mapRenderer;
    private final Map<String, MapLocation> world;
    private final ScreenViewport viewport = new ScreenViewport();
    private final MapLocation location;

    public MapScreen() throws IOException {
        LazyLocationLoader loader = new LazyLocationLoader();
        viewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        world = loader.getMapCache();
        mapRenderer = new MapRenderer(loader, viewport);
        location = loader.getMapCache().get(("forest_glade_0001"));
        mapRenderer.computePositions(location);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);
        viewport.apply();
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