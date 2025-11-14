package io.github.mufca.libgdx.datastructure.character.logic.components;

import static io.github.mufca.libgdx.constant.ErrorConstants.STATISTIC_CAN_T_BE_NULL;
import static io.github.mufca.libgdx.constant.ErrorConstants.CHARACTER_ID_CAN_T_BE_NULL;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PrimaryStatistics {

    private final Long characterId;

    private Float strength;
    private Float dexterity;
    private Float constitution;
    private Float intelligence;
    private Float wisdom;
    private Float charisma;


    public PrimaryStatistics(Long characterId, Float strength, Float dexterity,
        Float constitution, Float intelligence, Float wisdom, Float charisma) {
        this.characterId = Objects.requireNonNull(characterId, CHARACTER_ID_CAN_T_BE_NULL);
        this.strength = Objects.requireNonNull(strength, STATISTIC_CAN_T_BE_NULL.formatted("Strength"));
        this.dexterity = Objects.requireNonNull(dexterity, STATISTIC_CAN_T_BE_NULL.formatted("Dexterity"));
        this.constitution = Objects.requireNonNull(constitution, STATISTIC_CAN_T_BE_NULL.formatted("Constitution"));
        this.intelligence = Objects.requireNonNull(intelligence, STATISTIC_CAN_T_BE_NULL.formatted("Intelligence"));
        this.wisdom = Objects.requireNonNull(wisdom, STATISTIC_CAN_T_BE_NULL.formatted("Wisdom"));
        this.charisma = Objects.requireNonNull(charisma, STATISTIC_CAN_T_BE_NULL.formatted("Charisma"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId, strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    @Override
    public String toString() {
        return "PrimaryStatistics[" +
            "characterId=" + characterId + ", " +
            "strength=" + strength + ", " +
            "dexterity=" + dexterity + ", " +
            "constitution=" + constitution + ", " +
            "intelligence=" + intelligence + ", " +
            "wisdom=" + wisdom + ", " +
            "charisma=" + charisma + ']';
    }
}
