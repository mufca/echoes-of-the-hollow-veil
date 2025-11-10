package io.github.mufca.libgdx.datastructure.character.jsondata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SecondaryStatisticsData(
    @JsonProperty("hitPoints") double hitPoints,
    @JsonProperty("stamina") double stamina,
    @JsonProperty("mana") double mana
) {

}
