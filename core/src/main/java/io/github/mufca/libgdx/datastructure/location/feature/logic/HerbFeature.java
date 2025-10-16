package io.github.mufca.libgdx.datastructure.location.feature.logic;

import static io.github.mufca.libgdx.datastructure.location.feature.FeatureType.HERB;

import io.github.mufca.libgdx.datastructure.location.feature.LocationFeature;
import io.github.mufca.libgdx.datastructure.location.feature.jsondata.HerbData;
import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import java.util.List;
import lombok.Getter;

public final class HerbFeature extends LocationFeature {

    @Getter
    private final List<String> herbs;

    public HerbFeature(long featureId, String locationId, HerbData herbData,
        TimeSystem time, MessageRouter router) {
        super(HERB, locationId, featureId, time, router);
        this.herbs = herbData.herbs();
    }
}
