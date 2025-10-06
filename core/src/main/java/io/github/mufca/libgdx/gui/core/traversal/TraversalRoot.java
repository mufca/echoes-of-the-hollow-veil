package io.github.mufca.libgdx.gui.core.traversal;

import com.badlogic.gdx.Input;
import io.github.mufca.libgdx.datastructure.lowlevel.CursorList;
import io.github.mufca.libgdx.gui.core.interfaces.WithCommand;
import io.github.mufca.libgdx.gui.core.interfaces.WithShortcut;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Getter;

public class TraversalRoot {

    private TraversalContainer head, tail;
    @Getter
    private TraversalContainer currentContainer;
    private WithCommand currentWidget;

    public TraversalRoot() {
    }

    public void add(TraversalContainer container) {
        if (currentContainer == null) {
            currentContainer = head = tail = container;
            setCurrentWidget(container.getContent().current().orElseThrow());
        } else {
            tail.setNext(container);
            container.setPrevious(tail);
            tail = container;
        }
    }

    public boolean setCurrentContainer(TraversalContainer container) {
        TraversalContainer cursor = head;
        while (cursor != null) {
            if (cursor == container) {
                currentContainer = container;
                setCurrentWidget(currentContainer.getContent().current().orElseThrow());
                return true;
            }
            cursor = cursor.getNext();
        }
        return false;
    }

    public Stream<TraversalContainer> streamContainers() {
        List<TraversalContainer> list = new ArrayList<>();
        for (TraversalContainer cursor = head; cursor != null; cursor = cursor.getNext()) {
            list.add(cursor);
        }
        return list.stream();
    }

    public boolean moveToNextContainer() {
        if (currentContainer != null && currentContainer.hasNext()) {
            setCurrentContainer(currentContainer = currentContainer.getNext());
            setCurrentWidget(currentContainer.getContent().current().orElseThrow());
            return true;
        }
        return false;
    }

    public boolean moveToPreviousContainer() {
        if (currentContainer != null && currentContainer.hasPrevious()) {
            setCurrentContainer(currentContainer.getPrevious());
            setCurrentWidget(currentContainer.getContent().current().orElseThrow());
            return true;
        }
        return false;
    }

    public boolean handleKeyDown(int keycode) {
        if (currentContainer == null) {
            return false;
        }

        Direction direction = currentContainer.getDirection();
        CursorList<WithCommand> widgets = currentContainer.getContent();

        boolean moved = false;

        switch (direction) {
            case VERTICAL -> {
                if (keycode == Input.Keys.UP) {
                    moved = widgets.moveToPrevious();
                } else if (keycode == Input.Keys.DOWN) {
                    moved = widgets.moveToNext();
                } else if (keycode == Input.Keys.LEFT) {
                    moved = moveToPreviousContainer();
                } else if (keycode == Input.Keys.RIGHT) {
                    moved = moveToNextContainer();
                }
            }
            case HORIZONTAL -> {
                if (keycode == Input.Keys.LEFT) {
                    moved = widgets.moveToPrevious() || moveToPreviousContainer();
                } else if (keycode == Input.Keys.RIGHT) {
                    moved = widgets.moveToNext() || moveToNextContainer();
                }
            }
        }

        if (moved) {
            setCurrentWidget(widgets.current().orElseThrow());
            setCurrentWidget(currentContainer.getContent().current().orElseThrow());
            return true;
        }

        return handleShortcuts(keycode);
    }

    private boolean handleShortcuts(int keycode) {
        return streamContainers()
            .flatMap(container -> container.getContent().stream())
            .flatMap(wc -> (wc instanceof WithShortcut ws) ? Stream.of(ws) : Stream.empty())
            .filter(widget -> widget.getShortcutBinding() != null)
            .filter(widget -> widget.getShortcutBinding().matches(keycode))
            .findFirst()
            .map(widget -> {
                setCurrentContainerContaining(widget);
                setCurrentWidget(widget);
                return true;
            })
            .orElse(false);
    }

    private void setCurrentContainerContaining(WithCommand target) {
        streamContainers().forEach(container -> {
            if (container.getContent().contains(target)) {
                setCurrentContainer(container);
                container.getContent().setCurrent(target);
            }
        });
    }

    public Optional<WithCommand> currentWidget() {
        return Optional.of(currentWidget);
    }

    public void setCurrentWidget(WithCommand widget) {
        if (currentWidget != null) {
            currentWidget.forget();
        }
        currentWidget = widget;
        if (currentWidget != null) {
            currentWidget.focus();
        }
    }
}
