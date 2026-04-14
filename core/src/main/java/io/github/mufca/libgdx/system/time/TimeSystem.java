package io.github.mufca.libgdx.system.time;

import static io.github.mufca.libgdx.system.time.Ticks.SECOND;

import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import io.github.mufca.libgdx.scheduler.Scheduler;

public final class TimeSystem {

    private final float STEP = 1f / SECOND.duration();

    private final Scheduler scheduler;
    private double accumulator = 0;
    private long worldTick = 0;

    public TimeSystem(IdProvider idProvider) {
        this.scheduler = new Scheduler(idProvider);
    }

    public void schedule(Object tag, long delayInTicks, Runnable action) {
        scheduler.add(tag, worldTick + delayInTicks, 0, action);
    }

    public void scheduleRepeating(Object tag, long periodInTicks, Runnable action) {
        scheduler.add(tag, worldTick + periodInTicks, periodInTicks, action);
    }

    public void cancel(Object tag) {
        scheduler.cancel(tag);
    }

    public void update(float delta) {
        accumulator += delta;
        while (accumulator >= STEP) {
            accumulator -= STEP;
            worldTick++;
            scheduler.update(worldTick);
        }
    }

    public long now() {
        return worldTick;
    }

    public boolean isActive(Object tag) {
        return scheduler.isActive(tag);
    }
}