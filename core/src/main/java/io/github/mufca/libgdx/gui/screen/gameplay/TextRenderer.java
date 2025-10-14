package io.github.mufca.libgdx.gui.screen.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.datastructure.location.feature.LocationFeature;
import io.github.mufca.libgdx.datastructure.location.feature.logic.HerbFeature;
import io.github.mufca.libgdx.datastructure.location.logic.BaseLocation;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;
import java.util.LinkedList;
import java.util.List;

public class TextRenderer extends DockedViewportPanel {

    private static final int MAX_HISTORY = 1000;
    public static final int MINIMAP_BORDER_OFFSET = 1;
    private Table contentTable, root;
    private ScrollPane scrollPane;
    private final LinkedList<CoreTypingLabel> content = new LinkedList<>();

    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public void show() {
        contentTable = new Table();
        scrollPane = new ScrollPane(contentTable);
        root = new Table();
        root.setFillParent(true);
        root.add(scrollPane).expand().fill();
        stage.addActor(root);
    }

    public void moveToLocation(BaseLocation location) {
        addText("Moved to location: " + location.getShortDescription());
        addText(location.getLongDescription());
        List<LocationFeature> features = location.getFeatures();
        if (!features.isEmpty()) {
            for (LocationFeature feature : features) {
                switch (feature.getType()) {
                    case HERB:
                        addText("You see herbs: " + String.join(", ", ((HerbFeature) feature).getHerbs()));
                        break;
                    case CAMPFIRE:
                        addText("You see a campfire.");
                        break;
                }
            }

        }
        addText(getExits(location));
    }

    private void addText(String text) {
        CoreTypingLabel label = new CoreTypingLabel(text);
        label.setWrap(true);
        label.skipToTheEnd();

        content.add(label);
        contentTable.add(label).left().expandX().fillX().row();
        scrollPane.scrollTo(0, 0, 0, 0);
        if (content.size() > MAX_HISTORY) {
            CoreTypingLabel oldest = content.removeFirst();
            oldest.remove();
        }
    }

    private String getExits(BaseLocation location) {
        String exits = String.join(", ", location.getExits().stream().map(Exit::name).toList());
        return "Exits: %s".formatted(exits);
    }

    public void render(ScreenViewport minimapVieport) {
        camera.setToOrtho(false, viewport.getScreenWidth(), viewport.getScreenHeight());
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(
            minimapVieport.getScreenX() - viewport.getScreenX() - MINIMAP_BORDER_OFFSET,
            minimapVieport.getScreenY() - MINIMAP_BORDER_OFFSET,
            minimapVieport.getScreenWidth() + 2 * MINIMAP_BORDER_OFFSET,
            minimapVieport.getScreenHeight() + 2 * MINIMAP_BORDER_OFFSET
        );
        shapeRenderer.end();
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void draw() {
        stage.act();
        super.draw();
    }
}
