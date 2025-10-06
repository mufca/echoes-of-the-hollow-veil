package io.github.mufca.libgdx.datastructure.location.feature;

import static io.github.mufca.libgdx.datastructure.location.feature.FeatureType.HERB;

import java.util.List;
import lombok.Getter;

public class HerbFeature extends LocationFeature {

    @Getter
    private final List<String> herbs;

    public HerbFeature(List<String> herbs) {
        super(HERB);
        this.herbs = herbs;
    }
}
