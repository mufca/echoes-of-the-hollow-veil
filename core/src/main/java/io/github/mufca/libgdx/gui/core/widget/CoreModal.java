package io.github.mufca.libgdx.gui.core.widget;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.mufca.libgdx.datastructure.utils.ListHelper;
import io.github.mufca.libgdx.gui.core.bookevent.BookEvent;
import io.github.mufca.libgdx.util.JsonHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.mufca.libgdx.gui.core.bookevent.BookEvent.THE_END;

/**
 * Simple modal overlay with a black background, implemented as a Table subclass.
 * Each instance represents one layer (LOWER or UPPER).
 */
public class CoreModal extends Table {
    private final Table stepListTable = new Table();
    private final Table rightContentTable = new Table();
    private final List<BookEvent> steps;
    private final boolean progressiveMode;
    private BookEvent currentEvent;
    private ObjectNode collectedData = JsonNodeFactory.instance.objectNode();

    /**
     * @param stage The Stage to which the modal will be added.
     * @param layer Determines stacking: LOWER is added first (over the game UI),
     *              UPPER is added last (on top of everything).
     */
    public CoreModal(Stage stage, Layer layer, List<BookEvent> steps, boolean progressive) {
        super();
        this.steps = steps;
        this.progressiveMode = progressive;

        prepare(stage);
        setLayer(stage, layer);
        buildStructure();

        this.currentEvent = steps.getFirst();
        renderCurrentStep();
    }

    private void setLayer(Stage stage, Layer layer) {
        stage.getRoot().addActorAt(layer.getLayerIndex(), this);
    }

    private void prepare(Stage stage) {
        setFillParent(true);
        setVisible(false);
        float padX = stage.getWidth() * 0.1f;
        float padY = stage.getHeight() * 0.1f;
        pad(padY, padX, padY, padX);
    }

    private void buildStructure() {
        clearChildren();
        left().top();

        stepListTable.left().top();
        rightContentTable.left().top();

        add(stepListTable).width(200).top().left();
        add(rightContentTable).expand().fill();
    }

    private void renderCurrentStep() {
        stepListTable.clear();
        rightContentTable.clear();

        List<BookEvent> visibleSteps = progressiveMode
            ? ListHelper.upToInclusive(steps, currentEvent)
            : List.copyOf(steps);

        visibleSteps.forEach(step ->
            stepListTable.add(step.getLabel()).left().row()
        );
        if (currentEvent != null) {
            currentEvent.setData(collectedData);
            currentEvent.onEnter();
            rightContentTable.add(currentEvent.getView()).expand().fill();
        }
    }

    public void show() {
        setVisible(true);
        renderCurrentStep();
    }

    public void hide() {
        setVisible(false);
    }

    public void next() {
        saveCurrentStepData();
        currentEvent = find(steps::stream).orElse(currentEvent);
        renderCurrentStep();
    }

    public void previous() {
        saveCurrentStepData();
        currentEvent = find(() -> ListHelper.reverseToStream(steps)).orElse(currentEvent);
        renderCurrentStep();
    }

    private Optional<BookEvent> find(Supplier<Stream<BookEvent>> direction) {
        Iterator<BookEvent> it = direction.get().iterator();
        while (it.hasNext()) {
            BookEvent current = it.next();
            if (current.equals(currentEvent) && it.hasNext()) {
                return Optional.of(it.next());
            }
        }
        return THE_END;
    }

    private void saveCurrentStepData() {
        if (currentEvent != null) {
            ObjectNode updates = currentEvent.getData();
            collectedData = JsonHelper.merge(collectedData, updates);
        }
    }
}
