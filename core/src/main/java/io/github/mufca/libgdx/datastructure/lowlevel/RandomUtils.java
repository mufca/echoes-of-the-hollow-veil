package io.github.mufca.libgdx.datastructure.lowlevel;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomUtils {

    private static final Random RANDOM = new Random();

    public static <T> Optional<T> pickFromList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }
        int index = RANDOM.nextInt(list.size());
        return Optional.of(list.get(index));
    }

    public static int pickFromRange(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }
}
