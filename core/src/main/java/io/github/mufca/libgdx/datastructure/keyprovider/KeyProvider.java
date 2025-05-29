package io.github.mufca.libgdx.datastructure.keyprovider;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class KeyProvider {
    public static final String NO_KEY_AVAILABLE = "No key available";
    private final Set<KeyboardKey> usedKeys = new HashSet<>();
    private final KeyboardLayout layout;

    public KeyProvider(KeyboardLayout layout) {
        this.layout = layout;
    }

    public KeyboardKey takeNextKey() {
        return Stream.of(
                layout.preferredNumerics(),
                layout.preferredLetters(),
                layout.theRest())
            .map(this::findUnusedKeyInList)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .map(this::reserveAndReturn)
            .orElseThrow(() -> new IllegalStateException(NO_KEY_AVAILABLE));
    }

    private KeyboardKey reserveAndReturn(KeyboardKey key) {
        usedKeys.add(key);
        return key;
    }

    private Optional<KeyboardKey> findUnusedKeyInList(List<KeyboardKey> list) {
        return list.stream().filter(key -> !usedKeys.contains(key)).findFirst();
    }

    public void releaseKeys(Set<KeyboardKey> toRemove) {
        usedKeys.removeAll(toRemove);
    }
}
