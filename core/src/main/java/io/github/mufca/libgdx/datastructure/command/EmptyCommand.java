package io.github.mufca.libgdx.datastructure.command;

import lombok.Getter;

import static io.github.mufca.libgdx.util.UIHelper.doNothing;

@Getter
public class EmptyCommand implements Command {

    boolean enabled = false;

    public EmptyCommand() {
    }


    @Override
    public void execute() {
        doNothing();
    }

    @Override
    public void enable(boolean isEnabled) {
        enabled = isEnabled;
    }

}
