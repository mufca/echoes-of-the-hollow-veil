package io.github.mufca.libgdx.datastructure.character;

import io.github.mufca.libgdx.util.Strings;
import java.util.Objects;

public record BaseCharacter(Long characterId, CharacterType type, String name) {

    public BaseCharacter {
        Objects.requireNonNull(characterId, "CharacterId can't be null");
        Objects.requireNonNull(type, "CharacterType can't be null");
        Strings.requireNonBlank(name, "Name can't be blank or null");
    }

    public boolean hasName() {
        return !"Unnamed".equals(name);
    }
}
