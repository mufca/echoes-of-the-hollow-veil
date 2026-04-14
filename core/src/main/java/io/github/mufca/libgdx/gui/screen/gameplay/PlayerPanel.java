package io.github.mufca.libgdx.gui.screen.gameplay;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static io.github.mufca.libgdx.datastructure.character.logic.components.StatTag.CHARISMA;
import static io.github.mufca.libgdx.gui.core.portrait.PortraitFile.SMALL;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.mufca.libgdx.datastructure.character.logic.components.StatTag;
import io.github.mufca.libgdx.datastructure.player.Player;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;
import io.github.mufca.libgdx.system.GameEngine;
import io.github.mufca.libgdx.system.time.TimeSystem;
import io.github.mufca.libgdx.util.LogHelper;
import io.github.mufca.libgdx.util.UIHelper;

public final class PlayerPanel extends DockedViewportPanel {

    private static final String RACE_PATTERN = "%s %s %s";
    private static final String SETTING_STAT = "Setting %s to %s";
    private final CoreTypingLabel[] statValueLabels = new CoreTypingLabel[StatTag.values().length];
    private final CoreTypingLabel[] statNameLabels = new CoreTypingLabel[StatTag.values().length];
    private final Table root;
    private final Player player;
    private final TimeSystem timeSystem;
    private final StatRegistry stats;
    private Image portrait;
    private CoreTypingLabel name;
    private CoreTypingLabel race;

    public PlayerPanel(Player player, GameEngine engine) {
        super();
        this.player = player;
        this.timeSystem = engine.timeSystem();
        this.stats = new StatRegistry(player.characterStats(), engine.inkFunctions());
        this.root = new Table().top().left();
        buildLayout();
    }

    private void buildLayout() {
        root.setFillParent(true);
        stage.addActor(root);

        portrait = new Image(player.portraits().get(SMALL));
        portrait.setSize(SMALL.width(), SMALL.height());
        root.add(new Image(UIHelper.getFilledColor(BLACK))).size(300, 1).center().row();
        root.add(portrait).pad(10).center().row();

        name = new CoreTypingLabel(player.baseCharacter().name());
        String raceText = RACE_PATTERN.formatted(
            player.appearanceTraits().firstTrait(),
            player.appearanceTraits().secondTrait(),
            player.appearanceTraits().race()
        );
        race = new CoreTypingLabel(raceText);
        root.add(name.skipToTheEnd()).padTop(5).center().row();
        root.add(race.skipToTheEnd()).padBottom(10).center().row();

        Table statsTable = new Table();
        statsTable.defaults().pad(2).left();
        statsTable.add(new CoreTypingLabel("=== STATISTICS ===").skipToTheEnd()).colspan(2).row();
        for (StatTag tag : StatTag.values()) {
            var tagIndex = tag.ordinal();
            Table rowTable = new Table();
            statNameLabels[tagIndex] = new CoreTypingLabel(tag.label() +": ");
            statValueLabels[tagIndex] = new CoreTypingLabel("null");
            rowTable.add(statNameLabels[tagIndex].skipToTheEnd()).left();
            rowTable.add(statValueLabels[tagIndex].skipToTheEnd()).left();
            statsTable.add(rowTable).left().row();
            if (tag == CHARISMA) {
                statsTable.add(new CoreTypingLabel("=== CONDITION ===").skipToTheEnd()).row();
            }
        }

        root.add(statsTable).padTop(10).padLeft(10).left();

    }

    public void render(float delta) {
        this.camera.setToOrtho(false, viewport.getScreenWidth(), viewport.getScreenHeight());
        stage.act(delta);
        this.act(delta);
        stage.draw();
    }

    public void act(float delta) {
        stats.update();
        if (stats.anyChanged()) {
            updateStatLabels();
        }
        if (player.portraits().updateIfNeeded()) {
            ((TextureRegionDrawable) portrait.getDrawable()).setRegion(player.portraits().get(SMALL));
            portrait.invalidateHierarchy();
        }
    }

    private void updateStatLabels() {
        for (StatTag tag : StatTag.values()) {
            var tagIndex = tag.ordinal();
            if (!statValueLabels[tagIndex].getIntermediateText().toString().equals(stats.coloredLevels()[tagIndex])) {
                LogHelper.debug(this, SETTING_STAT.formatted(statValueLabels[tagIndex].getIntermediateText().toString(), stats.coloredLevels()[tagIndex], stats.coloredLevels()[tagIndex]));
                statValueLabels[tagIndex].setText(stats.coloredLevels()[tagIndex]);
            }
        }
    }

    public void dispose() {
        timeSystem.cancel(this);
        stage.dispose();
    }
}