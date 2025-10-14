package io.github.mufca.libgdx.datastructure.location.feature.jsondata;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.mufca.libgdx.datastructure.location.feature.FeatureType;

public record CampfireData(
    @JsonProperty("type") FeatureType type,
    @JsonProperty("fuel") int fuel) implements FeatureData {

}
