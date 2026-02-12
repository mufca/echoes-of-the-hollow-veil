package io.github.mufca.libgdx.gui.screen.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;
import java.util.LinkedList;

public class TextRenderer extends DockedViewportPanel {

    private static final int MAX_HISTORY = 1000;
    public static final int MINIMAP_BORDER_OFFSET = 1;
    private final LinkedList<CoreTypingLabel> content = new LinkedList<>();
    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Table contentTable, root;
    private ScrollPane scrollPane;

    public void show() {
        contentTable = new Table();
        scrollPane = new ScrollPane(contentTable);
        root = new Table();
        root.setFillParent(true);
        root.add(scrollPane).expand().fill();
        stage.addActor(root);
    }

    public void addText(String text) {
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

    public void render(ScreenViewport minimapViewport) {
        camera.setToOrtho(false, viewport.getScreenWidth(), viewport.getScreenHeight());
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(
            minimapViewport.getScreenX() - viewport.getScreenX() - MINIMAP_BORDER_OFFSET,
            minimapViewport.getScreenY() - MINIMAP_BORDER_OFFSET,
            minimapViewport.getScreenWidth() + 2 * MINIMAP_BORDER_OFFSET,
            minimapViewport.getScreenHeight() + 2 * MINIMAP_BORDER_OFFSET
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
