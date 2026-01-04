package io.github.mufca.libgdx.util;

public class SafeCast {

    public static int floatToIntExact(float value) {
        if (value != (int) value) {
            throw new IllegalArgumentException(
                "Float value is not an integer: %f, expected: %d".formatted(value, (int) value));
        }
        return (int) value;
    }

    public static int explicitCeiling(float value) {
        return (int) Math.ceil(value);
    }
}
