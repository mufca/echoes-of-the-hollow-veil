package io.github.mufca.libgdx.gui.core.widget;

import static io.github.mufca.libgdx.util.SafeCast.explicitCeiling;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import io.github.mufca.libgdx.gui.core.traversal.Direction;

/**
 * A rigid layout group that positions children using pure Integers. Coordinates are RELATIVE to this group's origin
 * (0,0).
 */
public class PixelLayout extends WidgetGroup {

    private final Direction direction;
    private final int gap;

    public PixelLayout(Direction direction, int gap) {
        this.direction = direction;
        this.gap = gap;
    }

    private int getChildWidth(Actor actor) {
        return explicitCeiling((actor instanceof Layout l) ? l.getPrefWidth() : actor.getWidth());
    }

    private int getChildHeight(Actor actor) {
        return explicitCeiling((actor instanceof Layout l) ? l.getPrefHeight() : actor.getHeight());
    }

    @Override
    public void layout() {
        if (direction == Direction.HORIZONTAL) {
            layoutHorizontal();
        } else {
            layoutVertical();
        }
    }

    private void layoutHorizontal() {
        // Start from local X = 0.
        int x = 0;
        int containerHeight = explicitCeiling(getHeight());

        for (Actor child : getChildren()) {
            int width = getChildWidth(child);
            int height = getChildHeight(child);

            // Center vertically within the container's height
            int y = (containerHeight - height) / 2;

            child.setBounds(x, y, width, height);

            if (child instanceof Layout l) {
                l.validate();
            }

            x += width + gap;
        }
    }

    private void layoutVertical() {
        // Start from the TOP of the container (local height).
        int y = explicitCeiling(getHeight());

        for (Actor child : getChildren()) {
            int width = getChildWidth(child);
            int height = getChildHeight(child);

            y -= height;

            // Align to left (local X = 0)
            child.setBounds(0, y, width, height);

            if (child instanceof Layout l) {
                l.validate();
            }

            // Move cursor down by gap
            y -= gap;
        }
    }

    @Override
    public float getPrefWidth() {
        int totalWidth = 0;
        int maxWidth = 0;
        for (Actor child : getChildren()) {
            int width = getChildWidth(child);
            totalWidth += width;
            maxWidth = Math.max(maxWidth, width);
        }
        if (direction == Direction.HORIZONTAL) {
            int gapTotal = Math.max(0, getChildren().size - 1) * gap;
            return totalWidth + gapTotal;
        } else {
            return maxWidth;
        }
    }

    @Override
    public float getPrefHeight() {
        int totalHeight = 0;
        int maxHeight = 0;
        for (Actor child : getChildren()) {
            int height = getChildHeight(child);
            totalHeight += height;
            maxHeight = Math.max(maxHeight, height);
        }
        if (direction == Direction.VERTICAL) {
            int gapTotal = Math.max(0, getChildren().size - 1) * gap;
            return totalHeight + gapTotal;
        } else {
            return maxHeight;
        }
    }
}