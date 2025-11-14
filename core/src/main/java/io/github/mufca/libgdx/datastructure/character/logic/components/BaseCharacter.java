package io.github.mufca.libgdx.datastructure.character.logic.components;

import static io.github.mufca.libgdx.constant.ErrorConstants.CHARACTER_ID_CAN_T_BE_NULL;
import static io.github.mufca.libgdx.constant.ErrorConstants.NOT_BLANK_PATTERN;

import io.github.mufca.libgdx.util.Strings;
import java.util.Objects;

public record BaseCharacter(Long characterId, CharacterType type, String name) {

    public BaseCharacter {
        Objects.requireNonNull(characterId, CHARACTER_ID_CAN_T_BE_NULL);
        Objects.requireNonNull(type, NOT_BLANK_PATTERN.formatted("Type"));
        Strings.requireNonBlank(name, NOT_BLANK_PATTERN.formatted("Name"));
    }
}
