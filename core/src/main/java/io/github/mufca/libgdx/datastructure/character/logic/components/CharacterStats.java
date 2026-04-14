package io.github.mufca.libgdx.datastructure.character.logic.components;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static io.github.mufca.libgdx.constant.ErrorConstants.CAN_T_BE_NULL;
import static io.github.mufca.libgdx.constant.ErrorConstants.CLASS_CAN_T_BE_NULL;
import static io.github.mufca.libgdx.datastructure.character.logic.components.StatTag.CHARISMA;
import static io.github.mufca.libgdx.datastructure.character.logic.components.StatTag.CONSTITUTION;
import static io.github.mufca.libgdx.datastructure.character.logic.components.StatTag.DEXTERITY;
import static io.github.mufca.libgdx.datastructure.character.logic.components.StatTag.HIT_POINTS;
import static io.github.mufca.libgdx.datastructure.character.logic.components.StatTag.INTELLIGENCE;
import static io.github.mufca.libgdx.datastructure.character.logic.components.StatTag.MANA;
import static io.github.mufca.libgdx.datastructure.character.logic.components.StatTag.STAMINA;
import static io.github.mufca.libgdx.datastructure.character.logic.components.StatTag.STRENGTH;
import static io.github.mufca.libgdx.datastructure.character.logic.components.StatTag.WISDOM;

import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import io.github.mufca.libgdx.util.LogHelper;
import java.util.Objects;

public final class CharacterStats {

    private static final float MIN_VALUE = 0f;
    private static final float MAX_VALUE = 500f;
    private static final String CHANGED_STAT_DEBUG = "Changed %s stat for character with id %d to %f...";
    private final long characterId;
    private final EventBus eventBus;

    private final float[] statistics = new float[StatTag.values().length];

    public CharacterStats(long characterId, EventBus eventBus, float strength, float dexterity,
        float constitution, float intelligence, float wisdom, float charisma) {
        this.eventBus = Objects.requireNonNull(eventBus, CAN_T_BE_NULL.formatted("EventBus"));
        this.characterId = characterId;
        setup(strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    private void setup(float strength, float dexterity, float constitution, float intelligence, float wisdom,
        float charisma) {
        statistics[STRENGTH.ordinal()] = strength;
        statistics[DEXTERITY.ordinal()] = dexterity;
        statistics[CONSTITUTION.ordinal()] = constitution;
        statistics[INTELLIGENCE.ordinal()] = intelligence;
        statistics[WISDOM.ordinal()] = wisdom;
        statistics[CHARISMA.ordinal()] = charisma;
        statistics[STAMINA.ordinal()] = maxStamina();
        statistics[MANA.ordinal()] = maxMana();
        statistics[HIT_POINTS.ordinal()] = maxHitPoints();
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CharacterStats p &&
            Objects.equals(this.characterId, p.characterId);
    }

    @Override
    public String toString() {
        return CharacterStats.class.getSimpleName() + "[" +
            "characterId=" + characterId + ", " +
            STRENGTH.name() + "=" + statistics[STRENGTH.ordinal()] + ", " +
            DEXTERITY.name() + "=" + statistics[DEXTERITY.ordinal()] + ", " +
            CONSTITUTION.name() + "=" + statistics[CONSTITUTION.ordinal()] + ", " +
            INTELLIGENCE.name() + "=" + statistics[INTELLIGENCE.ordinal()] + ", " +
            WISDOM.name() + "=" + statistics[WISDOM.ordinal()] + ", " +
            CHARISMA.name() + "=" + statistics[CHARISMA.ordinal()] + ", " +
            HIT_POINTS.name() + "=" + statistics[HIT_POINTS.ordinal()] + ", " +
            STAMINA.name() + "=" + statistics[STAMINA.ordinal()] + ", " +
            MANA.name() + "=" + statistics[MANA.ordinal()] + "]";
    }

    public void byTag(StatTag tag, float newValue) {
        Objects.requireNonNull(tag, CLASS_CAN_T_BE_NULL.formatted(StatTag.class.getSimpleName()));
        boolean wasMax = isMaxByTag(tag);
        clampStat(tag, newValue);
        LogHelper.debug(this, CHANGED_STAT_DEBUG.formatted(tag.name(), characterId, newValue));
        if (!tag.isPrimary() && !isMaxByTag(tag) && wasMax) {
            LogHelper.debug(this, "Sending request to start stamina regen for "+this);
            eventBus.post(new StartRegenEvent(characterId, tag));
        }
    }

    public float byTag(StatTag tag) {
        Objects.requireNonNull(tag, CLASS_CAN_T_BE_NULL.formatted(StatTag.class.getSimpleName()));
        return statistics[tag.ordinal()];
    }

    public float maxByTag(StatTag tag) {
        Objects.requireNonNull(tag, CLASS_CAN_T_BE_NULL.formatted(StatTag.class.getSimpleName()));
        return switch (tag) {
            case STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA -> MAX_VALUE;
            case STAMINA -> maxStamina();
            case MANA -> maxMana();
            case HIT_POINTS -> maxHitPoints();
        };
    }

    public boolean isMaxByTag(StatTag tag) {
        Objects.requireNonNull(tag, CLASS_CAN_T_BE_NULL.formatted(StatTag.class.getSimpleName()));
        return switch (tag) {
            case STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA -> byTag(tag) == MAX_VALUE;
            case STAMINA -> byTag(tag) == maxStamina();
            case MANA -> byTag(tag) == maxMana();
            case HIT_POINTS -> byTag(tag) == maxHitPoints();
        };
    }

    private void clampStat(StatTag tag, float newValue) {
        Objects.requireNonNull(tag, CLASS_CAN_T_BE_NULL.formatted(StatTag.class.getSimpleName()));
        var max = maxByTag(tag);
        statistics[tag.ordinal()] = clamp(newValue, MIN_VALUE, max);
        if (tag.isPrimary() && tag != DEXTERITY) {
            revalidateResources();
        }
    }

    private float maxStamina() {
        return statistics[STRENGTH.ordinal()] + statistics[CONSTITUTION.ordinal()] * 2;
    }

    private float maxMana() {
        return statistics[INTELLIGENCE.ordinal()] + statistics[WISDOM.ordinal()] + statistics[CHARISMA.ordinal()];
    }

    private float maxHitPoints() {
        return statistics[CONSTITUTION.ordinal()] * 3;
    }

    private void revalidateResources() {
        statistics[STAMINA.ordinal()] = clamp(statistics[STAMINA.ordinal()], MIN_VALUE, maxStamina());
        statistics[MANA.ordinal()] = clamp(statistics[MANA.ordinal()], MIN_VALUE, maxMana());
        statistics[HIT_POINTS.ordinal()] = clamp(statistics[HIT_POINTS.ordinal()], MIN_VALUE, maxHitPoints());
    }
}
