package io.github.mufca.libgdx.datastructure.random;

import java.util.List;
import java.util.Random;

public class RandomGenerator {

    private static final Random RANDOM = new Random();

    public static <T> T pickFromList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int index = RANDOM.nextInt(list.size());
        return list.get(index);
    }

    public static int pickFromRange(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }
}
