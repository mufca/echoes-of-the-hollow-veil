package io.github.mufca.libgdx.datastructure.character.logic.components;

import lombok.Getter;

@Getter
public enum StatTag {
    STRENGTH(true, "Strength"),
    DEXTERITY(true, "Dexterity"),
    CONSTITUTION(true, "Constitution"),
    INTELLIGENCE(true, "Intelligence"),
    WISDOM(true, "Wisdom"),
    CHARISMA(true, "Charisma"),
    HIT_POINTS(false, "Physical"),
    STAMINA(false, "Stamina"),
    MANA(false, "Mental");

    private final boolean isPrimary;
    private final String label;

    StatTag(boolean isPrimary, String label) {
        this.label = label;
        this.isPrimary = isPrimary;
    }


}
