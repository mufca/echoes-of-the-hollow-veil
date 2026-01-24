package io.github.mufca.libgdx.gui.core.widget;

import static com.badlogic.gdx.graphics.GL32.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest;
import static com.badlogic.gdx.graphics.Texture.TextureWrap.ClampToEdge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.mufca.libgdx.gui.shaders.ShaderHandler;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

public abstract class CoreScreen extends ScreenAdapter {

    protected Stage stage;
    protected FrameBuffer frameBufferToDraw, frameBufferToRead;
    private TextureRegion drawRegion;
    private SpriteBatch screenBatch;
    @Setter
    private List<ShaderHandler> postProcessingShaders = new ArrayList<>();  //use with caution max 3

    public void initializeShaders() {
        updateFrameBuffers();
        drawRegion = createFlippedRegion(frameBufferToDraw);
        screenBatch = new SpriteBatch();
    }

    private void updateScreenBatchProjection() {
        if (screenBatch != null) {
            screenBatch.setProjectionMatrix(
                new Matrix4().setToOrtho2D(
                    0, 0,
                    Gdx.graphics.getBackBufferWidth(),
                    Gdx.graphics.getBackBufferHeight()
                )
            );
        }
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
            updateFrameBuffers();
            updateScreenBatchProjection();
        }
    }

    @Override
    public void render(float delta) {
        captureToBufferWhenNeeded();
        Gdx.gl.glClearColor(0, 0, 0, 0); // Black clear color
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        if (stage != null) {
            stage.act(delta);
            stage.draw();
        }
        finishCapturingAndDoPostProcessing(delta);
    }

    public void initializeStageAndInputs() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private FrameBuffer getNewFrameBuffer() {
        return new FrameBuffer(Format.RGB888,
            Gdx.graphics.getBackBufferWidth(),
            Gdx.graphics.getBackBufferHeight(),
            false);
    }

    private void updateFrameBuffers() {
        if (frameBufferToDraw != null) {
            frameBufferToDraw.dispose();
        }
        if (frameBufferToRead != null) {
            frameBufferToRead.dispose();
        }
        frameBufferToRead = getNewFrameBuffer();
        frameBufferToDraw = getNewFrameBuffer();
        frameBufferToRead.getColorBufferTexture().setFilter(Nearest, Nearest);
        frameBufferToDraw.getColorBufferTexture().setFilter(Nearest, Nearest);
        frameBufferToRead.getColorBufferTexture().setWrap(ClampToEdge, ClampToEdge);
        frameBufferToDraw.getColorBufferTexture().setWrap(ClampToEdge, ClampToEdge);
    }

    private void postProcessing(List<ShaderHandler> shaders, float delta) {
        screenBatch.disableBlending();
        for (ShaderHandler handler : shaders) {
            drawRegion = createFlippedRegion(frameBufferToDraw);
            frameBufferToRead.begin();
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);
            screenBatch.setShader(handler.shader());
            screenBatch.begin();
            handler.applyUniforms(delta);
            screenBatch.draw(drawRegion, 0, 0);
            screenBatch.end();
            frameBufferToRead.end();

            FrameBuffer tmp = frameBufferToDraw;
            frameBufferToDraw = frameBufferToRead;
            frameBufferToRead = tmp;
        }
    }

    private void captureToBufferWhenNeeded() {
        if (!postProcessingShaders.isEmpty()) {
            frameBufferToDraw.begin();
            Gdx.gl.glViewport(0, 0, frameBufferToDraw.getWidth(), frameBufferToDraw.getHeight());
        }
    }

    private void finishCapturingAndDoPostProcessing(float delta) {
        if (!postProcessingShaders.isEmpty()) {
            frameBufferToDraw.end();
            postProcessing(postProcessingShaders, delta);
            drawAfterPostProcessing();
        }
    }

    private void drawAfterPostProcessing() {
        if (drawRegion == null) {
            throw new IllegalStateException("drawRegion is null. Did you forget to call super.show()?");
        }
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        Gdx.gl.glDisable(GL32.GL_SCISSOR_TEST);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);
        screenBatch.disableBlending();
        screenBatch.begin();
        screenBatch.draw(
            drawRegion,
            0, 0,
            Gdx.graphics.getBackBufferWidth(),
            Gdx.graphics.getBackBufferHeight()
        );
        screenBatch.setBlendFunction(GL32.GL_SRC_ALPHA, GL32.GL_ONE_MINUS_SRC_ALPHA);
        screenBatch.end();
    }

    private TextureRegion createFlippedRegion(FrameBuffer buffer) {
        Texture texture = buffer.getColorBufferTexture();
        TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        region.flip(false, true);
        return region;
    }

    @Override
    public void show() {
        initializeStageAndInputs();
        initializeShaders();
        updateScreenBatchProjection();
        stage.getViewport().update(
            Gdx.graphics.getBackBufferWidth(),
            Gdx.graphics.getBackBufferHeight(),
            true
        );
    }

    @Override
    public void dispose() {
        // Clean up resources when the screen is disposed
        if (stage != null) {
            stage.dispose();
        }
        if (screenBatch != null) {
            screenBatch.dispose();
        }
        if (frameBufferToDraw != null) {
            frameBufferToDraw.dispose();
        }
        if (frameBufferToRead != null) {
            frameBufferToRead.dispose();
        }
    }
}
