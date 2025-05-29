package io.github.mufca.libgdx.gui.core.interfaces;

import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.mufca.libgdx.datastructure.command.Command;
import io.github.mufca.libgdx.datastructure.shortcut.ShortcutBinding;
import io.github.mufca.libgdx.gui.core.icon.KeycapIcon;

public interface WithShortcut extends WithCommand {
    Actor getActor();
    KeycapIcon getIcon();                 // icon to display
    ShortcutBinding getShortcutBinding(); // shortcut command
    boolean isActive();

    @Override
    default Command getCommand() {
        return getShortcutBinding().command();
    }
}
