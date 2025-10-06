package io.github.mufca.libgdx.util;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.mufca.libgdx.gui.core.interfaces.WithCommand;
import io.github.mufca.libgdx.gui.core.traversal.Direction;
import io.github.mufca.libgdx.gui.core.traversal.TraversalContainer;

public class WidgetBuilder {

    private static final int defaultPadding = 5;

    public static Table buildTable(TraversalContainer container) {
        Table toReturn = new Table();

        toReturn.pad(defaultPadding);
        for (WithCommand widget : container.getContent().stream().toList()) {
            if (container.getDirection() == Direction.HORIZONTAL) {
                toReturn.add(widget.asActor()).padRight(2 * defaultPadding);
            } else if (container.getDirection() == Direction.VERTICAL) {
                toReturn.add(widget.asActor()).left().row();
            }
        }
        return toReturn;
    }
}
