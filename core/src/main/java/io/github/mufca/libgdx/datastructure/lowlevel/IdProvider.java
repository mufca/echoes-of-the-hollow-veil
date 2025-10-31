package io.github.mufca.libgdx.datastructure.lowlevel;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class IdProvider {

    private static final int BUFFER_SIZE = 300;
    private final Deque<Long> recent = new ArrayDeque<>(BUFFER_SIZE);
    private final Set<Long> used = new HashSet<>(BUFFER_SIZE);

    public synchronized long generateUniqueId() {
        long id = getId();
        while (used.contains(id)) {
            id = getId();
        }
        used.add(id);
        recent.addLast(id);
        if (recent.size() > BUFFER_SIZE) {
            Long removed = recent.removeFirst();
            used.remove(removed);
        }

        return id;
    }

    private long getId() {
        return System.nanoTime();
    }
}