package io.github.mufca.libgdx.scheduler;

import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public final class Scheduler {

    public record Task(long id, Object tag, long interval, long nextTick, Runnable action) implements Comparable<Task> {

        @Override
        public int compareTo(Task o) {
            return Long.compare(this.nextTick, o.nextTick);
        }

        Task next() {
            return new Task(id, tag, interval, nextTick + interval, action);
        }
    }

    private final PriorityQueue<Task> queue = new PriorityQueue<>();
    private final Map<Object, Long> activeTaskIds = new HashMap<>(); // Tag -> Task ID
    private final IdProvider idProvider;

    public Scheduler(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public long add(Object tag, long nextTick, long interval, Runnable action) {
        if (activeTaskIds.containsKey(tag)) {
            return activeTaskIds.get(tag);
        }

        long id = idProvider.generateUniqueId();
        Task t = new Task(id, tag, interval, nextTick, action);

        queue.add(t);
        activeTaskIds.put(tag, id);
        return id;
    }

    public void update(long currentTick) {
        while (!queue.isEmpty() && queue.peek().nextTick <= currentTick) {
            Task task = queue.poll();

            Long currentActiveId = activeTaskIds.get(task.tag());
            if (currentActiveId == null || currentActiveId != task.id()) {
                continue;
            }

            task.action().run();

            if (task.interval() > 0) {
                Task next = task.next();
                queue.add(next);
            } else {
                activeTaskIds.remove(task.tag());
            }
        }
    }

    public void cancel(Object tag) {
        activeTaskIds.remove(tag);
    }

    public boolean isActive(Object tag) {
        return activeTaskIds.containsKey(tag);
    }
}