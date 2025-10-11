package io.github.mufca.libgdx.gui.screen.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.MapLocation;
import io.github.mufca.libgdx.datastructure.map.GridPosition;
import io.github.mufca.libgdx.datastructure.map.MapLayout;

import java.util.Map;
import lombok.Getter;

public class MapRenderer {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Viewport viewport;
    @Getter
    private final MapLayout mapLayout;
    private static final int TILE_SIZE = 30;
    @Getter
    private final OrthographicCamera camera;
    private MapLocation currentLocation;

    public MapRenderer(LazyLocationLoader loader, Viewport viewport) {
        this.viewport = viewport;
        this.camera = (OrthographicCamera) viewport.getCamera();
        this.mapLayout = new MapLayout(loader, TILE_SIZE);
        this.camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    public void computePositions(MapLocation start) {
        mapLayout.computePositions(start);
        currentLocation = start;
    }

    public void render(Map<String, MapLocation> world, MapLocation currentLocation) {
        this.camera.setToOrtho(false, viewport.getScreenWidth(), viewport.getScreenHeight());
        this.currentLocation = currentLocation;
        GridPosition currentLocationPosition = mapLayout.getPosition(currentLocation.targetId());
        int gridXToCenterOn = currentLocationPosition.x();
        int gridYToCenterOn = currentLocationPosition.y();
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
        Map<String, GridPosition> positions = mapLayout.getPositions();
        for (MapLocation location : world.values()) {
            GridPosition startingPosition = positions.get(location.targetId());
            if (startingPosition == null) continue;
            for (var exit : location.exits()) {
                GridPosition endingPosition = positions.get(exit.targetId());
                if (endingPosition == null) continue;
                drawLine(startingPosition, endingPosition);
            }
        }
    }

    private void drawLine(GridPosition start, GridPosition end) {
        float startX = start.x() * TILE_SIZE;
        float startY = start.y() * TILE_SIZE;
        float endX = end.x() * TILE_SIZE;
        float endY = end.y() * TILE_SIZE;
        shapeRenderer.rectLine(startX, startY, endX, endY, 2f);
    }

    private void drawLocationNodes() {
        for (var entry : mapLayout.getPositions().entrySet()) {
            drawLocationNode(entry);
        }
    }

    private void drawLocationNode(Map.Entry<String, GridPosition> entry) {
        Color fillColor = new Color(0.2f, 0.5f, 0.3f, 1f);
        Color borderColor = new Color(0.2f, 0.8f, 0.3f, 1f);
        if (entry.getKey().equals(currentLocation.targetId())) {
            borderColor = new Color(1f, 0.7f, 0.7f, 1f);
        }
        shapeRenderer.setColor(borderColor); // border
        float bottomLeftCornerX = entry.getValue().x() * TILE_SIZE - 7.5f;
        float bottomLeftCornerY = entry.getValue().y() * TILE_SIZE - 7.5f;
        shapeRenderer.rect(bottomLeftCornerX, bottomLeftCornerY, mapLayout.getBorderSize(), mapLayout.getBorderSize());
        shapeRenderer.setColor(fillColor);
        bottomLeftCornerX += mapLayout.getBorder();
        bottomLeftCornerY += mapLayout.getBorder();
        shapeRenderer.rect(bottomLeftCornerX, bottomLeftCornerY, mapLayout.getFillSize(), mapLayout.getFillSize());
    }
}

