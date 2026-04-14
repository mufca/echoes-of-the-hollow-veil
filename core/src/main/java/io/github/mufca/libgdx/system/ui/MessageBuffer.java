package io.github.mufca.libgdx.system.ui;

import com.badlogic.gdx.utils.Array;

public class MessageBuffer {

    private final Array<String> pendingMessages = new Array<>(false, 16);

    public void add(String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        pendingMessages.add(message);
    }

    public void addAll(String... messages) {
        for (String msg : messages) {
            add(msg);
        }
    }

    public Array<String> drain() {
        return pendingMessages;
    }

    public void clear() {
        pendingMessages.clear();
    }

    public boolean hasMessages() {
        return pendingMessages.size > 0;
    }
}