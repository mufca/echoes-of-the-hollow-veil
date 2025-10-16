package io.github.mufca.libgdx.datastructure.location.feature.logic;

import static io.github.mufca.libgdx.datastructure.location.feature.FeatureType.CAMPFIRE;
import static io.github.mufca.libgdx.datastructure.location.feature.logic.CampfireFeature.Phase.BLAZE;
import static io.github.mufca.libgdx.datastructure.location.feature.logic.CampfireFeature.Phase.BURNING;
import static io.github.mufca.libgdx.datastructure.location.feature.logic.CampfireFeature.Phase.EMBERS;
import static io.github.mufca.libgdx.datastructure.location.feature.logic.CampfireFeature.Phase.OUT;
import static io.github.mufca.libgdx.datastructure.location.feature.logic.CampfireFeature.Phase.UNLIT;
import static io.github.mufca.libgdx.scheduler.event.DeliveryScope.CURRENT_LOCATION;

import io.github.mufca.libgdx.datastructure.location.feature.LocationFeature;
import io.github.mufca.libgdx.datastructure.location.feature.jsondata.CampfireData;
import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import io.github.mufca.libgdx.scheduler.event.TextEvent;
import java.util.Random;

public final class CampfireFeature extends LocationFeature {

    private long scheduledTaskId = -1;

    private int fuel;
    private Phase phase = UNLIT;

    private static final long HEARTBEAT_TICKS = 5;
    private static final Random random = new Random();

    public enum Phase {
        UNLIT, BLAZE, BURNING, EMBERS, OUT
    }

    public CampfireFeature(long featureId, String locationId, CampfireData data,
        TimeSystem time, MessageRouter router) {
        super(CAMPFIRE, locationId, featureId, time, router);
        fuel = data.fuel();
    }

    public void light(int initialFuel) {
        if (phase == OUT || phase == UNLIT) {
            fuel = Math.max(0, initialFuel);
            phase = (fuel > 60) ? BLAZE : BURNING;
            scheduleHeartbeat();
            send("You light the campfire. The flames crackle.");
        } else {
            send("The campfire is already burning.");
        }
    }

    public void addFuel(int amount) {
        if (phase == OUT) {
            send("There's nothing left to fuel the campfire. Only cold ashes remain.");
            return;
        }
        fuel = Math.min(100, fuel + amount);
        if (fuel > 60) {
            phase = BLAZE;
        }
        if (phase == UNLIT) {
            light(amount);
        } else {
            send("You add wood. The fire grows stronger.");
        }
    }

    private void scheduleHeartbeat() {
        cancel();
        scheduledTaskId = time.getScheduler().scheduleRepeating(locationId, featureId,
            time.now(), HEARTBEAT_TICKS, this::heartbeat);
    }

    private void cancel() {
        if (scheduledTaskId != -1) {
            time.getScheduler().cancelByFeature(locationId, featureId);
            scheduledTaskId = -1;
        }
    }

    private void heartbeat() {
        if (phase == UNLIT
            || phase == OUT) {
            return;
        }

        int drain = switch (phase) {
            case BLAZE -> 3;
            case BURNING -> 2;
            case EMBERS -> 1;
            default -> 0;
        };

        fuel = Math.max(0, fuel - drain);

        if (fuel == 0) {
            if (phase == BLAZE
                || phase == BURNING) {
                phase = EMBERS;
                send("The fire dies down, leaving only glowing embers.");
            } else if (phase == EMBERS) {
                phase = OUT;
                send("The campfire went out");
                cancel();
            }
        }

        if (fuel > 0 && random.nextFloat() < 0.25f) {
            switch (phase) {
                case BLAZE -> send("The flames roar, sparks shoot upwards.");
                case BURNING -> send("The fire crackles with a steady rhythm.");
                case EMBERS -> send("The embers redden quietly in the ashes.");
            }
        }
    }

    private void send(String msg) {
        router.publish(new TextEvent(CURRENT_LOCATION, locationId, msg));
    }
}