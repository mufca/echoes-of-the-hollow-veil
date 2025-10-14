package io.github.mufca.libgdx.datastructure.location.feature;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import lombok.Getter;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CampfireFeature.class, name = "CAMPFIRE"),
    @JsonSubTypes.Type(value = HerbFeature.class, name = "HERB"),
    // dodaj inne
})
public abstract class LocationFeature {

    protected final FeatureType type;
    protected final String locationId;
    protected final String featureId;

    protected final TimeSystem time;
    protected final MessageRouter router;

    protected LocationFeature(FeatureType type, String locationId, String featureId,
        TimeSystem time, MessageRouter router) {
        this.type = type;
        this.locationId = locationId;
        this.featureId = featureId;
        this.time = time;
        this.router = router;
    }

    public void onAddedToLocation() {
    }

    public boolean isAlwaysActive() {
        return false; //override if needed
    }
}
