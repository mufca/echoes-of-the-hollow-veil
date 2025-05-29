package io.github.mufca.libgdx.datastructure.shortcut;

import io.github.mufca.libgdx.datastructure.command.Command;

public record ShortcutBinding (int keycode, Command command) {

    public boolean matches(int keycode) {
        return this.keycode == keycode;
    }
}
