package io.github.mufca.libgdx.datastructure.character.logic.components;

import static io.github.mufca.libgdx.constant.ErrorConstants.CHARACTER_ID_CAN_T_BE_NULL;

import io.github.mufca.libgdx.util.Strings;
import java.util.Objects;

public record AppearanceTraits(Long characterId, String firstTrait, String secondTrait, String race) {

    public static final String NULL_OR_BLANK_PATTERN = "%s can't be null or blank";

    public AppearanceTraits {
        Objects.requireNonNull(characterId, CHARACTER_ID_CAN_T_BE_NULL);
        Strings.requireNonBlank(firstTrait, NULL_OR_BLANK_PATTERN.formatted("First trait"));
        Strings.requireNonBlank(secondTrait, NULL_OR_BLANK_PATTERN.formatted("Second trait"));
        Strings.requireNonBlank(race, NULL_OR_BLANK_PATTERN.formatted("Race"));
    }
}
