package io.github.mufca.libgdx.datastructure.location.feature;

import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.Scheduler;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import lombok.Getter;

@Getter
public abstract class LocationFeature {

    protected final FeatureType type;
    protected final String locationId;
    protected final long featureId;

    protected final TimeSystem time;
    protected final MessageRouter router;
    protected final Scheduler scheduler;

    protected LocationFeature(FeatureType type, String locationId, long featureId,
        TimeSystem time, MessageRouter router) {
        this.type = type;
        this.locationId = locationId;
        this.featureId = featureId;
        this.time = time;
        this.router = router;
        this.scheduler = time.getScheduler();
    }

    public void onAddedToLocation() {
    }

    public boolean isAlwaysActive() {
        return false;
    }
}
