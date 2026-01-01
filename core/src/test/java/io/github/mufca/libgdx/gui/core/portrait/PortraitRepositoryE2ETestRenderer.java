package io.github.mufca.libgdx.gui.core.portrait;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;
import lombok.Getter;

public class PortraitRepositoryE2ETestRenderer {

    private static final int PADDING = 2;
    @Getter
    private static float deltaHolder = 0f;

    private PortraitRepositoryE2ETestRenderer() {
    }

    public static void render(SpriteBatch batch, PortraitRepository repository) {
        int x;
        int y = 0;

        repository.processTextureUpload();

        for (var entry : repository.snapshotCharacterToPortraits().entrySet()) {
            int maxY = 0;
            x = 0;
            for (var portraitId : entry.getValue()) {
                var portraitEntry = repository.snapshotPortraits().get(portraitId);
                var region = portraitEntry.region();
                var portraitFile = portraitEntry.portraitFile();
                int width = portraitFile.width();
                int height = portraitFile.height();
                maxY = Math.max(maxY, height);
                batch.draw(region, x, y, width, height);

                x += portraitFile.width() + PADDING;
            }
            y += maxY + PADDING;
        }
    }

    public static void renderUnderStress(DockedViewportPanel panel, SpriteBatch batch, PortraitRepository repository) {
        int cols = 10;
        int rows = 7;

        repository.processTextureUpload();

        float cellWidth = (float) panel.viewport().getScreenWidth() / cols;
        float cellHeight = (float) panel.viewport().getScreenHeight() / rows;

        int index = 0;
        for (PortraitEntry entry : repository.snapshotPortraits().values()) {

            int col = index % cols;
            int row = index / cols;

            float x = col * cellWidth;
            float y = panel.viewport().getScreenHeight() - (row + 1) * cellHeight;

            var region = entry.region();

            float scaleX = cellWidth / region.getRegionWidth();
            float scaleY = cellHeight / region.getRegionHeight();
            float scale = Math.min(scaleX, scaleY);
            float drawWidth = region.getRegionWidth() * scale;
            float drawHeight = region.getRegionHeight() * scale;

            batch.draw(region, x, y, drawWidth, drawHeight);

            index++;
        }
    }

    public static void renderTimeline(DockedViewportPanel panel, SpriteBatch batch, ShapeRenderer sr,
        PortraitRepository repository, PortraitContainer container, float delta) {
        batch.end();

        deltaHolder += delta;
        repository.processTextureUpload();
        container.add(PortraitFile.SMALL);
        container.updateIfNeeded();

        var viewportWidth = panel.viewport().getScreenWidth();
        var y = 200;
        float height = 100f;
        float triWidth = 45f;
        float padding = 10f;

        // final width of the middle rectangle
        float rectWidth = viewportWidth - 2 * triWidth - 2 * padding;

        // X positions
        float xLeft = padding;
        float xRect = xLeft + triWidth;
        float xRightTriStart = xRect + rectWidth;
        sr.setColor(0.25f, 0.25f, 0.25f, 1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        // --- LEFT TRIANGLE (right-angled) ---
        // Points: left bottom, left top, joint center-left
        sr.triangle(
            xLeft, y,                      // bottom-left
            xRect, y + height,             // top-left
            xRect, y
        );

        // --- CENTER RECTANGLE ---
        sr.rect(xRect, y, rectWidth, height);

        // --- RIGHT TRIANGLE ---
        // Points: right bottom, right top, joint center-right
        sr.triangle(
            xRightTriStart, y + height,  // midpoint touching rectangle
            xRightTriStart, y,     // bottom-right
            xRightTriStart + triWidth, y + height  // top-right
        );

        sr.end();
        var portrait = container.get(PortraitFile.SMALL);
        portrait.setRegionWidth(PortraitFile.SMALL.width() / 3);
        portrait.setRegionHeight(PortraitFile.SMALL.height() / 3);
        float duration = 5f;
        float progress = Math.min(deltaHolder / duration, 1f);
        progress = Interpolation.smooth.apply(progress);
        float x = xLeft + (xRightTriStart + triWidth - xLeft - portrait.getRegionWidth()) * progress;
        batch.begin();
        batch.draw(portrait, x, y);
        batch.end();
        sr.begin(ShapeType.Line);
        sr.setColor(Color.WHITE);
        sr.rect(x, y, portrait.getRegionWidth(), portrait.getRegionHeight());
        sr.end();
        batch.begin();
    }
}
