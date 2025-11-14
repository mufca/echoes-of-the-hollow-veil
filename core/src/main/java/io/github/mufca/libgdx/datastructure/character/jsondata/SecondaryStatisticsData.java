package io.github.mufca.libgdx.datastructure.character.jsondata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SecondaryStatisticsData(
    @JsonProperty("hitPoints") float hitPoints,
    @JsonProperty("stamina") float stamina,
    @JsonProperty("mana") float mana
) {

}
