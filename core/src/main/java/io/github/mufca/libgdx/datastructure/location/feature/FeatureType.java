package io.github.mufca.libgdx.datastructure.location.feature;

import io.github.mufca.libgdx.datastructure.location.feature.jsondata.CampfireData;
import io.github.mufca.libgdx.datastructure.location.feature.jsondata.FeatureData;
import io.github.mufca.libgdx.datastructure.location.feature.jsondata.HerbData;
import io.github.mufca.libgdx.datastructure.location.feature.logic.CampfireFeature;
import io.github.mufca.libgdx.datastructure.location.feature.logic.HerbFeature;
import lombok.Getter;

@Getter
public enum FeatureType {

    HERB(HerbFeature.class, HerbData.class),
    CAMPFIRE(CampfireFeature.class, CampfireData.class);

    private final Class<? extends LocationFeature> implementation;
    private final Class<? extends FeatureData> data;

    FeatureType(Class<? extends LocationFeature> implementation, Class<? extends FeatureData> data) {
        this.implementation = implementation;
        this.data = data;
    }
}
