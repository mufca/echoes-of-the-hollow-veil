package io.github.mufca.libgdx.utils;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.lwjgl.opengl.GL32;

/**
 * E2E test extension that opens a real LibGDX window and provides a fully functional DockedViewportPanel with *
 * integrated batch/renderers.
 */
public final class GdxExtension implements BeforeAllCallback, AfterAllCallback {

    private static final String THREAD_NAME = "Gdx-TestThread";
    private static final String NOT_INITIALIZED = "GdxExtension: panel not initialized within 5s";
    private static final String TITLE = "Gdx E2E: ";

    @FunctionalInterface
    public interface RenderCallback {

        void render(DockedViewportPanel panel, float delta);
    }

    private Thread gdxThread;
    private final CountDownLatch ready = new CountDownLatch(1);
    private final CountDownLatch exit = new CountDownLatch(1);
    private volatile RenderCallback renderCallback;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        gdxThread = new Thread(() -> {
            Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
            cfg.setTitle(TITLE + context.getDisplayName());
            cfg.setWindowedMode(1680, 1050);
            cfg.useVsync(false);
            cfg.setForegroundFPS(30);
            cfg.setInitialVisible(true);
            new Lwjgl3Application(new ApplicationAdapter() {
                private DockedViewportPanel panel;
                private BitmapFont font;
                private SpriteBatch batch;

                @Override
                public void create() {
                    panel = new DockedViewportPanel();
                    panel.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    batch = new SpriteBatch();
                    font = new BitmapFont();
                    ready.countDown();
                }

                @Override
                public void render() {
                    Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
                    Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);
                    batch.begin();
                    font.setColor(Color.WHITE);
                    font.draw(batch, Long.toString(System.nanoTime()), 30, Gdx.graphics.getHeight() - 30);
                    float delta = Gdx.graphics.getDeltaTime();
                    panel.update(delta);
                    panel.apply();
                    if (renderCallback != null) {
                        renderCallback.render(panel, delta);
                    }
                    batch.end();
                }

                @Override
                public void resize(int width, int height) {
                    panel.setBounds(0, 0, width, height);
                }

                @Override
                public void dispose() {
                    panel.stage().dispose();
                    font.dispose();
                    exit.countDown();
                }
            }, cfg);
        }, THREAD_NAME);

        gdxThread.start();

        if (!ready.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException(NOT_INITIALIZED);
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
    }

    public void setRenderCallback(RenderCallback callback) {
        this.renderCallback = callback;
    }
}