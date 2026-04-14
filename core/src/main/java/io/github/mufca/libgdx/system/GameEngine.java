package io.github.mufca.libgdx.system;

import static io.github.mufca.libgdx.util.UIHelper.doNothing;

import io.github.mufca.libgdx.context.GameContext;
import io.github.mufca.libgdx.datastructure.character.logic.components.StartRegenEvent;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import io.github.mufca.libgdx.scheduler.eventbus.GameEvent;
import io.github.mufca.libgdx.system.command.CommandSystem;
import io.github.mufca.libgdx.system.command.KeyPressedEvent;
import io.github.mufca.libgdx.system.command.MovementRequestEvent;
import io.github.mufca.libgdx.system.ink.InkFunctions;
import io.github.mufca.libgdx.system.movement.LocationChangedEvent;
import io.github.mufca.libgdx.system.movement.MovementEvent;
import io.github.mufca.libgdx.system.movement.MovementSystem;
import io.github.mufca.libgdx.system.npc.NPCSystem;
import io.github.mufca.libgdx.system.regen.RegenSystem;
import io.github.mufca.libgdx.system.time.TimeSystem;
import io.github.mufca.libgdx.system.ui.MessageBuffer;
import io.github.mufca.libgdx.system.ui.UIMessageSystem;
import io.github.mufca.libgdx.system.weather.DayNightSystem;
import lombok.Getter;

@Getter
public class GameEngine {

    private final InkFunctions inkFunctions = new InkFunctions();
    private final MessageBuffer messageBuffer = new MessageBuffer();
    private final EventBus eventBus;
    private final TimeSystem timeSystem;
    private final MovementSystem movementSystem;
    private final RegenSystem regenSystem;
    private final DayNightSystem dayNightSystem;
    private final CommandSystem commandSystem;
    private final NPCSystem npcSystem;
    private final UIMessageSystem uiMessageSystem;

    public GameEngine(GameContext context, EventBus eventBus) {
        this.eventBus = eventBus;
        timeSystem = new TimeSystem(context.idProvider());
        movementSystem = new MovementSystem(eventBus, context, context.player(), messageBuffer);
        regenSystem = new RegenSystem(context.player(), context.npcRepository(), eventBus, timeSystem);
        dayNightSystem = new DayNightSystem(inkFunctions, timeSystem, messageBuffer);
        commandSystem = new CommandSystem(eventBus);
        npcSystem = new NPCSystem(context.npcRepository(), context);
        uiMessageSystem = new UIMessageSystem(inkFunctions, messageBuffer, context.npcRepository(), context);
    }

    public void initialize() {
//        timeSystem.scheduleRepeating("gpu_monitor", 25L, () -> {
//            Optional<GpuStatus> query = NvidiaSmiMonitor.query();
//            query.ifPresent(q -> LogHelper.info(this, q.toString()));
//        });
        eventBus.post(new LocationChangedEvent("", "forest_glade_0001"));
        dayNightSystem.initialize();
    }

    public void update(float delta) {
        timeSystem.update(delta);
        processEvent(eventBus.getEvent(KeyPressedEvent.class));
        processEvent(eventBus.getEvent(MovementRequestEvent.class));
        processEvent(eventBus.getEvent(MovementEvent.class));
        processEvent(eventBus.getEvent(LocationChangedEvent.class));
        processEvent(eventBus.getEvent(StartRegenEvent.class));
        eventBus.flush();
    }

    private void processEvent(GameEvent event) {
        switch (event) {
            case null -> doNothing();
            case KeyPressedEvent kPrEvent -> commandSystem.handleKeyPressed(kPrEvent);
            case MovementRequestEvent mReEvent -> movementSystem.attemptToMove(mReEvent);
            case MovementEvent mEvent -> uiMessageSystem.handleMovementEvent(mEvent);
            case LocationChangedEvent lChEvent -> {
                npcSystem.handleLocationChangedEvent(lChEvent);
                uiMessageSystem.handleLocationChangedEvent(lChEvent);
            }
            case StartRegenEvent sReEvent -> regenSystem.startRegen(sReEvent);
            default -> throw new IllegalStateException("Unexpected value: " + event);
        }
    }
}