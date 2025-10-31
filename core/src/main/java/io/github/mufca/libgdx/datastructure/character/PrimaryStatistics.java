package io.github.mufca.libgdx.datastructure.character;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PrimaryStatistics {

    public static final String CAN_T_BE_NULL = "%s statistic can't be null";
    public static final String CHARACTER_ID_IS_NULL = "Character characterId is null";

    private final Long characterId;

    private Float strength;
    private Float dexterity;
    private Float constitution;
    private Float intelligence;
    private Float wisdom;
    private Float charisma;


    public PrimaryStatistics(Long characterId, Float strength, Float dexterity,
        Float constitution, Float intelligence, Float wisdom, Float charisma) {
        this.characterId = Objects.requireNonNull(characterId, CHARACTER_ID_IS_NULL);
        this.strength = Objects.requireNonNull(strength, CAN_T_BE_NULL.formatted("Strength"));
        this.dexterity = Objects.requireNonNull(dexterity, CAN_T_BE_NULL.formatted("Dexterity"));
        this.constitution = Objects.requireNonNull(constitution, CAN_T_BE_NULL.formatted("Constitution"));
        this.intelligence = Objects.requireNonNull(intelligence, CAN_T_BE_NULL.formatted("Intelligence"));
        this.wisdom = Objects.requireNonNull(wisdom, CAN_T_BE_NULL.formatted("Wisdom"));
        this.charisma = Objects.requireNonNull(charisma, CAN_T_BE_NULL.formatted("Charisma"));
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
