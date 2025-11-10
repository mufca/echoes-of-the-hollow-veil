package io.github.mufca.libgdx.datastructure.location.feature;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

public class IdProviderTest {

    private static final int THREAD_COUNT = 32;
    private static final int IDS_PER_THREAD = 200_000;
    private static final int EXPECTED_TOTAL = THREAD_COUNT * IDS_PER_THREAD;
    private final int TESTING_SIZE = 5_000_000;

    @Test
    public void shouldGetUniqueIds() {
        // GIVEN
        var usedIds = new HashSet<Long>();
        var idProvider = new IdProvider();

        // WHEN
        for (int i = 0; i < TESTING_SIZE; i++) {
            var idToAdd = idProvider.generateUniqueId();
            var isAdded = usedIds.add(idToAdd);
            assertThat(isAdded).as("Failed to add ID %s at iteration %s due to duplication", idToAdd, i).isTrue();
        }

        // THEN
        assertThat(usedIds).hasSize(TESTING_SIZE);
    }

    @Test
    public void shouldGenerateUniqueIdsInMultithreadedEnvironment() {
        // GIVEN
        var idProvider = new IdProvider();
        var allIds = Collections.newSetFromMap(new ConcurrentHashMap<>());

        try (var executor = Executors.newFixedThreadPool(THREAD_COUNT)) {
            var latch = new CountDownLatch(THREAD_COUNT);

            // WHEN
            for (int t = 0; t < THREAD_COUNT; t++) {
                executor.execute(() -> {
                    for (int i = 0; i < IDS_PER_THREAD; i++) {
                        long id = idProvider.generateUniqueId();
                        boolean added = allIds.add(id);
                        if (!added) {
                            throw new IllegalStateException("Duplicate ID detected: %d".formatted(id));
                        }
                    }
                    latch.countDown();
                });
            }
            executor.shutdownNow();
        }

        // THEN
        assertThat(allIds)
            .hasSize(EXPECTED_TOTAL)
            .doesNotContainNull();
    }
}