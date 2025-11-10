package io.github.mufca.libgdx.datastructure.character.jsondata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AppearanceData(
    @JsonProperty("firstTrait") List<String> firstTrait,
    @JsonProperty("secondTrait") List<String> secondTrait,
    @JsonProperty("race") String race
) {

}
