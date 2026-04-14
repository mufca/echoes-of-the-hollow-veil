package io.github.mufca.libgdx.system.movement;

import static io.github.mufca.libgdx.datastructure.character.logic.components.StatTag.STAMINA;

import io.github.mufca.libgdx.datastructure.location.CurrentLocationProvider;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.datastructure.player.Player;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import io.github.mufca.libgdx.system.command.MovementRequestEvent;
import io.github.mufca.libgdx.system.ui.MessageBuffer;
import io.github.mufca.libgdx.util.LogHelper;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class MovementSystem {

    private static final float STAMINA_COST_FOR_TRAVELING = 5f;
    private static final String FAILED_TO_LOAD_LOCATION = "Failed to load location: %s";
    private static final String TOO_EXHAUSTED_TO_TRAVEL = "You are too exhausted to travel to %s.";
    private final EventBus eventBus;
    private final CurrentLocationProvider locationProvider;
    private final Supplier<Float> playerStaminaGetter;
    private final Consumer<Float> playerStaminaSetter;
    private final MessageBuffer messageBuffer;

    public MovementSystem(EventBus eventBus, CurrentLocationProvider locationProvider, Player player, MessageBuffer messageBuffer) {
        this.eventBus = eventBus;
        this.locationProvider = locationProvider;
        this.playerStaminaGetter = () -> player.characterStats().byTag(STAMINA);
        this.playerStaminaSetter = (stamina) -> player.characterStats().byTag(STAMINA, stamina);
        this.messageBuffer = messageBuffer;
    }

    public void attemptToMove(MovementRequestEvent movementRequestEvent) {
        var direction = movementRequestEvent.direction();
        var currentLocation = locationProvider.currentLocation();
        var optionalExit = currentLocation.exits().stream().
            filter(exit -> exit.name().equals(direction)).findFirst();

        eventBus.post(new MovementEvent(direction, optionalExit.isPresent()));
        if (optionalExit.isPresent() && playerStaminaGetter.get() >= STAMINA_COST_FOR_TRAVELING) {
            var staminaAfterTraveling = playerStaminaGetter.get() - STAMINA_COST_FOR_TRAVELING;
            playerStaminaSetter.accept(staminaAfterTraveling);
            Exit exit = optionalExit.get();
            var oldLocationId = currentLocation.targetId();
            var newLocationId = exit.targetId();
            try {
                locationProvider.currentLocation(exit.targetId());
                eventBus.post(new LocationChangedEvent(oldLocationId, newLocationId));
            } catch (IOException e) {
                LogHelper.error(this, FAILED_TO_LOAD_LOCATION.formatted(newLocationId));
                throw new RuntimeException(e);
            }
        } else if (playerStaminaGetter.get() < STAMINA_COST_FOR_TRAVELING) {
            messageBuffer.add(TOO_EXHAUSTED_TO_TRAVEL.formatted(movementRequestEvent.direction()));
        }
    }
}
