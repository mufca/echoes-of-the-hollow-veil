package io.github.mufca.libgdx.gui.screen.mainmenu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import static io.github.mufca.libgdx.util.UIHelper.BLACK_60A;
import static io.github.mufca.libgdx.util.UIHelper.getFilledColor;

/**
 * Handles keyboard and mouse navigation for a vertical menu of CoreTypingLabel items.
 * Arrow keys rotate the list; hovering updates selection to the hovered label.
 * Clicking or pressing Enter shows a content panel.
 */
public class MenuNavigator implements InputProcessor {
    private final Deque<CoreTypingLabel> labels;
    private final Table paddingContainer;
    private final Table detailTable;

    public MenuNavigator(Table paddingContainer, CoreTypingLabel... labels) {
        this.paddingContainer = paddingContainer;
        this.labels = new LinkedList<>(Arrays.asList(labels));

        // Create detail panel (hidden by default)
        detailTable = new Table();
        detailTable.setBackground(getFilledColor(BLACK_60A));
        detailTable.setVisible(false);
        detailTable.pad(30f, 30f, 30f, 30f);
        this.paddingContainer.add(new Table()).padLeft(30f);
        this.paddingContainer.add(detailTable).grow().top().left();

        // Enable hover on labels
        for (CoreTypingLabel label : this.labels) {
            label.setTouchable(Touchable.enabled);
            label.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    rotateTo(label);
                    adjustUnderline();
                }
            });
        }

        // Initial underline
        adjustUnderline();
    }

    private void rotateTo(CoreTypingLabel target) {
        while (!labels.peekFirst().equals(target)) {
            labels.addLast(labels.removeFirst());
        }
    }

    private void next() {
        labels.addLast(labels.removeFirst());
    }

    private void previous() {
        labels.addFirst(labels.removeLast());
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                previous();
                adjustUnderline();
                return true;
            case Input.Keys.DOWN:
                next();
                adjustUnderline();
                return true;
            case Input.Keys.ENTER:
                activateSelection();
                return true;
            case Input.Keys.ESCAPE:
                detailTable.setVisible(false);
            default:
                return false;
        }
    }

    /**
     * Fired on ENTER or click: shows the detail panel.
     */
    private boolean activateSelection() {
        CoreTypingLabel selected = labels.peekFirst();
        adjustUnderline();

        // Calculate paddings

        // Configure detailTable
        detailTable.clearChildren();
        detailTable.top().left();
        detailTable.add(MenuContentHandler.getContent(selected));
        detailTable.setVisible(true);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return activateSelection();
    }

    private void adjustUnderline() {
        CoreTypingLabel current = labels.peekFirst();
        for (CoreTypingLabel label : labels) {
            if (label.equals(current)) {
                label.underlineWholeText();
            } else {
                label.regenerate();
            }
        }
    }

    // Unused InputProcessor methods
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

}