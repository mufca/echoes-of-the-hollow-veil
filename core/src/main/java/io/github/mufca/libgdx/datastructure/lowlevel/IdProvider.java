package io.github.mufca.libgdx.datastructure.lowlevel;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public final class IdProvider {

    private final AtomicLong counter = new AtomicLong(0);

    public long generateUniqueId() {
        return counter.incrementAndGet();
    }
}