package io.github.mufca.libgdx.datastructure.keyprovider;

import static io.github.mufca.libgdx.datastructure.keyprovider.KeyProvider.NO_KEY_AVAILABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class KeyProviderTest {

    @ParameterizedTest
    @EnumSource(KeyboardLayout.class)
    void shouldReturnAllPreferredNumericsKeys(KeyboardLayout layout) {
        // GIVEN
        KeyProvider provider = new KeyProvider(layout);
        // WHEN
        List<KeyboardKey> keyboardKeys = layout.preferredNumerics().stream()
            .map(key -> provider.takeNextKey())
            .toList();
        // THEN
        assertThat(keyboardKeys).containsExactlyElementsOf(layout.preferredNumerics());
    }

    @ParameterizedTest
    @EnumSource(KeyboardLayout.class)
    void shouldThrowException_WhenAllKeysDepleted(KeyboardLayout layout) {
        // GIVEN
        KeyProvider provider = new KeyProvider(layout);
        // WHEN
        layout.getAll().stream()
            .flatMap(Collection::stream)
            .forEach(key -> provider.takeNextKey());
        // THEN
        Throwable thrown = catchThrowable(provider::takeNextKey);
        assertThat(thrown)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(NO_KEY_AVAILABLE);
    }

    @ParameterizedTest
    @EnumSource(KeyboardLayout.class)
    void shouldReuseReleasedKeys(KeyboardLayout layout) {
        // GIVEN
        KeyProvider provider = new KeyProvider(layout);

        // WHEN
        KeyboardKey firstKey = provider.takeNextKey();
        KeyboardKey secondKey = provider.takeNextKey();
        KeyboardKey thirdKey = provider.takeNextKey();
        provider.releaseKeys(Set.of(firstKey, thirdKey));
        KeyboardKey fourthKey = provider.takeNextKey();
        KeyboardKey fifthKey = provider.takeNextKey();

        // THEN
        assertThat(firstKey).isSameAs(fourthKey); // firstKey reused on fourthKey
        assertThat(secondKey).isNotSameAs(fifthKey);  // second key avoided when reusing on fifthKey
        assertThat(thirdKey).isSameAs(fifthKey); // thirdKey reused on fifthKey
    }
}
