package io.github.mufca.libgdx.datastructure.map;

import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.MapLocation;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public class MapLayout {
    private final Map<String, GridPosition> positions = new HashMap<>();
    private final LazyLocationLoader loader;
    @Getter
    private final int tileSize;
    @Getter
    private final float border;
    @Getter
    private final float borderSize;
    @Getter
    private final float fillSize;

    public MapLayout(LazyLocationLoader loader, int tileSize) {
        this.loader = loader;
        this.tileSize = tileSize;
        this.border = 1f;
        this.borderSize = (float) tileSize / 2;
        this.fillSize = borderSize - 2 * border;
    }

    public void computePositions(MapLocation start) {
        positions.clear();
        buildMap(start, 0, 0);
    }

    // This is simple deep first search implementation
    private void buildMap(MapLocation mapLocation, int x, int y) {
        if (positions.containsKey(mapLocation.targetId())) {
            return;
        }
        positions.put(mapLocation.targetId(), new GridPosition(x, y));

        for (Exit exit : mapLocation.exits()) {
            MapLocation exitLocation = loader.getMapCache().get(exit.targetId());
            GridPosition offset = directionToOffset(exit.name());
            if (exitLocation == null) continue;
            if (offset == null) continue;
            buildMap(exitLocation, x + offset.x(), y + offset.y());
        }
    }

    private GridPosition directionToOffset(String direction) {
        return switch (direction.toLowerCase()) {
            case "north" -> new GridPosition(0, 1);
            case "south" -> new GridPosition(0, -1);
            case "east" -> new GridPosition(1, 0);
            case "west" -> new GridPosition(-1, 0);
            case "north-east" -> new GridPosition(1, 1);
            case "north-west" -> new GridPosition(-1, 1);
            case "south-east" -> new GridPosition(1, -1);
            case "south-west" -> new GridPosition(-1, -1);
            default -> null;
        };
    }

    public GridPosition getPosition(String targetId) {
        return positions.get(targetId);
    }

    public Map<String, GridPosition> getPositions() {
        return new HashMap<>(positions);
    }

}
