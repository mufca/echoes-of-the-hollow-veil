package io.github.mufca.libgdx.datastructure.character.logic.components;

import static io.github.mufca.libgdx.constant.ErrorConstants.CAN_T_BE_NULL;
import static io.github.mufca.libgdx.constant.ErrorConstants.NOT_BLANK_PATTERN;

import io.github.mufca.libgdx.util.Strings;
import java.util.Objects;

public record BaseCharacter(long characterId, CharacterType type, String name) {

    public BaseCharacter {
        Objects.requireNonNull(characterId, CAN_T_BE_NULL.formatted("CharacterId"));
        Objects.requireNonNull(type, NOT_BLANK_PATTERN.formatted("Type"));
        Strings.requireNonBlank(name, NOT_BLANK_PATTERN.formatted("Name"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId);
    }
}
