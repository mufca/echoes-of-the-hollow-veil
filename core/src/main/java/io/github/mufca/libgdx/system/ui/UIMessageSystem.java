package io.github.mufca.libgdx.system.ui;

import io.github.mufca.libgdx.datastructure.location.CurrentLocationProvider;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import io.github.mufca.libgdx.system.ink.InkBridge;
import io.github.mufca.libgdx.system.movement.LocationChangedEvent;
import io.github.mufca.libgdx.system.movement.MovementEvent;
import java.util.stream.Collectors;

public class UIMessageSystem {

    private final EventBus eventBus;
    private final InkBridge inkBridge;
    private final CurrentLocationProvider currentLocationProvider;

    public UIMessageSystem(EventBus eventBus, InkBridge inkBridge, CurrentLocationProvider currentLocationProvider) {
        this.eventBus = eventBus;
        this.inkBridge = inkBridge;
        this.currentLocationProvider = currentLocationProvider;
        this.eventBus.subscribe(MovementEvent.class, this::handleMovementEvent);
        this.eventBus.subscribe(LocationChangedEvent.class, this::handleLocationChangedEvent);
    }

    private void handleLocationChangedEvent(LocationChangedEvent locationChangedEvent) {
        var currentLocation = currentLocationProvider.currentLocation();
        var exits = currentLocation.exits().stream().map(Exit::name).collect(Collectors.joining(", "));
        eventBus.publish(new UITextEvent(currentLocation.shortDescription()));
        eventBus.publish(new UITextEvent(currentLocation.longDescription()));
        eventBus.publish(new UITextEvent("Exits: " + exits));
    }

    private void handleMovementEvent(MovementEvent movementEvent) {
        UITextEvent movementMessage = new UITextEvent(
            inkBridge.format("movementMessage", movementEvent.direction(), movementEvent.present()));
        eventBus.publish(movementMessage);
    }
}
