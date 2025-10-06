package io.github.mufca.libgdx.datastructure.command;

public interface Command {

    void execute();

    void enable(boolean isEnabled);

    default void undo() {
    }

    default boolean isEnabled() {
        return true;
    }
}
