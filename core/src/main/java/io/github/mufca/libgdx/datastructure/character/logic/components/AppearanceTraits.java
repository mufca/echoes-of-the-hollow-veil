package io.github.mufca.libgdx.datastructure.character.logic.components;

import static io.github.mufca.libgdx.constant.ErrorConstants.CAN_T_BE_NULL;

import io.github.mufca.libgdx.util.Strings;
import java.util.Objects;

public record AppearanceTraits(long characterId, String firstTrait, String secondTrait, String race) {

    public static final String NULL_OR_BLANK_PATTERN = "%s can't be null or blank";

    public AppearanceTraits {
        Objects.requireNonNull(characterId, CAN_T_BE_NULL.formatted("CharacterId"));
        Strings.requireNonBlank(firstTrait, NULL_OR_BLANK_PATTERN.formatted("First trait"));
        Strings.requireNonBlank(secondTrait, NULL_OR_BLANK_PATTERN.formatted("Second trait"));
        Strings.requireNonBlank(race, NULL_OR_BLANK_PATTERN.formatted("Race"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId);
    }
}
