package io.github.mufca.libgdx.scheduler.eventbus;

import java.io.IOException;

public interface EventListener<E extends GameEvent> {

    void on(E event) throws IOException;
}
