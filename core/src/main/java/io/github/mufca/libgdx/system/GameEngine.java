package io.github.mufca.libgdx.system;

import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import io.github.mufca.libgdx.system.movement.LocationChangedEvent;
import io.github.mufca.libgdx.system.movement.MovementSystem;
import io.github.mufca.libgdx.system.time.TimeSystem;
import io.github.mufca.libgdx.system.command.CommandSystem;
import io.github.mufca.libgdx.system.ink.InkBridge;
import io.github.mufca.libgdx.system.ui.UIMessageSystem;
import io.github.mufca.libgdx.system.weather.DayNightSystem;
import io.github.mufca.libgdx.util.LogHelper;
import io.github.mufca.libgdx.util.NvidiaSmiMonitor;
import io.github.mufca.libgdx.util.NvidiaSmiMonitor.GpuStatus;
import java.io.IOException;
import java.util.Optional;
import lombok.Getter;

@Getter
public class GameEngine {

    private final EventBus eventBus = new EventBus();
    private final InkBridge inkBridge = new InkBridge();
    private final TimeSystem time = new TimeSystem();
    private final MovementSystem movementSystem;
    private final DayNightSystem dayNightSystem;
    private final CommandSystem commandSystem;
    private final UIMessageSystem uiMessageSystem;

    public GameEngine(GameContext context) throws IOException {
        movementSystem = new MovementSystem(eventBus, context);
        dayNightSystem = new DayNightSystem(inkBridge, time, eventBus);
        commandSystem = new CommandSystem(eventBus);
        uiMessageSystem = new UIMessageSystem(eventBus, inkBridge, context);
    }

    public void initialize() {
        time.scheduler().scheduleRepeating("gpu_monitor", 1L, time().now(), 10000L, () -> {
            Optional<GpuStatus> query = NvidiaSmiMonitor.query();
            query.ifPresent(q -> LogHelper.info(this, q.toString()));
        });
        eventBus.publish(new LocationChangedEvent("", "forest_glade_0001"));
        dayNightSystem.initialize();
    }

    public void updateTime(float delta) {
        time.update(delta);
    }
}
