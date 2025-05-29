package io.github.mufca.libgdx.gui.core.bookevent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface BookEvent {
    Optional<BookEvent> THE_END = Optional.empty();

    CoreTypingLabel getLabel();

    Actor getView();

    /**
     * Called when this step becomes visible.
     */
    default void onEnter() {
    }

    /**
     * Called before advancing to the next step.
     * Return true to allow transition, false to block it.
     */
    default boolean canProceed() {
        return true;
    }

    /**
     * Called before moving to the previous step.
     */
    default boolean canGoBack() {
        return true;
    }

    /**
     * Called when the step is hidden or removed.
     */
    default void onExit() {
    }

    /**
     * Called to inject accumulated data from previous steps.
     * Implementation should not modify the given ObjectNode.
     */
    default void setData(ObjectNode data) {}

    /**
     * Returns a new ObjectNode with updates made by this step.
     * Returning null means no changes.
     */
    default ObjectNode getData() {
        return null;
    }
}