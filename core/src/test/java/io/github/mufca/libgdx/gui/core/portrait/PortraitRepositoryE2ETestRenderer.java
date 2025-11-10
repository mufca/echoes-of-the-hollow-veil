package io.github.mufca.libgdx.gui.core.portrait;

import static com.badlogic.gdx.graphics.Color.PINK;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;
import io.github.mufca.libgdx.util.UIHelper;

public class PortraitRepositoryE2ETestRenderer {

    private static final int PADDING = 2;
    private static final SpriteBatch batch = new SpriteBatch();

    private PortraitRepositoryE2ETestRenderer() {
    }

    public static void render(DockedViewportPanel panel, PortraitRepository repository) {
        panel.apply(batch);

        batch.begin();
        int x = 0;
        int y = 0;

        for (var entry : repository.snapshotCharacterToPortraits().entrySet()) {
            int maxY = 0;
            x = 0;
            for (var portraitId : entry.getValue()) {
                var portraitEntry = repository.snapshotPortraits().get(portraitId);
                if (portraitEntry == null) {
                    continue;
                }

                var region = portraitEntry.region();
                var portraitFile = portraitEntry.portraitFile();
                int width = portraitFile.width();
                int height = portraitFile.height();
                if (region == null) {
                    region = UIHelper.getFilledColor(PINK).getRegion();
                    region.setRegionWidth(width);
                    region.setRegionHeight(height);
                }
                maxY = Math.max(maxY, height);
                batch.draw(region, x, y, width, height);

                x += portraitFile.width() + PADDING;
            }
            y += maxY + PADDING;
        }

        batch.end();
    }

    public static void renderUnderStress(DockedViewportPanel panel, PortraitRepository repository) {
        batch.begin();
        panel.apply(batch);
        int cols = 10;
        int rows = 7;
        int totalSlots = cols * rows;

        float cellWidth = (float) panel.viewport().getScreenWidth() / cols;
        float cellHeight = (float) panel.viewport().getScreenHeight() / rows;

        int index = 0;
        for (PortraitEntry entry : repository.snapshotPortraits().values()) {
            if (index >= totalSlots) {
                break;
            }

            int col = index % cols;
            int row = index / cols;

            float x = col * cellWidth;
            float y = panel.viewport().getScreenHeight() - (row + 1) * cellHeight;

            var region = entry.region();
            if (region != null) {
                float scaleX = cellWidth / region.getRegionWidth();
                float scaleY = cellHeight / region.getRegionHeight();
                float scale = Math.min(scaleX, scaleY);
                float drawWidth = region.getRegionWidth() * scale;
                float drawHeight = region.getRegionHeight() * scale;

                batch.draw(region, x, y, drawWidth, drawHeight);
            }
            index++;
        }
        batch.end();
    }
}
