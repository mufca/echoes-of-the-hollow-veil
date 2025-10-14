package io.github.mufca.libgdx.datastructure.location.feature.jsondata;

import io.github.mufca.libgdx.datastructure.location.feature.FeatureType;
import java.util.List;

public record HerbData(FeatureType type, List<String> herbs) implements FeatureData {

}
