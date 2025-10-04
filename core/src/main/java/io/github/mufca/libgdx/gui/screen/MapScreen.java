package io.github.mufca.libgdx.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL32;
import io.github.mufca.libgdx.datastructure.location.BaseLocation;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;

import java.io.IOException;
import java.util.Map;

public class MapScreen implements Screen {

    private final MapRenderer mapRenderer;
    private final Map<String, BaseLocation> world;

    public MapScreen() throws IOException {
        LazyLocationLoader loader = new LazyLocationLoader("locations/forest_glade_test");
        loader.getLocation("forest_glade_0001");
        loader.getLocation("forest_glade_0002");
        loader.getLocation("forest_glade_0003");
        loader.getLocation("forest_glade_0004");
        world = loader.getCache();
        mapRenderer = new MapRenderer(loader);
        mapRenderer.computePositions(loader.getLocation("forest_glade_0001"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);
        mapRenderer.render(world, 0,0);
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