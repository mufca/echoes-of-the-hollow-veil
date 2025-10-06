package io.github.mufca.libgdx.util;

public class SafeCast {

    public static int floatToIntExact(float value) {
        if (value != (int) value) {
            throw new IllegalArgumentException("Float value is not an integer: " + value);
        }
        return (int) value;
    }
}
