package io.github.mufca.libgdx.datastructure.location.feature.logic;

import static io.github.mufca.libgdx.datastructure.location.feature.FeatureType.FOREST;
import static io.github.mufca.libgdx.datastructure.random.RandomGenerator.pickFromList;
import static io.github.mufca.libgdx.datastructure.random.RandomGenerator.pickFromRange;
import static io.github.mufca.libgdx.scheduler.event.DeliveryScope.CURRENT_LOCATION;

import io.github.mufca.libgdx.datastructure.location.feature.LocationFeature;
import io.github.mufca.libgdx.datastructure.location.feature.constant.ForestEventConstant;
import io.github.mufca.libgdx.datastructure.location.feature.jsondata.ForestEventData;
import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import io.github.mufca.libgdx.scheduler.event.TextEvent;
import io.github.mufca.libgdx.util.LogHelper;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public final class ForestEvent extends LocationFeature {

    @Getter
    private final long heartbeatTicks = 5L * pickFromRange(30, 75);
    private final List<String> messages;

    public ForestEvent(long featureId, String locationId, ForestEventData data,
        TimeSystem time, MessageRouter router) {
        super(FOREST, locationId, featureId, time, router);
        messages = new ArrayList<>(data.events());
        if (data.merge()) {
            messages.addAll(ForestEventConstant.MESSAGES);
        }
        scheduler.scheduleRepeating(locationId, featureId, time.now(), heartbeatTicks, this::sendRandomMessage);
    }

    private void sendRandomMessage() {
        String message = pickFromList(messages);
        router.publish(new TextEvent(CURRENT_LOCATION, locationId, message));
        LogHelper.debug(this, "%s %d event published a message %s.".formatted(locationId, featureId, message));
    }
}
