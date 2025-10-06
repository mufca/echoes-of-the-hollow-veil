package io.github.mufca.libgdx.datastructure.statistics;

import lombok.Data;

@Data
public class PrimaryStatistics {

    private float strength;
    private float dexterity;
    private float endurance;
    private float intelligence;
    private float willpower;
    private float charisma;

    public PrimaryStatistics(float strength, float dexterity, float endurance, float intelligence, float willpower,
        float charisma) {
        this.strength = strength;
        this.dexterity = dexterity;
        this.endurance = endurance;
        this.intelligence = intelligence;
        this.willpower = willpower;
        this.charisma = charisma;
    }

    public PrimaryStatistics() {
        this(1f, 1f, 1f, 1f, 1f, 1f);
    }

}
