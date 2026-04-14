package io.github.mufca.libgdx.system.command;

import io.github.mufca.libgdx.datastructure.command.Command;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import io.github.mufca.libgdx.util.LogHelper;
import java.util.HashMap;
import java.util.Map;

public class CommandSystem {

    private final Map<Integer, Command> commandMap = new HashMap<>();

    public CommandSystem(EventBus eventBus) {
//        eventBus.subscribe(KeyPressedEvent.class, this::handleKeyPressed);
        CommandHelper.registerGlobalCommands(commandMap, eventBus);
    }

    public void handleKeyPressed(KeyPressedEvent keyPressedEvent) {
        LogHelper.debug(this, "Key pressed: " + keyPressedEvent.pressedKey());
        Command command = commandMap.get(keyPressedEvent.pressedKey());
        if (command != null) {
            command.execute();
        }
    }
}
