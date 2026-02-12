package io.github.mufca.libgdx.system.ui;

import io.github.mufca.libgdx.scheduler.eventbus.GameEvent;

public record UITextEvent(String textMessage) implements GameEvent {
}
