package io.github.mufca.libgdx.system.movement;

import io.github.mufca.libgdx.datastructure.location.CurrentLocationProvider;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import io.github.mufca.libgdx.system.command.MovementRequestEvent;
import io.github.mufca.libgdx.util.LogHelper;
import java.io.IOException;

public final class MovementSystem {

    private static final String FAILED_TO_LOAD_LOCATION = "Failed to load location: %s";
    private final EventBus eventBus;
    private final CurrentLocationProvider locationProvider;

    public MovementSystem(EventBus eventBus, CurrentLocationProvider locationProvider) {
        this.eventBus = eventBus;
        this.locationProvider = locationProvider;
        eventBus.subscribe(MovementRequestEvent.class, this::attemptToMove);
    }

    public void attemptToMove(MovementRequestEvent movementRequestEvent) {
        var direction = movementRequestEvent.direction();
        var currentLocation = locationProvider.currentLocation();
        var optionalExit = currentLocation.exits().stream().
            filter(exit -> exit.name().equals(direction)).findFirst();
        eventBus.publish(new MovementEvent(direction, optionalExit.isPresent()));
        if (optionalExit.isPresent()) {
            Exit exit = optionalExit.get();
            var oldLocationId = currentLocation.targetId();
            var newLocationId = exit.targetId();
            try {
                locationProvider.currentLocation(exit.targetId());
                eventBus.publish(new LocationChangedEvent(oldLocationId, newLocationId));
            } catch (IOException e) {
                LogHelper.error(this, FAILED_TO_LOAD_LOCATION.formatted(newLocationId));
                throw new RuntimeException(e);
            }
        }
    }
}
