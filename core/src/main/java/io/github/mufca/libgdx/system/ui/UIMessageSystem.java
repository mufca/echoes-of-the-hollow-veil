package io.github.mufca.libgdx.system.ui;

import io.github.mufca.libgdx.context.npc.NPCRepository;
import io.github.mufca.libgdx.datastructure.location.CurrentLocationProvider;
import io.github.mufca.libgdx.datastructure.location.Exit;
import io.github.mufca.libgdx.system.ink.InkFunctions;
import io.github.mufca.libgdx.system.movement.LocationChangedEvent;
import io.github.mufca.libgdx.system.movement.MovementEvent;
import io.github.mufca.libgdx.util.UIHelper;
import java.util.List;
import java.util.stream.Collectors;

public class UIMessageSystem {

    private final InkFunctions inkFunctions;
    private final CurrentLocationProvider currentLocationProvider;
    private final NPCRepository npcRepository;
    private final MessageBuffer messageBuffer;


    public UIMessageSystem(InkFunctions inkFunctions, MessageBuffer messageBuffer, NPCRepository npcRepository,
        CurrentLocationProvider currentLocationProvider) {
        this.npcRepository = npcRepository;
        this.inkFunctions = inkFunctions;
        this.messageBuffer = messageBuffer;
        this.currentLocationProvider = currentLocationProvider;

    }

    public void handleLocationChangedEvent(LocationChangedEvent locationChangedEvent) {
        var currentLocation = currentLocationProvider.currentLocation();
        var exits = "Exits: " + currentLocation.exits().stream()
            .map(Exit::name)
            .collect(Collectors.joining(", "));
        var npcs = npcRepository.getByCurrentLocation(currentLocation.targetId()).stream()
            .map(id -> npcRepository.get(id).orElseThrow())
            .filter(npc -> npc.isAlive())
            .map(npc -> npc.toString())
            .collect(Collectors.joining(", "));
        List.of(currentLocation.shortDescription(), currentLocation.longDescription(), npcs, exits).stream()
            .filter(s -> s != null && !s.isEmpty())
            .map(UIHelper::capitalize)
            .forEach(messageBuffer::add);
    }

    public void handleMovementEvent(MovementEvent movementEvent) {
        messageBuffer.add(inkFunctions.getMovementMessage(movementEvent.direction(), movementEvent.present()));
    }
}
