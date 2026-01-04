package io.github.mufca.libgdx.util;

import io.github.mufca.libgdx.gui.core.interfaces.WithCommand;
import io.github.mufca.libgdx.gui.core.traversal.TraversalContainer;
import io.github.mufca.libgdx.gui.core.widget.PixelLayout;

public class WidgetBuilder {

    private static final int defaultPadding = 5;

    public static PixelLayout buildLayout(TraversalContainer container) {

        var layout = new PixelLayout(container.direction(), defaultPadding);

        for (WithCommand widget : container.content().stream().toList()) {
            layout.addActor(widget.asActor());
        }

        return layout;
    }
}