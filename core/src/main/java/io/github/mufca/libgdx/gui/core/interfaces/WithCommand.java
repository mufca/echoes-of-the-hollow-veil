package io.github.mufca.libgdx.gui.core.interfaces;

import io.github.mufca.libgdx.datastructure.command.Command;

public interface WithCommand extends Focusable {

    Command getCommand();
}
