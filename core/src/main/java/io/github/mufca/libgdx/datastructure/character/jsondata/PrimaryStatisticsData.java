package io.github.mufca.libgdx.datastructure.character.jsondata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PrimaryStatisticsData(
    @JsonProperty("strength") double strength,
    @JsonProperty("dexterity") double dexterity,
    @JsonProperty("constitution") double constitution,
    @JsonProperty("intelligence") double intelligence,
    @JsonProperty("wisdom") double wisdom,
    @JsonProperty("charisma") double charisma
) {

}
