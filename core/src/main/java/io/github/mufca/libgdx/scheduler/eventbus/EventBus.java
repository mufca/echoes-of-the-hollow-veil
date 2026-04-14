package io.github.mufca.libgdx.scheduler.eventbus;

import io.github.mufca.libgdx.util.LogHelper;
import java.util.HashMap;
import java.util.Map;

public final class EventBus {
    private final Map<Class<? extends GameEvent>, GameEvent> activeEvents = new HashMap<>();

    public <E extends GameEvent> void post(E event) {
        activeEvents.put(event.getClass(), event);
    }

    @SuppressWarnings("unchecked")
    public <E extends GameEvent> E getEvent(Class<E> type) {
        return (E) activeEvents.get(type);
    }

    public boolean hasEvent(Class<? extends GameEvent> type) {
        return activeEvents.containsKey(type);
    }

    public void flush() {
        LogHelper.debug(this, "Flushing %d events...".formatted(activeEvents.size()));
        activeEvents.clear();
    }
}