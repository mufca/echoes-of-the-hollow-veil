package io.github.mufca.libgdx.datastructure.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.mufca.libgdx.datastructure.inventory.Item;
import io.github.mufca.libgdx.datastructure.location.feature.FeatureType;
import io.github.mufca.libgdx.datastructure.location.feature.LocationFeature;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class BaseLocation {
    private final String id;
    private final String shortDescription;
    private final String longDescription;
    private final List<Exit> exits;
    private final List<Character> characters;
    private final List<Item> objects;

    private final Map<FeatureType, LocationFeature> features = new HashMap<>();

    @JsonCreator
    public BaseLocation(
        @JsonProperty("id") String id,
        @JsonProperty("shortDescription") String shortDescription,
        @JsonProperty("longDescription") String longDescription,
        @JsonProperty("exits") List<Exit> exits,
        @JsonProperty("characters") List<Character> characters,
        @JsonProperty("objects") List<Item> objects
    ) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.exits = exits;
        this.characters = characters;
        this.objects = objects;
    }

    public <T extends LocationFeature> void addFeature(T feature) {
        features.put(feature.getType(), feature);
    }

    public LocationFeature getFeature(FeatureType type) {
        return features.get(type);
    }
}
