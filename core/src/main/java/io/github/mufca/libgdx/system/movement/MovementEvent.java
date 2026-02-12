package io.github.mufca.libgdx.system.movement;

import io.github.mufca.libgdx.scheduler.eventbus.GameEvent;

public record MovementEvent(String direction, Boolean present) implements GameEvent {

}
