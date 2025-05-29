package io.github.mufca.libgdx.gui.screen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.mufca.libgdx.datastructure.command.Command;
import io.github.mufca.libgdx.datastructure.command.EmptyCommand;
import io.github.mufca.libgdx.datastructure.keyprovider.KeyProvider;
import io.github.mufca.libgdx.gui.core.icon.KeycapIcon;
import io.github.mufca.libgdx.gui.core.interfaces.implementations.ActorWithShortcut;
import io.github.mufca.libgdx.gui.core.traversal.TraversalContainer;
import io.github.mufca.libgdx.gui.core.traversal.TraversalRoot;
import io.github.mufca.libgdx.gui.core.widget.CoreScreen;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;
import io.github.mufca.libgdx.shaders.ShaderFactory;
import io.github.mufca.libgdx.shaders.ShaderHandler;
import io.github.mufca.libgdx.util.WidgetBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.mufca.libgdx.datastructure.keyprovider.KeyboardLayout.QWERTY_ENGLISH;
import static io.github.mufca.libgdx.gui.core.traversal.Direction.HORIZONTAL;
import static io.github.mufca.libgdx.gui.core.traversal.Direction.VERTICAL;
import static io.github.mufca.libgdx.shaders.ShaderType.WIDGET_HIGHLIGHT;
import static io.github.mufca.libgdx.shaders.ShaderType.WIDGET_STROKE;

public class YetAnotherTestScreen extends CoreScreen {
    TraversalRoot root = new TraversalRoot();
    TraversalContainer vertical = new TraversalContainer(VERTICAL);
    TraversalContainer horizontal = new TraversalContainer(HORIZONTAL);
    KeyProvider provider = new KeyProvider(QWERTY_ENGLISH);
    Command emptyCommand = new EmptyCommand();
    ShaderHandler focus = new ShaderHandler(ShaderFactory.create(WIDGET_HIGHLIGHT));
    ShaderHandler strokeTop = new ShaderHandler(ShaderFactory.create(WIDGET_STROKE));
    ShaderHandler strokeBottom = new ShaderHandler(ShaderFactory.create(WIDGET_STROKE));

    List<String> stringLiterals = List.of(
        "Oto jakieś", "proste labelki", "obrazujące", "działanie komponentów", "zgrupowanych",
        "przy użyciu", "TraversalRoot", "oraz TraversalContainer", "w jedną logiczną całość",
        "po której można nawigować");
    Actor currentWidget;
    Actor currentTable;
    float elapsed = 0f;

    @Override
    public void show() {
        initializeStageAndInputs();
        List<ActorWithShortcut<CoreTypingLabel>> widgets = new ArrayList<>();
        for (String text : stringLiterals) {
            widgets.add(
                new ActorWithShortcut<>(
                    new CoreTypingLabel(text),
                    new KeycapIcon(provider.takeNextKey(), 28f),
                    emptyCommand));
        }
        vertical.add(widgets.getFirst());
        vertical.add(widgets.get(1));
        vertical.add(widgets.get(2));
        horizontal.add(widgets.get(3));
        horizontal.add(widgets.get(4));
        horizontal.add(widgets.get(5));
        horizontal.add(widgets.get(6));
        horizontal.add(widgets.get(7));
        horizontal.add(widgets.get(8));
        horizontal.add(widgets.get(9));
        root.add(vertical);
        root.add(horizontal);
        Table tVertical = WidgetBuilder.buildTable(vertical);
        Table tHorizontal = WidgetBuilder.buildTable(horizontal);
        stage.addActor(tVertical);
        stage.addActor(tHorizontal);
        tVertical.setPosition(300,900);
        tHorizontal.setPosition(300,300);
        tVertical.pack();
        tHorizontal.pack();
        stage.setKeyboardFocus(stage.getRoot());
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keyCode) {
                return root.handleKeyDown(keyCode);
            }
        });
        updateCurrentWidgets();
        initializeShaders();
    }

    @Override
    public void render(float delta) {
        updateCurrentWidgets();
        Vector2 screenPos = currentWidget.localToStageCoordinates(new Vector2(0, 0));
        float minX = screenPos.x;
        float minY = screenPos.y;
        float maxX = minX + currentWidget.getWidth();
        float maxY = minY + currentWidget.getHeight();
        Map<String, Object> focusUniforms = Map.of(
            "u_boundsMin", new Vector2(minX, minY),
            "u_boundsMax", new Vector2(maxX, maxY));
        focus.setUniforms(focusUniforms);
        screenPos = adjustCoordinates(currentTable.localToScreenCoordinates(new Vector2(0, 0)));
        minX = screenPos.x;
        minY = screenPos.y + currentTable.getHeight();
        float strokeLength = currentTable.getWidth();
        float strokeWidth = 3f;
        Map<String, Object> strokeTopUniforms = Map.of(
            "provideStartingPosition", new Vector2(minX, minY),
            "strokeLength", strokeLength,
            "strokeWidth", strokeWidth);
        strokeTop.setUniforms(strokeTopUniforms);
        minY = screenPos.y;
        Map<String, Object> strokeBottomUniforms = Map.of(
            "provideStartingPosition", new Vector2(minX, minY),
            "strokeLength", strokeLength,
            "strokeWidth", strokeWidth);
        strokeBottom.setUniforms(strokeBottomUniforms);
        setShaders(List.of(strokeTop, focus, strokeBottom));
        super.render(delta);
    }

    private void updateCurrentWidgets() {
        if (currentWidget != root.currentWidget().orElseThrow().asActor()) {
            elapsed = 0f;
            currentWidget = root.currentWidget().orElseThrow().asActor();
            if (currentTable != currentWidget.getParent()) {
                currentTable = currentWidget.getParent();
                strokeBottom.resetElapsed();
                strokeTop.resetElapsed();
            }
            focus.resetElapsed();
        }
    }

    private Vector2 adjustCoordinates(Vector2 vector2) {
        return vector2.set(vector2.x, stage.getHeight() - vector2.y);
    }

    @Override
    public void hide() {
        super.dispose();
    }
}
