package io.github.mufca.libgdx.datastructure.location.feature;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

public class IdProviderTest {

    private final int TESTING_SIZE = 5000000;

    @Test
    public void shouldGetUniqueIds() {
        // GIVEN
        var usedIds = new HashSet<Long>();
        var idProvider = new IdProvider();

        // WHEN
        for (int i = 0; i < TESTING_SIZE; i++) {
            var isAdded = usedIds.add(idProvider.generateUniqueId());
            assertThat(isAdded).isTrue();
        }

        // THEN
        assertThat(usedIds).hasSize(TESTING_SIZE);
    }
}