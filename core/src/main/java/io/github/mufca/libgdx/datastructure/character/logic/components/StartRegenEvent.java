package io.github.mufca.libgdx.datastructure.character.logic.components;

import io.github.mufca.libgdx.scheduler.eventbus.GameEvent;

public record StartRegenEvent(Long characterId, StatTag statTag) implements GameEvent {

}

