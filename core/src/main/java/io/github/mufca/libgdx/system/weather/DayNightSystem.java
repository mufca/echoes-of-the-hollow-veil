package io.github.mufca.libgdx.system.weather;

import io.github.mufca.libgdx.system.ink.InkFunctions;
import io.github.mufca.libgdx.system.time.TimeSystem;
import io.github.mufca.libgdx.system.ui.MessageBuffer;

public class DayNightSystem {

    private static final int DAY_CYCLE_LENGTH = 200;
    private static final int SUNRISE = 0;
    private static final int NOON = 50;
    private static final int SUNSET = 100;
    private final InkFunctions inkFunctions;
    private final TimeSystem time;
    private final MessageBuffer messageBuffer;

    public DayNightSystem(InkFunctions inkFunctions, TimeSystem time, MessageBuffer messageBuffer) {
        this.inkFunctions = inkFunctions;
        this.time = time;
        this.messageBuffer = messageBuffer;
    }

    public void initialize() {
        time.scheduleRepeating("SUN_POSITION", 1L, () -> handleSunTick(time.now()));
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
            messageBuffer.add(inkFunctions.getSunDescription(sunPos));
        }
    }
}
