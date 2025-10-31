package io.github.mufca.libgdx.util;

public final class Strings {

    private Strings() {
    }

    public static String requireNonBlank(String str, String message) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return str;
    }
}
