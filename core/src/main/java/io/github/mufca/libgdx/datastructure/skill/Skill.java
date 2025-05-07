package io.github.mufca.libgdx.datastructure.skill;

import lombok.Getter;

public class Skill {
    private SkillDescription description;
    @Getter
    private float value;
    @Getter
    private float practice;

    public Skill(SkillDescription description) {
        this.description = description;
        this.value = 0f;
        this.practice = 0f;
    }

    public void progressValue(float value) {
        this.value += value;
    }

    public void progressPractice(float practice) {
        this.practice += practice;
    }

}
