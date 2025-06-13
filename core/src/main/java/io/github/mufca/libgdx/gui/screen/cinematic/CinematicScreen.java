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

import java.util.List;

public class CinematicScreen extends CoreScreen {

    private final CursorList<CinematicStep> steps;
    private final Image backgroundImage = new Image();
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
        Table root = new Table();
        root.setFillParent(true);
        root.add(backgroundImage).expand().fill().row();
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

        showStep();
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

        backgroundImage.setDrawable(new TextureRegionDrawable(step.background()));
        textLabel.restart(step.text());
        if (step.narration() != null) step.narration().play();

        if (step.shader() != null) {
            shaderHandler = new ShaderHandler(step.shader());
            shaderHandler.baseOnActor(backgroundImage);             // any actor in stage will do
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
