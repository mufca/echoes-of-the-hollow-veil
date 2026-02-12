package io.github.mufca.libgdx.datastructure.command;

import static io.github.mufca.libgdx.util.UIHelper.doNothing;

public class EmptyCommand implements Command {

    public EmptyCommand() {
    }

    @Override
    public void execute() {
        doNothing();
    }

}
