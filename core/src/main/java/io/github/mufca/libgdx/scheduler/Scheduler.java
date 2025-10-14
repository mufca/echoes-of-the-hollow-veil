package io.github.mufca.libgdx.scheduler;

import java.util.Objects;
import java.util.PriorityQueue;

public final class Scheduler {

    public static final class Task implements Comparable<Task> {

        final long id;
        final String locationId;
        final long featureId;
        final long interval;
        long nextTick;
        final Runnable action;

        Task(long id, String locationId, long featureId, long firstTick, long interval, Runnable action) {
            this.id = id;
            this.locationId = locationId;
            this.featureId = featureId;
            this.nextTick = firstTick;
            this.interval = interval;
            this.action = action;
        }

        @Override
        public int compareTo(Task o) {
            return Long.compare(this.nextTick, o.nextTick);
        }
    }

    private final PriorityQueue<Task> priorityQueue = new PriorityQueue<>();
    private long seq = 0;

    public long schedule(String targetId, long featureId, long tick, Runnable action) {
        Task t = new Task(++seq, targetId, featureId, tick, 0, action);
        priorityQueue.add(t);
        return t.id;
    }

    public long scheduleRepeating(String loc, long featureId, long now, long period, Runnable action) {
        Task t = new Task(++seq, loc, featureId, now + period, period, action);
        priorityQueue.add(t);
        return t.id;
    }

    public void cancelByFeature(String loc, long featureId) {
        priorityQueue.removeIf(t -> Objects.equals(t.locationId, loc) && Objects.equals(t.featureId, featureId));
    }

    public void cancelByFeature(long featureId) {
        priorityQueue.removeIf(t -> Objects.equals(t.featureId, featureId));
    }

    public void executeDue(long nowTick) {
        while (!priorityQueue.isEmpty() && priorityQueue.peek().nextTick <= nowTick) {
            Task task = priorityQueue.poll();
            task.action.run();
            if (task.interval > 0) {
                task.nextTick += task.interval;
                priorityQueue.add(task);
            }
        }
    }
}
