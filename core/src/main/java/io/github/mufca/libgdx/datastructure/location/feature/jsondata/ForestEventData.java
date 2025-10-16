package io.github.mufca.libgdx.datastructure.location.feature.jsondata;

import io.github.mufca.libgdx.datastructure.location.feature.FeatureType;
import java.util.List;

public record ForestEventData(FeatureType type, List<String> events, boolean merge) implements FeatureData {

}
