package io.github.mufca.libgdx.gui.screen.map;

import static io.github.mufca.libgdx.shaders.ShaderType.MINIMAP_RING;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.jsondata.MapLocation;
import io.github.mufca.libgdx.datastructure.map.GridPosition;
import io.github.mufca.libgdx.datastructure.map.MapLayout;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;
import io.github.mufca.libgdx.shaders.ShaderFactory;
import io.github.mufca.libgdx.shaders.ShaderHandler;
import java.util.Map;
import lombok.Getter;

public class MapRenderer extends DockedViewportPanel {

    private static final int TILE_SIZE = 30;
    private static final float TILE_SIZE_OFFSET = 7.5f;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final ShaderHandler ringShader;
    @Getter
    private final MapLayout mapLayout;
    private Mesh ringMesh;
    private float time;
    private MapLocation currentLocation;

    public MapRenderer(LazyLocationLoader loader) {
        super();
        ringShader = new ShaderHandler(ShaderFactory.create(MINIMAP_RING));
        this.mapLayout = new MapLayout(loader, TILE_SIZE);
    }

    public void computePositions(MapLocation start) {
        mapLayout.computePositions(start);
        currentLocation = start;
    }

    public void render(Map<String, MapLocation> world, MapLocation currentLocation, float delta) {
        this.camera.setToOrtho(false, viewport.getScreenWidth(), viewport.getScreenHeight());
        this.currentLocation = currentLocation;
        GridPosition currentLocationPosition = mapLayout.getPosition(currentLocation.targetId());
        int gridXToCenterOn = currentLocationPosition.x();
        int gridYToCenterOn = currentLocationPosition.y();
        camera.position.set(gridXToCenterOn * TILE_SIZE, gridYToCenterOn * TILE_SIZE, 0);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawBackground();
        drawLocationExitPaths(world);
        drawLocationNodes();
        shapeRenderer.end();
        drawRotatingRing(delta);
    }

    private void drawBackground() {
        shapeRenderer.setColor(new Color(0f, 0f, 0f, 1f)); // pełne czarne tło
        shapeRenderer.rect(
            camera.position.x - viewport.getScreenWidth() / 2f,
            camera.position.y - viewport.getScreenHeight() / 2f,
            viewport.getScreenWidth(),
            viewport.getScreenHeight()
        );
    }

    private void drawLocationExitPaths(Map<String, MapLocation> world) {
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        Map<String, GridPosition> positions = mapLayout.getPositions();
        for (MapLocation location : world.values()) {
            GridPosition startingPosition = positions.get(location.targetId());
            if (startingPosition == null) {
                continue;
            }
            for (var exit : location.exits()) {
                GridPosition endingPosition = positions.get(exit.targetId());
                if (endingPosition == null) {
                    continue;
                }
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
        shapeRenderer.setColor(borderColor); // border
        float bottomLeftCornerX = entry.getValue().x() * TILE_SIZE - TILE_SIZE_OFFSET;
        float bottomLeftCornerY = entry.getValue().y() * TILE_SIZE - TILE_SIZE_OFFSET;
        shapeRenderer.rect(bottomLeftCornerX, bottomLeftCornerY, mapLayout.borderSize(), mapLayout.borderSize());
        shapeRenderer.setColor(fillColor);
        bottomLeftCornerX += mapLayout.border();
        bottomLeftCornerY += mapLayout.border();
        shapeRenderer.rect(bottomLeftCornerX, bottomLeftCornerY, mapLayout.fillSize(), mapLayout.fillSize());
    }

    private void drawRotatingRing(float delta) {
        initializeRingMesh();

        time += delta;
        var shader = ringShader.shader();

        if (shader == null) {
            return;
        }
        shader.bind();

        float width = viewport.getScreenWidth();
        float height = viewport.getScreenHeight();

        shader.setUniformf("u_time", time);
        shader.setUniformf("u_resolution", width, height);
        shader.setUniformf("u_center", width / 2f, height / 2f);
        shader.setUniformf("u_radius", 18f);

        Gdx.gl.glEnable(GL32.GL_BLEND);
        Gdx.gl.glBlendFunc(GL32.GL_SRC_ALPHA, GL32.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL32.GL_DEPTH_TEST);

        ringMesh.render(shader, GL32.GL_TRIANGLES);

        Gdx.gl.glDisable(GL32.GL_BLEND);
    }

    private void initializeRingMesh() {
        if (ringMesh != null) {
            return;
        }

        ringMesh = new Mesh(true, 4, 6,
            new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"));
        ringMesh.setVertices(new float[]{
            -1, -1,
            1, -1,
            1, 1,
            -1, 1
        });
        ringMesh.setIndices(new short[]{0, 1, 2, 2, 3, 0});
    }

}

