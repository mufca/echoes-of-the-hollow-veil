package io.github.mufca.libgdx.datastructure.location.feature;

import io.github.mufca.libgdx.datastructure.location.feature.jsondata.CampfireData;
import io.github.mufca.libgdx.datastructure.location.feature.jsondata.FeatureData;
import io.github.mufca.libgdx.datastructure.location.feature.jsondata.ForestEventData;
import io.github.mufca.libgdx.datastructure.location.feature.jsondata.HerbData;
import io.github.mufca.libgdx.datastructure.location.feature.logic.CampfireFeature;
import io.github.mufca.libgdx.datastructure.location.feature.logic.ForestEvent;
import io.github.mufca.libgdx.datastructure.location.feature.logic.HerbFeature;
import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;

public class FeatureFactory {

    private static final IdProvider idProvider = new IdProvider();

    public static LocationFeature createFeature(FeatureData data, String locationId, TimeSystem time,
        MessageRouter router) {
        long featureId = idProvider.generateFeatureId();
        return switch (data.type()) {
            case HERB -> new HerbFeature(featureId, locationId, (HerbData) data, time, router);
            case CAMPFIRE -> new CampfireFeature(featureId, locationId, (CampfireData) data, time, router);
            case FOREST -> new ForestEvent(featureId, locationId, (ForestEventData) data, time, router);
        };
    }
}
