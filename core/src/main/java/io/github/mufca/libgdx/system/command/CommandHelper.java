package io.github.mufca.libgdx.system.command;

import com.badlogic.gdx.Input;
import io.github.mufca.libgdx.datastructure.command.Command;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import java.util.Map;

public class CommandHelper {

    public static void registerGlobalCommands(Map<Integer, Command> commandMap, EventBus eventBus) {
        registerMovement(commandMap, eventBus, Input.Keys.NUMPAD_8, "north");
        registerMovement(commandMap, eventBus, Input.Keys.NUMPAD_9, "north-east");
        registerMovement(commandMap, eventBus, Input.Keys.NUMPAD_6, "east");
        registerMovement(commandMap, eventBus, Input.Keys.NUMPAD_3, "south-east");
        registerMovement(commandMap, eventBus, Input.Keys.NUMPAD_2, "south");
        registerMovement(commandMap, eventBus, Input.Keys.NUMPAD_1, "south-west");
        registerMovement(commandMap, eventBus, Input.Keys.NUMPAD_4, "west");
        registerMovement(commandMap, eventBus, Input.Keys.NUMPAD_7, "north-west");
    }

    private static void registerMovement(Map<Integer, Command> commandMap, EventBus eventBus, int key,
        String direction) {
        commandMap.put(key, () -> eventBus.publish(new MovementRequestEvent(direction)));
    }
}
