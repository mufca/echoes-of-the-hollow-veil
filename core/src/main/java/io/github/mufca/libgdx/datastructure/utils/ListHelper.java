package io.github.mufca.libgdx.datastructure.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ListHelper {

    /**
     * Returns a stream of the list in reversed order (Java 21+).
     */
    public static <T> Stream<T> reverseToStream(List<T> list) {
        return list.reversed().stream();
    }

    /**
     * Returns a new list containing elements up to and including the target. If the target is not found, returns the
     * full list.
     */
    public static <T> List<T> upToInclusive(List<T> list, T target) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            result.add(item);
            if (Objects.equals(item, target)) {
                break;
            }
        }
        return result;
    }

}