package io.github.mufca.libgdx.scheduler;

import io.github.mufca.libgdx.scheduler.event.EventBus;
import io.github.mufca.libgdx.scheduler.event.TextEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Setter;

public final class MessageRouter {
    private final EventBus bus;
    @Setter
    private String currentLocationId;
    private final Map<String, Deque<String>> locationBuffers = new HashMap();
    private final int maxPerLocation = 50;

    public MessageRouter(EventBus bus, String currentLocationId) {
        this.bus = bus;
        this.currentLocationId = currentLocationId;
    }


    public void publish(TextEvent ev) {
        switch (ev.scope()) {
            case CURRENT_LOCATION -> {
                if (ev.locationId().equals(currentLocationId)) {
                    bus.publish(ev);
                }
            }
            case LOCATION -> {
                var dq = locationBuffers.computeIfAbsent(ev.locationId(), k -> new ArrayDeque<>());
                if (dq.size() == maxPerLocation) dq.removeFirst();
                dq.addLast(ev.textMessage());

                if (ev.locationId().equals(currentLocationId)) {
                    bus.publish(ev);
                }
            }
            case GLOBAL, PRIVATE_TO_PLAYER -> {
                bus.publish(ev);
            }
        }
    }

    public List<String> drainLocationBuffer(String locationId) {
        var dq = locationBuffers.getOrDefault(locationId, new ArrayDeque<>());
        var out = List.copyOf(dq);
        dq.clear();
        return out;
    }
}
