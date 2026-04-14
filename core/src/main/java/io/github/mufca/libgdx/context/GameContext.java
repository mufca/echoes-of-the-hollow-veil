package io.github.mufca.libgdx.context;

import io.github.mufca.libgdx.context.npc.NPCRepository;
import io.github.mufca.libgdx.datastructure.location.CurrentLocationProvider;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.logic.BaseLocation;
import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import io.github.mufca.libgdx.datastructure.player.Player;
import io.github.mufca.libgdx.gui.core.portrait.PortraitRepository;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import java.io.IOException;
import lombok.Getter;

@Getter
public final class GameContext implements CurrentLocationProvider {

    private final IdProvider idProvider = new IdProvider();
    private final LazyLocationLoader loader;
    private final PortraitRepository portraitRepository;
    private final Player player;
    private final NPCRepository npcRepository;
    private BaseLocation currentLocation;


    public GameContext(String startingLocationId, EventBus eventBus) throws IOException {
        loader = new LazyLocationLoader();
        currentLocation = loader.getLocation(startingLocationId);
        portraitRepository = new PortraitRepository(idProvider);
        npcRepository = new NPCRepository(idProvider, eventBus, portraitRepository, this::currentLocation);
        player = new Player(idProvider, eventBus, portraitRepository);
    }

    public void processTextureUpload() {
        portraitRepository.processTextureUpload();
    }

    public void currentLocation(String locationId) throws IOException {
        currentLocation = loader.getLocation(locationId);
    }

}
