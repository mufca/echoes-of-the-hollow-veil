package io.github.mufca.libgdx.gui.core.interfaces.implementations;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.mufca.libgdx.datastructure.command.Command;
import io.github.mufca.libgdx.datastructure.shortcut.ShortcutBinding;
import io.github.mufca.libgdx.gui.core.icon.KeycapIcon;
import io.github.mufca.libgdx.gui.core.interfaces.WithShortcut;
import lombok.Setter;

public class ActorWithShortcut<T extends Actor> extends Table implements WithShortcut {

    private final T actor;
    private final KeycapIcon icon;
    private final ShortcutBinding binding;
    @Setter
    private boolean isActive;
    private boolean isFocused;

    public ActorWithShortcut(T actor, KeycapIcon icon, Command command) {
        this.actor = actor;
        this.icon = icon;
        this.binding = new ShortcutBinding(icon.getKey().keycode(), command);

        add(icon).padRight(5).center();
        add(actor).left();
    }

    @Override
    public T getActor() {
        return actor;
    }

    @Override
    public KeycapIcon getIcon() {
        return icon;
    }

    @Override
    public ShortcutBinding getShortcutBinding() {
        return binding;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void focus() {
        isFocused = true;
    }

    @Override
    public void forget() {
        isFocused = false;
    }

    @Override
    public boolean isFocused() {
        return isFocused;
    }
}
