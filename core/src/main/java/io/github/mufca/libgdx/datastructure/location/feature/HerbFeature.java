package io.github.mufca.libgdx.datastructure.location.feature;

import lombok.Getter;

import java.util.List;

import static io.github.mufca.libgdx.datastructure.location.feature.FeatureType.*;

public class HerbFeature extends LocationFeature {
    @Getter
    private final List<String> herbs;

    public HerbFeature(List<String> herbs) {
        super(HERB);
        this.herbs = herbs;
    }
}
