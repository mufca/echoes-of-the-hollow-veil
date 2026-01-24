package io.github.mufca.libgdx.system.sun;

import static io.github.mufca.libgdx.scheduler.event.DeliveryScope.GLOBAL;

import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import io.github.mufca.libgdx.scheduler.event.TextEvent;
import io.github.mufca.libgdx.system.ink.InkBridge;

public class SunSystem {

    private static final int DAY_CYCLE_LENGTH = 200;
    private static final int SUNRISE = 0;
    private static final int NOON = 50;
    private static final int SUNSET = 100;
    private final InkBridge inkBridge;
    private final MessageRouter router;
    private final TimeSystem time;

    public SunSystem(InkBridge inkBridge, MessageRouter router, TimeSystem time) {
        this.inkBridge = inkBridge;
        this.router = router;
        this.time = time;
    }

    public void initialize() {
        time.scheduler()
            .scheduleRepeating("1", 1L, time.now(), 50L,
                () -> handleSunTick(time.now()));
    }

    public void handleSunTick(long currentTick) {
        int cycleTick = (int) (currentTick % DAY_CYCLE_LENGTH);

        // Map ticks onto sun position
        Integer sunPos = switch (cycleTick) {
            case SUNRISE -> 0;
            case NOON -> 1;
            case SUNSET -> 2;
            default -> null;
        };

        if (sunPos != null) {
            // 1. We get a formatted description from Ink through our bridge
            // evaluateFunction("sun_event", sunPos) returns a formatted String
            String flavorText = inkBridge.format("sun_event", sunPos);

            // 2. We publish a narrative event (what the player will see)
            router.publish(new TextEvent(GLOBAL, "ignore", flavorText));
        }
    }
}
