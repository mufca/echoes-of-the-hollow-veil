package io.github.mufca.libgdx.gui.core.widget;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public abstract class CoreScreen extends ApplicationAdapter implements Screen {
    protected Stage stage;
    protected FrameBuffer frameBufferToDraw, frameBufferToRead;
    private TextureRegion drawRegion;
    private SpriteBatch screenBatch;
    @Setter
    private List<ShaderHandler> shaders = new ArrayList<>();

    public void initializeShaders() {
        updateFrameBuffers();
        drawRegion = getFBRegion(frameBufferToDraw);
        screenBatch = new SpriteBatch();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        updateFrameBuffers();
    }

    @Override
    public void render(float delta) {
        captureToBufferWhenNeeded();
        Gdx.gl.glClearColor(0, 0, 0, 1); // Black clear color
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
            drawRegion = new TextureRegion(frameBufferToDraw.getColorBufferTexture());
            drawRegion.flip(false, true);
            frameBufferToRead.begin();
            screenBatch.setShader(handler.getShader());
            screenBatch.begin();
            handler.applyUniforms(delta);
            screenBatch.draw(drawRegion,0,0);
            screenBatch.end();
            frameBufferToRead.end();

            FrameBuffer tmp = frameBufferToDraw;
            frameBufferToDraw = frameBufferToRead;
            frameBufferToRead = tmp;


        }
    }

    private void captureToBufferWhenNeeded() {
        if (!shaders.isEmpty()) {
            frameBufferToDraw.begin();
        }
    }

    private void finishCapturingAndDoPostProcessing(float delta) {
        if (!shaders.isEmpty()) {
            frameBufferToDraw.end();
            postProcessing(shaders, delta);
            drawAfterPostProcessing();
        }
    }

    private void drawAfterPostProcessing() {
        screenBatch.begin();
        screenBatch.draw(drawRegion,0,0);
        screenBatch.end();
    }
}
