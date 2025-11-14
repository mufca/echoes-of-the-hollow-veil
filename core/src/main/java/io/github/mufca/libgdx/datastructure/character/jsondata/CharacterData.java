package io.github.mufca.libgdx.datastructure.character.jsondata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CharacterData(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("type") String type,
    @JsonProperty("enumReferenceName") String enumReferenceName,
    @JsonProperty("respawningTicks") int respawningTicks,
    @JsonProperty("aggressive") boolean aggressive,
    @JsonProperty("statsRandomAdjustment") int statsRandomAdjustment,
    @JsonProperty("appearance") AppearanceData appearance,
    @JsonProperty("primaryStatistics") PrimaryStatisticsData primaryStatistics,
    @JsonProperty("secondaryStatistics") SecondaryStatisticsData secondaryStatistics
) {

}
