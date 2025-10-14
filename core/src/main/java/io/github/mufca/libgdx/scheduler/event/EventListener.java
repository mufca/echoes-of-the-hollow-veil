package io.github.mufca.libgdx.scheduler.event;

public interface EventListener<E extends GameEvent> {
    void on(E event);
}
