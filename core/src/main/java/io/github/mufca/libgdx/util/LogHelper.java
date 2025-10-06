package io.github.mufca.libgdx.util;

import com.badlogic.gdx.Gdx;

public class LogHelper {

    public static void debug(Object obj, String message) {
        if (obj == null) {
            debug(message);
        } else {
            Gdx.app.debug(obj.getClass().getSimpleName(), message);
        }
    }

    private static void debug(String message) {
        Gdx.app.debug("Unknown class", message);
    }
}
