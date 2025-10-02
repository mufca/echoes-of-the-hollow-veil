package io.github.mufca.libgdx.datastructure.location.feature;

import lombok.Getter;

@Getter
public abstract class LocationFeature {
    private final FeatureType type;

    protected LocationFeature(FeatureType type) {
        this.type = type;
    }

}
