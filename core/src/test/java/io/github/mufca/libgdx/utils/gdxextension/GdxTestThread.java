package io.github.mufca.libgdx.utils.gdxextension;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public final class GdxTestThread extends Thread {

    private final static String NAME = "GdxTestThread";

    public GdxTestThread(String displayName, TestApplicationListener listener) {
        super(() -> {
            var configuration = new Lwjgl3ApplicationConfiguration();
            configuration.setTitle(displayName);
            configuration.setWindowedMode(1680, 1050);
            configuration.setDecorated(false);
            configuration.useVsync(false);
            configuration.setForegroundFPS(30);
            configuration.setInitialVisible(true);
            new Lwjgl3Application(listener, configuration);
        }, NAME);
    }


}
