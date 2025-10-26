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
            setCurrentWidget(container.content().current().orElseThrow());
        } else {
            tail.next(container);
            container.previous(tail);
            tail = container;
        }
    }

    public boolean setCurrentContainer(TraversalContainer container) {
        TraversalContainer cursor = head;
        while (cursor != null) {
            if (cursor == container) {
                currentContainer = container;
                setCurrentWidget(currentContainer.content().current().orElseThrow());
                return true;
            }
            cursor = cursor.next();
        }
        return false;
    }

    public Stream<TraversalContainer> streamContainers() {
        List<TraversalContainer> list = new ArrayList<>();
        for (TraversalContainer cursor = head; cursor != null; cursor = cursor.next()) {
            list.add(cursor);
        }
        return list.stream();
    }

    public boolean moveToNextContainer() {
        if (currentContainer != null && currentContainer.hasNext()) {
            setCurrentContainer(currentContainer = currentContainer.next());
            setCurrentWidget(currentContainer.content().current().orElseThrow());
            return true;
        }
        return false;
    }

    public boolean moveToPreviousContainer() {
        if (currentContainer != null && currentContainer.hasPrevious()) {
            setCurrentContainer(currentContainer.previous());
            setCurrentWidget(currentContainer.content().current().orElseThrow());
            return true;
        }
        return false;
    }

    public boolean handleKeyDown(int keycode) {
        if (currentContainer == null) {
            return false;
        }

        Direction direction = currentContainer.direction();
        CursorList<WithCommand> widgets = currentContainer.content();

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
            setCurrentWidget(currentContainer.content().current().orElseThrow());
            return true;
        }

        return handleShortcuts(keycode);
    }

    private boolean handleShortcuts(int keycode) {
        return streamContainers()
            .flatMap(container -> container.content().stream())
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
            if (container.content().contains(target)) {
                setCurrentContainer(container);
                container.content().setCurrent(target);
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
