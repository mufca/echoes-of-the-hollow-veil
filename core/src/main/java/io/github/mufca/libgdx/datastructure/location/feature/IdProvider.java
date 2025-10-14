package io.github.mufca.libgdx.datastructure.location.feature;

import java.util.HashSet;
import java.util.Set;

public class IdProvider {

    private final Set<Long> used = new HashSet<>();

    public long generateFeatureId() {
        long id = getId();
        while (!used.add(id)) {
            id = getId();
        }
        return id;
    }

    private long getId() {
        return System.currentTimeMillis();
    }
}
