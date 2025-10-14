package io.github.mufca.libgdx.datastructure.location.feature;

import static io.github.mufca.libgdx.datastructure.location.feature.FeatureType.HERB;

import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import java.util.List;
import lombok.Getter;

public class HerbFeature extends LocationFeature {

    @Getter
    private final List<String> herbs;

    public HerbFeature(TimeSystem time, MessageRouter router, String locationId, String featureId, List<String> herbs) {
        super(HERB, locationId, featureId, time, router);
        this.herbs = herbs;
    }
}
