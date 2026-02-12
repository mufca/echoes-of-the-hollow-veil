package io.github.mufca.libgdx.gui.core;

import com.badlogic.gdx.InputAdapter;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import io.github.mufca.libgdx.system.command.KeyPressedEvent;
import java.util.BitSet;
import java.util.Objects;

public class GlobalInputProcessor extends InputAdapter {

    private final EventBus eventBus;
    private final BitSet pressedKeys = new BitSet();

    public GlobalInputProcessor(EventBus eventBus) {
        this.eventBus = Objects.requireNonNull(eventBus);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!pressedKeys.get(keycode)) {
            pressedKeys.set(keycode);
            eventBus.publish(new KeyPressedEvent(keycode));
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        pressedKeys.clear(keycode);
        return true;
    }
}
