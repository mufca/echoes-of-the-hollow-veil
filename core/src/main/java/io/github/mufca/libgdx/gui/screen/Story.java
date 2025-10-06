package io.github.mufca.libgdx.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.mufca.libgdx.gui.Paragraph;
import io.github.mufca.libgdx.gui.core.widget.CoreScreen;
import io.github.mufca.libgdx.temporarytrash.ParagraphConstants;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class Story extends CoreScreen {

    private Table innerTable;
    private ScrollPane scrollPane;

    @Override
    public void show() {
        initializeStageAndInputs();
        Table outerTable = new Table();
        innerTable = new Table();
        scrollPane = new ScrollPane(innerTable);
        outerTable.setFillParent(true);
        outerTable.add(scrollPane).expand().fill();
        stage.addActor(outerTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Paragraph paragraph = new Paragraph(ParagraphConstants.getRandomParagraph());
            innerTable.add(paragraph).expandX().fillX().row();
            paragraph.addComponents();
            removeExcessCell();
            scrollPane.scrollTo(0, 0, 0, 0);
        }
    }

    private void removeExcessCell() {
        int max_limit = 5;
        if (innerTable.getRows() > max_limit) {

            innerTable.getChild(0).remove();
        }

    }


    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
