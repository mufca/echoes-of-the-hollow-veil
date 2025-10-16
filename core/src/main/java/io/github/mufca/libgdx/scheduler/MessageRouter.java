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
    private final Map<String, Deque<String>> locationBuffers = new HashMap<>();
    private final int maxPerLocation = 50;

    public MessageRouter(EventBus bus, String currentLocationId) {
        this.bus = bus;
        this.currentLocationId = currentLocationId;
    }


    public void publish(TextEvent textEvent) {
        switch (textEvent.scope()) {
            case CURRENT_LOCATION -> {
                if (textEvent.locationId().equals(currentLocationId)) {
                    bus.publish(textEvent);
                }
            }
            case LOCATION -> { // Not used as for now but in the future may be useful
                var deque = locationBuffers.computeIfAbsent(textEvent.locationId(), k -> new ArrayDeque<>());
                if (deque.size() == maxPerLocation) {
                    deque.removeFirst();
                }
                deque.addLast(textEvent.textMessage());

                if (textEvent.locationId().equals(currentLocationId)) {
                    bus.publish(textEvent);
                }
            }
            case GLOBAL, PRIVATE_TO_PLAYER -> bus.publish(textEvent);
        }
    }

    public List<String> drainLocationBuffer(String locationId) {
        var deque = locationBuffers.getOrDefault(locationId, new ArrayDeque<>());
        var out = List.copyOf(deque);
        deque.clear();
        return out;
    }
}
