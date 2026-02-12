package io.github.mufca.libgdx.scheduler.eventbus;

import io.github.mufca.libgdx.util.LogHelper;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class EventBus {

    public static final String EVENT_LISTENER_THREW_AN_EXCEPTION = "Event listener threw an exception %s: %s";
    private final Map<Class<? extends GameEvent>, List<EventListener<?>>> listeners = new HashMap<>();

    public <E extends GameEvent> void subscribe(Class<E> type, EventListener<E> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    public <E extends GameEvent> void publish(E event) {
        var list = listeners.get(event.getClass());
        if (list != null) {
            for (EventListener<?> listener : list) {
                try {
                    ((EventListener<E>) listener).on(event);
                } catch (Exception e) {
                    LogHelper.error(this, EVENT_LISTENER_THREW_AN_EXCEPTION
                        .formatted(event.getClass().getSimpleName(), e.getMessage()));
                }
            }
        }
    }
}
