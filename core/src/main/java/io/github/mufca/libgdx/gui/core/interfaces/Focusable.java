package io.github.mufca.libgdx.gui.core.interfaces;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface Focusable {
    void focus();
    void forget();
    boolean isFocused();
    default Actor asActor() {
        return (Actor)this;
    }
}
