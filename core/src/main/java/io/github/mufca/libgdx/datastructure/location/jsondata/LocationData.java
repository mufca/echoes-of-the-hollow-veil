package io.github.mufca.libgdx.datastructure.location.jsondata;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.mufca.libgdx.datastructure.inventory.Item;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.datastructure.location.feature.jsondata.FeatureData;
import java.util.List;


public record LocationData(
    @JsonProperty("id") String targetId,
    @JsonProperty("shortDescription") String shortDescription,
    @JsonProperty("longDescription") String longDescription,
    @JsonProperty("exits") List<Exit> exits,
    @JsonProperty("npcDefinitions") List<String> npcDefinitions,
    @JsonProperty("objects") List<Item> objects,
    @JsonProperty("features") List<FeatureData> features) {

}
