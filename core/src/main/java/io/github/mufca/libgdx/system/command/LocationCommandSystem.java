package io.github.mufca.libgdx.system.command;

import io.github.mufca.libgdx.datastructure.command.Command;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocationCommandSystem {

    private final EventBus eventBus;
    private final List<Command> commandList = new ArrayList<>();

    public LocationCommandSystem(EventBus eventBus) {
        this.eventBus = eventBus;
//        this.eventBus.subscribe(, this::handleLocationStateChanged);
    }

    private void handleLocationStateChanged(Objects locationStateChanged) {
    }

}
