package io.github.mufca.libgdx.util;

import com.badlogic.gdx.Gdx;
import java.util.function.BiConsumer;

public class LogHelper {

    public enum Level {
        DEBUG(Gdx.app::debug),
        INFO(Gdx.app::log),
        ERROR(Gdx.app::error);

        final BiConsumer<String, String> logMethod;

        Level(BiConsumer<String, String> logMethod) {
            this.logMethod = logMethod;
        }
    }

    public static void log(Object obj, Level level, String message) {
        String tag = (obj != null) ? obj.getClass().getSimpleName() : "Unknown";
        level.logMethod.accept(tag, message);
    }

    public static void debug(Object obj, String msg) { log(obj, Level.DEBUG, msg); }
    public static void info(Object obj, String msg)  { log(obj, Level.INFO, msg); }
    public static void error(Object obj, String msg) { log(obj, Level.ERROR, msg); }
}
