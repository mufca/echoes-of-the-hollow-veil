package io.github.mufca.libgdx.datastructure.character.jsondata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PrimaryStatisticsData(
    @JsonProperty("strength") float strength,
    @JsonProperty("dexterity") float dexterity,
    @JsonProperty("constitution") float constitution,
    @JsonProperty("intelligence") float intelligence,
    @JsonProperty("wisdom") float wisdom,
    @JsonProperty("charisma") float charisma
) {

}
