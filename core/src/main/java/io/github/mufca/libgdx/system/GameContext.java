package io.github.mufca.libgdx.system;

import io.github.mufca.libgdx.datastructure.location.CurrentLocationProvider;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.logic.BaseLocation;
import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import io.github.mufca.libgdx.datastructure.player.Player;
import io.github.mufca.libgdx.gui.core.portrait.PortraitRepository;
import java.io.IOException;
import lombok.Getter;

@Getter
public final class GameContext implements CurrentLocationProvider {

    private final IdProvider idProvider = new IdProvider();
    private final LazyLocationLoader loader;
    private final PortraitRepository portraitRepository;
    private final Player player;
    private BaseLocation currentLocation;


    public GameContext(String startingLocationId) throws IOException {
        loader = new LazyLocationLoader();
        currentLocation = loader.getLocation(startingLocationId);
        portraitRepository = new PortraitRepository(idProvider);
        player = new Player(idProvider, portraitRepository);
    }

    public void processTextureUpload() {
        portraitRepository.processTextureUpload();
    }

    public void currentLocation(String locationId) throws IOException {
        currentLocation = loader.getLocation(locationId);
    }

}
