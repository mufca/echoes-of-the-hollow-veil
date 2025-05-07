package io.github.mufca.libgdx.datastructure.skill;

import java.util.Objects;

public record SkillDescription(SkillType type, String name, String description) {

    private static final String SKILL_S_CANNOT_BE_NULL = "Skill %s cannot be null";
    private static final String TYPE = "type";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    public SkillDescription {
        type = Objects.requireNonNull(type, SKILL_S_CANNOT_BE_NULL.formatted(TYPE));
        name = Objects.requireNonNull(name, SKILL_S_CANNOT_BE_NULL.formatted(NAME));
        description = Objects.requireNonNull(description, SKILL_S_CANNOT_BE_NULL.formatted(DESCRIPTION));
    }
}
