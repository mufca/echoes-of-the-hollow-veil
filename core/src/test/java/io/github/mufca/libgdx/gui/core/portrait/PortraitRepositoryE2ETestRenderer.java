package io.github.mufca.libgdx.gui.core.portrait;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;

public class PortraitRepositoryE2ETestRenderer {

    private static final int PADDING = 2;

    private PortraitRepositoryE2ETestRenderer() {
    }

    public static void render(SpriteBatch batch, PortraitRepository repository) {
        int x;
        int y = 0;

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
}
