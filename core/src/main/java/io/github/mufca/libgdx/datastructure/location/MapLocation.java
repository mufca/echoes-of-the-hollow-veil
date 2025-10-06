package io.github.mufca.libgdx.datastructure.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MapLocation(String targetId, String shortDescription, List<Exit> exits) {

    @JsonCreator
    public MapLocation(
        @JsonProperty("id") String targetId,
        @JsonProperty("shortDescription") String shortDescription,
        @JsonProperty("exits") List<Exit> exits
    ) {
        this.targetId = targetId;
        this.shortDescription = shortDescription;
        this.exits = exits;
    }
}
