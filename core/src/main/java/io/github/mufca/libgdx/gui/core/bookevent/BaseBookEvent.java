package io.github.mufca.libgdx.gui.core.bookevent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;

public abstract class BaseBookEvent implements BookEvent {
    private final CoreTypingLabel label;
    private final Table view;

    protected BaseBookEvent(String label) {
        this.label = new CoreTypingLabel(label);
        this.view = new Table();
        this.view.top().left(); // standard alignment
    }

    @Override
    public CoreTypingLabel getLabel() {
        return label;
    }

    @Override
    public Actor getView() {
        return view;
    }

    protected Table content() {
        return view;
    }
}
