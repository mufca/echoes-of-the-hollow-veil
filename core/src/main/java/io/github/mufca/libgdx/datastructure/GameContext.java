package io.github.mufca.libgdx.datastructure;

import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.logic.BaseLocation;
import io.github.mufca.libgdx.datastructure.player.Player;
import io.github.mufca.libgdx.datastructure.story.StoryContext;
import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import io.github.mufca.libgdx.scheduler.event.EventBus;
import java.io.IOException;
import lombok.Getter;

@Getter
public final class GameContext {

    private final Player player;
    private final StoryContext story;
    private final TimeSystem time;
    private final EventBus eventBus;
    private final MessageRouter router;
    private final LazyLocationLoader loader;
    private final BaseLocation currentLocation;

    public GameContext(Player player) throws IOException {
        this.player = player;
        this.story = new StoryContext();
        time = new TimeSystem();
        eventBus = new EventBus();
        router = new MessageRouter(eventBus, "forest_glade_0001");
        loader = new LazyLocationLoader(time, router);
        currentLocation = loader.getLocation("forest_glade_0001");
    }
}
