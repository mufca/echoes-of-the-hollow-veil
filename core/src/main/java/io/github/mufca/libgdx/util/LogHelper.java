package io.github.mufca.libgdx.util;

import org.tinylog.Logger;

public final class LogHelper {

    private LogHelper() {
    }

    public enum Level {
        DEBUG, INFO, ERROR, WARN
    }

    private static String tagOf(Object obj) {
        if (obj == null) {
            return "Unknown";
        }
        if (obj instanceof Class<?> cls) {
            return cls.getSimpleName();
        }
        return obj.getClass().getSimpleName();
    }

    private static void log(Object obj, Level level, String message) {
        String tag = tagOf(obj);
        switch (level) {
            case DEBUG -> Logger.tag(tag).debug(message);
            case INFO -> Logger.tag(tag).info(message);
            case ERROR -> Logger.tag(tag).error(message);
            case WARN -> Logger.tag(tag).warn(message);
        }
    }

    public static void debug(Object obj, String msg) {
        log(obj, Level.DEBUG, msg);
    }

    public static void info(Object obj, String msg) {
        log(obj, Level.INFO, msg);
    }

    public static void error(Object obj, String msg) {
        log(obj, Level.ERROR, msg);
    }

    public static void warn(Object obj, String msg) {
        log(obj, Level.WARN, msg);
    }
}