package io.github.mufca.libgdx.gui.screen.veryunready;

import static io.github.mufca.libgdx.datastructure.keyprovider.KeyboardLayout.QWERTY_ENGLISH;
import static io.github.mufca.libgdx.gui.core.traversal.Direction.HORIZONTAL;
import static io.github.mufca.libgdx.gui.core.traversal.Direction.VERTICAL;
import static io.github.mufca.libgdx.shaders.ShaderType.WIDGET_HIGHLIGHT;
import static io.github.mufca.libgdx.shaders.ShaderType.WIDGET_STROKE;

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

public class YetAnotherTestScreen extends CoreScreen {

    TraversalRoot root = new TraversalRoot();
    TraversalContainer vertical = new TraversalContainer(VERTICAL);
    TraversalContainer horizontal = new TraversalContainer(HORIZONTAL);
    KeyProvider provider = new KeyProvider(QWERTY_ENGLISH);
    Command emptyCommand = new EmptyCommand();
    ShaderHandler focus = new ShaderHandler(ShaderFactory.create(WIDGET_HIGHLIGHT));
    ShaderHandler stroke = new ShaderHandler(ShaderFactory.create(WIDGET_STROKE));

    List<String> stringLiterals = List.of(
        "Oto jakieś", "proste labelki", "obrazujące", "działanie komponentów", "zgrupowanych",
        "przy użyciu", "TraversalRoot", "oraz TraversalContainer", "w jedną logiczną całość",
        "po której można nawigować");
    Actor currentWidget;
    Actor currentTable;

    @Override
    public void show() {
        super.show();
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
        tVertical.setPosition(stage.getWidth() / 18, stage.getHeight() / 1.3f);
        tHorizontal.setPosition(stage.getWidth() / 18, stage.getHeight() / 12);
        tVertical.pack();
        tHorizontal.pack();
        stage.setKeyboardFocus(stage.getRoot());
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keyCode) {
                return root.handleKeyDown(keyCode);
            }
        });
        initializeShaders();
        postProcessingShaders(List.of(stroke, focus));
    }

    @Override
    public void render(float delta) {
        updateCurrentWidgets();
        focus.baseOnActor(currentWidget);
        stroke.baseOnActor(currentTable);
        super.render(delta);
    }

    private void updateCurrentWidgets() {
        if (currentWidget != root.currentWidget().orElseThrow().asActor()) {
            currentWidget = root.currentWidget().orElseThrow().asActor();
            focus.resetElapsed();
            if (currentTable != currentWidget.getParent()) {
                currentTable = currentWidget.getParent();
                stroke.resetElapsed();
            }
        }
    }

    @Override
    public void hide() {
        super.dispose();
    }
}
