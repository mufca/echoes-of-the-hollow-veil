package io.github.mufca.libgdx.system.movement;

import io.github.mufca.libgdx.scheduler.eventbus.GameEvent;

public record LocationChangedEvent(String fromLocationId, String toLocationId) implements GameEvent {

}
