package io.github.mufca.libgdx.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.mufca.libgdx.datastructure.command.Command;
import io.github.mufca.libgdx.datastructure.keyprovider.KeyProvider;
import io.github.mufca.libgdx.datastructure.keyprovider.KeyboardLayout;
import io.github.mufca.libgdx.gui.core.icon.KeycapIcon;
import io.github.mufca.libgdx.gui.core.interfaces.implementations.ActorWithShortcut;
import io.github.mufca.libgdx.gui.core.widget.CoreScreen;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;
import io.github.mufca.libgdx.shaders.ShaderFactory;
import io.github.mufca.libgdx.shaders.ShaderType;
import io.github.mufca.libgdx.util.UIHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;


public class ModalTestScreen extends CoreScreen {

    private final ShaderProgram shader = ShaderFactory.create(ShaderType.WIDGET_HIGHLIGHT).program();
    List<ActorWithShortcut<CoreTypingLabel>> componentList = new ArrayList<>();
    float elapsed = 0f;

    @Override
    public void show() {
        initializeStageAndInputs();
        TextureRegionDrawable filledColor = UIHelper.getFilledColor(Color.BLACK);
        Image image = new Image(filledColor);
        image.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(image);
        Table t = new Table();
        t.left().padLeft(500).padBottom(2000);
        stage.addActor(t);
        for (KeyboardLayout layout : KeyboardLayout.values()) {
            KeyProvider provider = new KeyProvider(layout);
            for (int i = 0; i < 36; i++) {
                if ((i + 1) % 10 == 0) {
                    t.add(new KeycapIcon(provider.takeNextKey(), 28f)).pad(5f).row();
                } else {
                    t.add(new KeycapIcon(provider.takeNextKey(), 28f)).pad(5f);
                }
            }
            t.add(new CoreTypingLabel("")).row();
        }
        ActorWithShortcut<CoreTypingLabel> actorWithShortcut = new ActorWithShortcut<>(new CoreTypingLabel(""),
            new KeycapIcon(new KeyProvider(KeyboardLayout.QWERTY_ENGLISH).takeNextKey(), 28f), new Command() {
            @Override
            public void execute() {

            }

            @Override
            public void enable(boolean isEnabled) {

            }
        });
/*        List<KeycapIcon> icons =
            List.of(new KeycapIcon(provider.takeNextKey(), 28f),
                new KeycapIcon(provider.takeNextKey(), 28f),
                new KeycapIcon(provider.takeNextKey(), 28f),
                new KeycapIcon(provider.takeNextKey(), 28f),
                new KeycapIcon(provider.takeNextKey(), 28f),
                new KeycapIcon(provider.takeNextKey(), 28f));
        List<CoreTypingLabel> labels =
            List.of(new CoreTypingLabel("Jeden"),
                new CoreTypingLabel("Dwa"),
                new CoreTypingLabel("Trzy"),
                new CoreTypingLabel("Cztery"),
                new CoreTypingLabel("Pięć"),
                new CoreTypingLabel("Sześć"));
        for (int i = 0; i < labels.size(); i++) {
            ActorWithShortcut<CoreTypingLabel> component = new ActorWithShortcut<>(labels.get(i), icons.get(i),
                new Command() {
                    @Override
                    public void execute() {

                    }

                    @Override
                    public void enable(boolean isEnabled) {

                    }
                });
            componentList.add(component);
            t.add(component).left().row();
        }*/
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        Batch batch = stage.getBatch();

        float duration = 0.5f;
        float brushProgress = Math.min((elapsed += delta) / duration, 1f);

        Optional<ActorWithShortcut<CoreTypingLabel>> optComp =
            componentList.stream()
                .filter(c -> c.getActor().getUnformattedText().equals("Pięć"))
                .findFirst();

        if (optComp.isPresent()) {
            ActorWithShortcut<CoreTypingLabel> component = optComp.get();
            Vector2 screenPos = component.localToStageCoordinates(new Vector2(0, 0));
            float minX = screenPos.x;
            float minY = screenPos.y;
            float maxX = minX + component.getWidth();
            float maxY = minY + component.getHeight();

            batch.setShader(shader);

            shader.setUniformf("u_brushPos", brushProgress);
            shader.setUniformf("u_boundsMin", minX, minY);
            shader.setUniformf("u_boundsMax", maxX, maxY);
        }

        stage.act(delta);
        stage.draw();
        batch.setShader(null);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
