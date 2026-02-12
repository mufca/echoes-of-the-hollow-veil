package io.github.mufca.libgdx.system.weather;

import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import io.github.mufca.libgdx.system.ink.InkBridge;
import io.github.mufca.libgdx.system.time.TimeSystem;
import io.github.mufca.libgdx.system.ui.UITextEvent;

public class DayNightSystem {

    private static final int DAY_CYCLE_LENGTH = 200;
    private static final int SUNRISE = 0;
    private static final int NOON = 50;
    private static final int SUNSET = 100;
    private final InkBridge inkBridge;
    private final TimeSystem time;
    private final EventBus eventBus;

    public DayNightSystem(InkBridge inkBridge, TimeSystem time, EventBus eventBus) {
        this.inkBridge = inkBridge;
        this.time = time;
        this.eventBus = eventBus;
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
            eventBus.publish(new UITextEvent(flavorText));
        }
    }
}
