package io.github.mufca.libgdx.datastructure.location.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.mufca.libgdx.datastructure.inventory.Item;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.datastructure.location.feature.FeatureFactory;
import io.github.mufca.libgdx.datastructure.location.feature.FeatureType;
import io.github.mufca.libgdx.datastructure.location.feature.LocationFeature;
import io.github.mufca.libgdx.datastructure.location.feature.jsondata.FeatureData;
import io.github.mufca.libgdx.datastructure.location.jsondata.LocationData;
import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class BaseLocation {

    private final String targetId;
    private final String shortDescription;
    private final String longDescription;
    private final List<Exit> exits;
    private final List<String> npcDefinitions;
    private final List<Item> objects;

    private final List<LocationFeature> features = new ArrayList<>();

    @JsonCreator
    public BaseLocation(LocationData data, TimeSystem time, MessageRouter router) {
        this.targetId = data.targetId();
        this.shortDescription = data.shortDescription();
        this.longDescription = data.longDescription();
        this.exits = data.exits();
        this.npcDefinitions = data.npcDefinitions();
        this.objects = data.objects();
        for (FeatureData featureData : data.features()) {
            addFeature(FeatureFactory.createFeature(featureData, targetId, time, router));
        }
    }

    public <T extends LocationFeature> void addFeature(T feature) {
        features.add(feature);
    }

    public List<LocationFeature> getFeaturesByType(FeatureType type) {
        return features.stream().filter(feature -> feature.type() == type).
            toList();
    }
}
