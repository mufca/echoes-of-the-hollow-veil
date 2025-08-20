package io.github.mufca.libgdx.gui.screen.cinematic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.mufca.libgdx.datastructure.lowlevel.CursorList;
import io.github.mufca.libgdx.gui.core.widget.CoreScreen;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;
import io.github.mufca.libgdx.shaders.ShaderHandler;
import io.github.mufca.libgdx.util.LogHelper;

import java.util.List;

public class CinematicScreen extends CoreScreen {

    private static final String SCENE_IMAGE_AFTER_LAYOUT_SIZE = "Scene image after layout: X:%.0f Y:%.0f %.0fx%.0f";
    private final CursorList<CinematicStep> steps;
    private final Image sceneMainImage = new Image();
    private final Table root = new Table();
    private final CoreTypingLabel textLabel = new CoreTypingLabel("");
    private ShaderHandler shaderHandler;

    private float timeSinceStepStart = 0f;

    public CinematicScreen(List<CinematicStep> rawSteps) {
        this.steps = new CursorList<>();
        rawSteps.forEach(steps::add);
    }

    @Override
    public void show() {
        super.show();
        root.setFillParent(true);
        root.add(sceneMainImage).expand().fill().row();
        root.add(textLabel).pad(50f);

        stage.addActor(root);
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    advance();
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                advance();
                return true;
            }
        });
        Gdx.app.postRunnable(this::showStep);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        timeSinceStepStart += delta;

        CinematicStep step = steps.current().orElseThrow();
        if (step.autoAdvance() && timeSinceStepStart >= step.delaySeconds()) {
            advance();
        }
        if (shaderHandler != null) {
            shaderHandler.applyUniforms(delta);
        }
    }

    private void showStep() {
        timeSinceStepStart = 0f;

        CinematicStep step = steps.current().orElseThrow();

        sceneMainImage.setDrawable(new TextureRegionDrawable(step.background()));
        LogHelper.debug(this,
                SCENE_IMAGE_AFTER_LAYOUT_SIZE.formatted(
                        sceneMainImage.getX(),
                        sceneMainImage.getY(),
                        sceneMainImage.getWidth(),
                        sceneMainImage.getHeight()));
        textLabel.restart(step.text());
        if (step.narration() != null) step.narration().play();

        if (step.shader() != null) {
            shaderHandler = new ShaderHandler(step.shader());
            shaderHandler.baseOnActor(sceneMainImage);             // any actor in stage will do
            setPostProcessingShaders(List.of(shaderHandler));
        } else {
            setPostProcessingShaders(List.of());
        }

        if (step.onShow() != null) step.onShow().run();
    }

    private void advance() {
        if (steps.moveToNext()) {
            showStep();
        } else {
            Gdx.app.exit();
        }
    }
}
