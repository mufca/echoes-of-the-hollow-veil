package io.github.mufca.libgdx.datastructure.location.feature;

import static io.github.mufca.libgdx.datastructure.location.feature.FeatureType.CAMPFIRE;
import static io.github.mufca.libgdx.scheduler.event.DeliveryScope.CURRENT_LOCATION;

import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.event.TextEvent;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import java.util.Random;

public final class CampfireFeature extends LocationFeature {

    private long scheduledTaskId = -1;

    private int fuel = 0;
    private Phase phase = Phase.UNLIT;

    private static final long HEARTBEAT_TICKS = 5; // co 1s przy 5 TPS
    private static final Random rng = new Random();

    public enum Phase {
        UNLIT, BLAZE, BURNING, EMBERS, OUT
    }

    public CampfireFeature(String locationId, String featureId, TimeSystem time, MessageRouter router) {
        super(CAMPFIRE, locationId, featureId, time, router);
    }

    public void light(int initialFuel) {
        if (phase == Phase.OUT || phase == Phase.UNLIT) {
            fuel = Math.max(0, initialFuel);
            phase = (fuel > 60) ? Phase.BLAZE : Phase.BURNING;
            scheduleHeartbeat();
            send("Rozpalasz ognisko. Płomienie trzaskają.");
        } else {
            send("Ognisko już płonie.");
        }
    }

    public void addFuel(int amount) {
        if (phase == Phase.OUT) {
            send("Nie ma już czego podsycać. Pozostały tylko zimne popioły.");
            return;
        }
        fuel = Math.min(100, fuel + amount);
        if (fuel > 60) phase = Phase.BLAZE;
        if (phase == Phase.UNLIT) {
            light(amount);
        } else {
            send("Dokładasz drewna. Ogień przybiera na sile.");
        }
    }

    private void scheduleHeartbeat() {
        cancel();
        scheduledTaskId = time.getScheduler().scheduleEvery(locationId, featureId,
            time.now(), HEARTBEAT_TICKS, this::heartbeat);
    }

    private void cancel() {
        if (scheduledTaskId != -1) {
            time.getScheduler().cancelByFeature(locationId, featureId);
            scheduledTaskId = -1;
        }
    }

    private void heartbeat() {
        if (phase == Phase.UNLIT || phase == Phase.OUT) return;

        int drain = switch (phase) {
            case BLAZE -> 3;
            case BURNING -> 2;
            case EMBERS -> 1;
            default -> 0;
        };

        fuel = Math.max(0, fuel - drain);

        // zmiana fazy przy braku paliwa
        if (fuel == 0) {
            if (phase == Phase.BLAZE || phase == Phase.BURNING) {
                phase = Phase.EMBERS;
                send("Ogień przygasa, zostają żarzące się węgle.");
            } else if (phase == Phase.EMBERS) {
                phase = Phase.OUT;
                send("Ognisko dogasło.");
                cancel();
            }
        }

        if (fuel > 0 && rng.nextFloat() < 0.25f) {
            switch (phase) {
                case BLAZE -> send("Płomienie huczą, iskry strzelają w górę.");
                case BURNING -> send("Ogień trzaska równym rytmem.");
                case EMBERS -> send("Żar czerwienieje cicho w popiele.");
            }
        }
    }

    private void send(String msg) {
        router.publish(new TextEvent(CURRENT_LOCATION, locationId, msg));
    }
}