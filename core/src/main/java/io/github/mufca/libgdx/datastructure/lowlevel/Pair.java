package io.github.mufca.libgdx.datastructure.lowlevel;

import java.util.Objects;

public record Pair<T>(T first, T second) {

    private static final String ELEMENT_CANNOT_BE_NULL = "%s element cannot be null";
    private static final String SECOND = "Second";
    private static final String FIRST = "First";

    public Pair {
        first = Objects.requireNonNull(first, ELEMENT_CANNOT_BE_NULL.formatted(FIRST));
        second = Objects.requireNonNull(second, ELEMENT_CANNOT_BE_NULL.formatted(SECOND));
    }
}
