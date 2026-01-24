package io.github.mufca.libgdx.system.gamecontext;

import io.github.mufca.libgdx.datastructure.character.storage.NPCSystem;
import io.github.mufca.libgdx.datastructure.location.LazyLocationLoader;
import io.github.mufca.libgdx.datastructure.location.logic.BaseLocation;
import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import io.github.mufca.libgdx.datastructure.player.Player;
import io.github.mufca.libgdx.gui.core.portrait.PortraitRepository;
import io.github.mufca.libgdx.scheduler.MessageRouter;
import io.github.mufca.libgdx.scheduler.TimeSystem;
import io.github.mufca.libgdx.system.ink.InkBridge;
import io.github.mufca.libgdx.system.sun.SunSystem;
import io.github.mufca.libgdx.scheduler.event.EventBus;
import io.github.mufca.libgdx.util.LogHelper;
import io.github.mufca.libgdx.util.NvidiaSmiMonitor;
import io.github.mufca.libgdx.util.NvidiaSmiMonitor.GpuStatus;
import java.io.IOException;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
public final class GameContext {

    private final IdProvider idProvider = new IdProvider();
    private final TimeSystem time = new TimeSystem();
    private final EventBus eventBus = new EventBus();
    private final MessageRouter router;
    private final LazyLocationLoader loader;
    private final PortraitRepository portraitRepository;
    private final NPCSystem npcSystem;
    private final Player player;
    private final InkBridge inkBridge;
    private final SunSystem sunSystem;
    @Setter
    private BaseLocation currentLocation;


    public GameContext(String startingLocationId) throws IOException {
        router = new MessageRouter(eventBus, startingLocationId);
        loader = new LazyLocationLoader(time, router);
        currentLocation = loader.getLocation(startingLocationId);
        portraitRepository = new PortraitRepository(idProvider);
        npcSystem = new NPCSystem(idProvider, portraitRepository, () -> currentLocation);
        player = new Player(idProvider, portraitRepository);
        inkBridge = new InkBridge();
        sunSystem = new SunSystem(inkBridge, router, time);
    }

    public void initialize() {
        sunSystem.initialize();
        time.scheduler().scheduleRepeating("gpu_monitor", 1L, time().now(), 10000L, () -> {
            Optional<GpuStatus> query = NvidiaSmiMonitor.query();
            query.ifPresent(q -> LogHelper.info(this, q.toString()));
        });
    }

    public void processTextureUpload() {
        portraitRepository.processTextureUpload();
    }

    public void updateTime(float delta) {
        time.update(delta);
    }

    public void updateRouterLocation() {
        if (!router.currentLocationId().equals(currentLocation.targetId())) {
            router.currentLocationId(currentLocation.targetId());
        }
    }
}
