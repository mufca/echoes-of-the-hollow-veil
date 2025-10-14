package io.github.mufca.libgdx.datastructure.location.feature;

import lombok.Getter;

@Getter
public enum FeatureType {
    HERB(HerbFeature.class),
    CAMPFIRE(CampfireFeature.class);

    private final Class<? extends LocationFeature> implementation;

    FeatureType(Class<? extends LocationFeature> implementation) {
        this.implementation = implementation;
    }

}
