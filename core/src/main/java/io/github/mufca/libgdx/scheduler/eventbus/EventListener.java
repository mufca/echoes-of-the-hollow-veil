package io.github.mufca.libgdx.scheduler.eventbus;

public interface EventListener<E extends GameEvent> {

    void on(E event);
}
