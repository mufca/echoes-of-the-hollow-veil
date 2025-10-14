package io.github.mufca.libgdx.scheduler.event;

public record TextEvent(DeliveryScope scope, String locationId, String textMessage) implements GameEvent {
}
