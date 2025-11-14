package io.github.mufca.libgdx.gui.screen.gameplay;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static io.github.mufca.libgdx.gui.core.portrait.PortraitFile.SMALL;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.mufca.libgdx.datastructure.GameContext;
import io.github.mufca.libgdx.datastructure.character.logic.components.PrimaryStatistics;
import io.github.mufca.libgdx.datastructure.character.logic.components.SecondaryStatistics;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;
import io.github.mufca.libgdx.util.UIHelper;

public final class PlayerPanel extends DockedViewportPanel {

    private static final String RACE_PATTERN = "%s %s %s";
    private final Table root;
    private final GameContext context;
    private Image portrait;

    public PlayerPanel(GameContext context) {
        super();
        this.context = context;
        this.root = new Table().top().left();
        root.setFillParent(true);
        stage.addActor(root);
        buildLayout();
    }

    private void buildLayout() {
        var player = context.player();

        portrait = new Image(player.portraits().get(SMALL));
        root.add(new Image(UIHelper.getFilledColor(BLACK))).size(300, 1).center().row();
        root.add(portrait).pad(10).center().row();

        CoreTypingLabel nameLabel = new CoreTypingLabel(player.baseCharacter().name());
        String raceText = RACE_PATTERN.formatted(
            player.appearanceTraits().firstTrait(),
            player.appearanceTraits().secondTrait(),
            player.appearanceTraits().race()
        );
        CoreTypingLabel raceLabel = new CoreTypingLabel(raceText);
        root.add(nameLabel.skipToTheEnd()).padTop(5).center().row();
        root.add(raceLabel.skipToTheEnd()).padBottom(10).center().row();

        PrimaryStatistics p = player.primaryStatistics();
        SecondaryStatistics s = player.secondaryStatistics();

        Table statsTable = new Table();
        statsTable.defaults().pad(2).left();
        statsTable.add(new CoreTypingLabel("Strength: " + p.strength()).skipToTheEnd()).row();
        statsTable.add(new CoreTypingLabel("Dexterity: " + p.dexterity()).skipToTheEnd()).row();
        statsTable.add(new CoreTypingLabel("Constitution: " + p.constitution()).skipToTheEnd()).row();
        statsTable.add(new CoreTypingLabel("Intelligence: " + p.intelligence()).skipToTheEnd()).row();
        statsTable.add(new CoreTypingLabel("Wisdom: " + p.wisdom()).skipToTheEnd()).row();
        statsTable.add(new CoreTypingLabel("Charisma: " + p.charisma()).skipToTheEnd()).padBottom(20).row();

        statsTable.add(new CoreTypingLabel("HP: " + s.hitPoints()).skipToTheEnd()).row();
        statsTable.add(new CoreTypingLabel("Stamina: " + s.stamina()).skipToTheEnd()).row();
        statsTable.add(new CoreTypingLabel("Mana: " + s.mana()).skipToTheEnd()).row();

        root.add(statsTable).padTop(10).padLeft(10).left();
    }

    public void render(float delta) {
        this.camera.setToOrtho(false, viewport.getScreenWidth(), viewport.getScreenHeight());
        stage.act(delta);
        this.act(delta);
        stage.draw();
    }

    public void act(float delta) {
        context.player().portraits().updateIfNeeded();
        portrait.setDrawable(new TextureRegionDrawable(context.player().portraits().get(SMALL)));
    }


    public void dispose() {
        stage.dispose();
    }
}