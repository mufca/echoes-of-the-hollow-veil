package io.github.mufca.libgdx.scheduler;

import lombok.Getter;

public final class TimeSystem {

    public static final int TICKS_PER_SECOND = 5;
    private static final float STEP = 1f / TICKS_PER_SECOND;
    @Getter
    private final Scheduler scheduler = new Scheduler();

    private double accumulator = 0;
    private long worldTick = 0;

    public void update(float delta) {
        accumulator += delta;
        while (accumulator >= STEP) {
            accumulator -= STEP;
            tickOnce();
        }
    }

    private void tickOnce() {
        worldTick++;
        scheduler.executeDue(worldTick);
    }

    public long now() {
        return worldTick;
    }

    public void fastForward(long ticks) {
        for (long counter = 0; counter < ticks; counter++) {
            tickOnce();
        }
    }
}
