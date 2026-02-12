package io.github.mufca.libgdx.system.command;

import io.github.mufca.libgdx.scheduler.eventbus.GameEvent;

public record MovementRequestEvent(String direction) implements GameEvent {

}
