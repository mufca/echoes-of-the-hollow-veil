package io.github.mufca.libgdx.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.MapLocation;

import java.util.HashMap;
import java.util.Map;

public class MapRenderer {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Map<String, GridPosition> positions = new HashMap<>();
    private final LazyLocationLoader loader;
    private final int TILE_SIZE = 30;
    private final float BORDER = 1f;
    private final float BORDER_SIZE = (float) TILE_SIZE / 2;
    private final float FILL_SIZE = BORDER_SIZE - 2 * BORDER;
    private final OrthographicCamera camera;

    public MapRenderer(LazyLocationLoader loader) {
        this.loader = loader;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
    }


    public void computePositions(MapLocation start) {
        positions.clear();
        dfs(start, 0, 0);
    }

    private void dfs(MapLocation mapLocation, int x, int y) {
        if (positions.containsKey(mapLocation.targetId())) {
            return;
        }
        positions.put(mapLocation.targetId(), new GridPosition(x, y));

        for (Exit exit : mapLocation.exits()) {
            GridPosition offset = directionToOffset(exit.name());
            if (offset != null) {
                dfs(loader.getMapCache().get(exit.targetId()), x + offset.x, y + offset.y);
            }
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

    public void render(Map<String, MapLocation> world, int gridXToCenterOn, int gridYToCenterOn) {
        camera.position.set(gridXToCenterOn * TILE_SIZE, gridYToCenterOn * TILE_SIZE, 0);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawLocationExitPaths(world);
        drawLocationNodes();
        shapeRenderer.end();
    }

    private void drawLocationExitPaths(Map<String, MapLocation> world) {
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        for (MapLocation location : world.values()) {
            GridPosition startingPosition = positions.get(location.targetId());
            if (startingPosition == null) continue;
            for (Exit exit : location.exits()) {
                GridPosition endingPosition = positions.get(exit.targetId());
                if (endingPosition == null) continue;
                drawLine(startingPosition, endingPosition);
            }
        }
    }

    private void drawLine(GridPosition start, GridPosition end) {
        float startX = start.x * TILE_SIZE;
        float startY = start.y * TILE_SIZE;
        float endX = end.x * TILE_SIZE;
        float endY = end.y * TILE_SIZE;
        shapeRenderer.rectLine(startX, startY, endX, endY,2f);
    }

    private void drawLocationNodes() {
        for (Map.Entry<String, GridPosition> entry : positions.entrySet()) {
            drawLocationNode(entry);
        }
    }

    private void drawLocationNode(Map.Entry<String, GridPosition> entry) {
        Color fillColor = new Color(0.2f, 0.5f, 0.3f, 1f);
        Color borderColor = new Color(0.2f, 0.8f, 0.3f, 1f);
        shapeRenderer.setColor(borderColor); // border
        float bottomLeftCornerX = entry.getValue().x * TILE_SIZE - 7.5f;
        float bottomLeftCornerY = entry.getValue().y * TILE_SIZE - 7.5f;
        shapeRenderer.rect(bottomLeftCornerX, bottomLeftCornerY, BORDER_SIZE, BORDER_SIZE);
        shapeRenderer.setColor(fillColor);
        bottomLeftCornerX += BORDER;
        bottomLeftCornerY += BORDER;
        shapeRenderer.rect(bottomLeftCornerX, bottomLeftCornerY, FILL_SIZE, FILL_SIZE);
    }

    private record GridPosition(int x, int y) {}
}

