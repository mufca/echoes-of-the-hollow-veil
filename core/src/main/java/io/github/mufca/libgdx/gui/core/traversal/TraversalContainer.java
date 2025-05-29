package io.github.mufca.libgdx.gui.core.traversal;

import io.github.mufca.libgdx.datastructure.lowlevel.CursorList;
import io.github.mufca.libgdx.gui.core.interfaces.WithCommand;
import io.github.mufca.libgdx.gui.core.interfaces.WithShortcut;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TraversalContainer {
    private final CursorList<WithCommand> content;
    private final Direction direction;
    @Setter
    private TraversalContainer next, previous;

    public TraversalContainer(Direction direction) {
        next = null;
        previous = null;
        content = new CursorList<>();
        this.direction = direction;
    }

    public void add(WithShortcut widget) {
        content.add(widget);
    }

    public boolean hasNext() {
        return next != null;
    }

    public boolean hasPrevious() {
        return previous != null;
    }
}
