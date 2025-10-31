package io.github.mufca.libgdx.datastructure;

import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.logic.BaseLocation;
import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import io.github.mufca.libgdx.datastructure.player.Player;
import io.github.mufca.libgdx.datastructure.story.StoryContext;
import io.github.mufca.libgdx.gui.core.portrait.PortraitRepository;
import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import io.github.mufca.libgdx.scheduler.event.EventBus;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;

@Getter
public final class GameContext {

    private final IdProvider idProvider = new IdProvider();
    private final StoryContext story = new StoryContext();
    private final TimeSystem time = new TimeSystem();
    private final EventBus eventBus = new EventBus();
    private final MessageRouter router;
    private final LazyLocationLoader loader;
    private final PortraitRepository portraitRepository;
    private Player player;
    @Setter
    private BaseLocation currentLocation;


    public GameContext(String startingLocationId) throws IOException {
        router = new MessageRouter(eventBus, startingLocationId);
        loader = new LazyLocationLoader(time, router);
        currentLocation = loader.getLocation(startingLocationId);
        portraitRepository = new PortraitRepository(idProvider);
    }

    public void createPlayer() {
        player = new Player(this);
    }
}
