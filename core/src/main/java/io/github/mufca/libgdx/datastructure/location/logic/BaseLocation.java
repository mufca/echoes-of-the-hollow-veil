package io.github.mufca.libgdx.datastructure.location.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.mufca.libgdx.datastructure.inventory.Item;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.datastructure.location.jsondata.LocationData;
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

    @JsonCreator
    public BaseLocation(LocationData data) {
        this.targetId = data.targetId();
        this.shortDescription = data.shortDescription();
        this.longDescription = data.longDescription();
        this.exits = data.exits();
        this.npcDefinitions = data.npcDefinitions();
        this.objects = data.objects();
    }
}
