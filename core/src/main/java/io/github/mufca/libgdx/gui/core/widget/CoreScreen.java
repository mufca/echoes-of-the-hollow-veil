package io.github.mufca.libgdx.gui.core.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.mufca.libgdx.shaders.ShaderHandler;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL32.GL_COLOR_BUFFER_BIT;

public abstract class CoreScreen extends ScreenAdapter {
    protected Stage stage;
    protected FrameBuffer frameBufferToDraw, frameBufferToRead;
    private TextureRegion drawRegion;
    private SpriteBatch screenBatch;
    @Setter
    private List<ShaderHandler> postProcessingShaders = new ArrayList<>();  //use with caution max 3

    public void initializeShaders() {
        updateFrameBuffers();
        drawRegion = getFBRegion(frameBufferToDraw);
        screenBatch = new SpriteBatch();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
            updateFrameBuffers();
        }
    }

    @Override
    public void render(float delta) {
        captureToBufferWhenNeeded();
        Gdx.gl.glClearColor(0, 0, 0, 1); // Black clear color
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
        return new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    private TextureRegion getFBRegion(FrameBuffer frameBuffer) {
        return new TextureRegion(frameBuffer.getColorBufferTexture());
    }

    private void updateFrameBuffers() {
        frameBufferToRead = getNewFrameBuffer();
        frameBufferToDraw = getNewFrameBuffer();
    }

    private void postProcessing(List<ShaderHandler> shaders, float delta) {
        for (ShaderHandler handler : shaders) {
            drawRegion = createFlippedRegion(frameBufferToDraw);
            frameBufferToRead.begin();
            screenBatch.setShader(handler.getShader());
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
        screenBatch.begin();
        screenBatch.draw(drawRegion, 0, 0);
        screenBatch.end();
    }

    private TextureRegion createFlippedRegion(FrameBuffer buffer) {
        TextureRegion region = new TextureRegion(buffer.getColorBufferTexture());
        region.flip(false, true);
        return region;
    }

    @Override
    public void show() {
        initializeStageAndInputs();
        initializeShaders();
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
