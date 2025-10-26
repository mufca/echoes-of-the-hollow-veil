package io.github.mufca.libgdx.scheduler.event;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class EventBus {

    private final Map<Class<? extends GameEvent>, List<EventListener<?>>> listeners = new HashMap<>();

    public <E extends GameEvent> void subscribe(Class<E> type, EventListener<E> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    public <E extends GameEvent> void publish(E event) {
        var list = listeners.get(event.getClass());
        if (list != null) {
            for (EventListener<?> listener : list) {
                ((EventListener<E>) listener).on(event);
            }
        }
    }
}
