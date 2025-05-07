package io.github.mufca.libgdx.gui.screen.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.mufca.libgdx.gui.core.widget.CoreScreen;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;

import static com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
import static com.badlogic.gdx.utils.Scaling.none;
import static io.github.mufca.libgdx.util.UIHelper.doNothing;
import static io.github.mufca.libgdx.util.UIHelper.getFilledColor;
import static io.github.mufca.libgdx.util.UIHelper.getTopLeftPaddings;
import static io.github.mufca.libgdx.constant.TextureConstants.BACKGROUND_MAIN_MENU;
import static io.github.mufca.libgdx.constant.TextureConstants.LEAF;

public class MainMenu extends CoreScreen {
    private static final float outerPad = 30f;
    private static final float innerPad = 15f;
    private static final Table bottomLayer = new Table();
    private static final Table menu = new Table();
    private static final Table paddingContainer = new Table();
    private static CoreTypingLabel newGame;
    private static CoreTypingLabel loadGame;
    private static CoreTypingLabel settings;
    private static CoreTypingLabel licences;
    private static CoreTypingLabel exit;
    private static final float topPadding = 0.10f;
    private static final float leftPadding = 0.18f;
    private static final float minimumSpawnCooldownInSeconds = 0.5f;
    private static final float maximumSpawnCooldownInSeconds = 2.5f;
    private static WindManager wind = new WindManager();
    private Image background;
    private TextureRegion leaf;
    private TextureRegionDrawable menuCanvas;
    private Group leafLayer = new Group();
    private float elapsedTime = 0f;
    private float nextSpawn = getNextSpawn();

    @Override
    public void show() {
        initializeStageAndInputs();
        prepareImages();
        stageBackground();
        stageFallingLeaves();
        stageMainMenu();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new MenuNavigator(paddingContainer, newGame, loadGame, settings, licences, exit));
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }


    @Override
    public void render(float delta) {
        defaultStageRender(delta);
        elapsedTime += delta;
        wind.elapsed(delta);
        if (leafLayer.getChildren().size < 25 & elapsedTime > nextSpawn) {
            spawnLeaf();
            elapsedTime = 0f;
            nextSpawn = getNextSpawn();
        }
        if (wind.isTimeToStart()) {
            for (Actor actor : leafLayer.getChildren()) {
                switch (actor) {
                    case LeafActor leaf -> leaf.blowWind(wind.getDuration());
                    default -> doNothing();
                }
            }
            wind = new WindManager();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        setUpPaddingContainer();
    }

    @Override
    public void hide() {

    }

    private void prepareImages() {
        BACKGROUND_MAIN_MENU.setFilter(Linear, Linear);
        LEAF.setFilter(Linear, Linear);
        this.background = new Image(BACKGROUND_MAIN_MENU);
        this.leaf = new TextureRegion(LEAF);

        this.background.setScaling(none);
        this.menu.setBackground(getFilledColor(Color.BLACK));
    }

    private void stageBackground() {
        bottomLayer.setFillParent(true);
        bottomLayer.center();
        bottomLayer.add(background);
        stage.addActor(bottomLayer);
    }

    private void stageFallingLeaves() {
        stage.addActor(leafLayer);
    }

    private void stageMainMenu() {
        newGame = new CoreTypingLabel("New game");
        loadGame = new CoreTypingLabel("Load game");
        settings = new CoreTypingLabel("Settings");
        licences = new CoreTypingLabel("Licences");
        exit = new CoreTypingLabel("Exit");
        menu.pad(outerPad).defaults().pad(innerPad);
        menu.add(newGame).row();
        menu.add(loadGame).row();
        menu.add(settings).row();
        menu.add(licences).row();
        menu.add(exit).row();

        paddingContainer.setFillParent(true);
        setUpPaddingContainer();
        paddingContainer.add(menu).top();

        stage.addActor(paddingContainer);
    }

    private void setUpPaddingContainer() {
        var paddings = getTopLeftPaddings(stage, topPadding, leftPadding);
        paddingContainer.top().left().pad(paddings.first(),paddings.second(),paddings.first(),paddings.second());
    }

    private void spawnLeaf() {
        LeafActor newLeaf = new LeafActor(leaf, stage.getWidth(), stage.getHeight());
        leafLayer.addActor(newLeaf);
    }

    private float getNextSpawn() {
        return MathUtils.random(minimumSpawnCooldownInSeconds, maximumSpawnCooldownInSeconds);
    }

}
