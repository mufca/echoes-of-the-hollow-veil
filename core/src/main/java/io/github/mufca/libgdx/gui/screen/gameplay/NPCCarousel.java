package io.github.mufca.libgdx.gui.screen.gameplay;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.mufca.libgdx.gui.core.portrait.PortraitContainer;
import io.github.mufca.libgdx.gui.core.portrait.PortraitFile;
import io.github.mufca.libgdx.gui.core.portrait.PortraitRepository;
import io.github.mufca.libgdx.gui.core.widget.DockedViewportPanel;
import io.github.mufca.libgdx.system.npc.NPCSystem;
import java.util.List;

public class NPCCarousel extends DockedViewportPanel {

    private final NPCSystem npcSystem;
    private final PortraitRepository portraitRepository;
    private final Group portraits = new Group();
    private int npcListHash = 0;


    public NPCCarousel(NPCSystem npcSystem, PortraitRepository portraitRepository) {
        super();
        this.npcSystem = npcSystem;
        this.portraitRepository = portraitRepository;
        buildLayout();
    }

    private void buildLayout() {
        stage.addActor(portraits);
    }

    public void render(float delta) {
        if (!npcSystem.areThereNPCsHere()) {
            return;
        }
        var npcList = npcSystem.getNPCs();
        if (npcList.hashCode() != npcListHash || portraits.getChildren().size != npcList.size()) {
            rebuildPortraits(npcList);
        }
        this.camera.setToOrtho(false, viewport.getScreenWidth(), viewport.getScreenHeight());
        stage.act(delta);
        stage.draw();
    }

    private void rebuildPortraits(List<Long> npcList) {
        npcListHash = npcList.hashCode();
        portraits.clearChildren();
        var currentX = 0f;
        var spacing = 5f;
        for (Long id : npcList) {
            var npc = npcSystem.getNPC(id);
            var container = new PortraitContainer(npc.base().characterId(), portraitRepository);
            container.add(PortraitFile.SMALL);
            var image = new Image(container.get(PortraitFile.SMALL));
            var ratio = 0.652173913f;
            var width = PortraitFile.SMALL.width()*ratio;
            var height = PortraitFile.SMALL.height()*ratio;
            image.setSize(width, height);
            image.setPosition(currentX, 0);
            portraits.addActor(image);
            currentX += width + spacing;
        }
    }
}
