package io.github.mufca.libgdx.datastructure.character.logic.components;

import static io.github.mufca.libgdx.constant.ErrorConstants.STATISTIC_CAN_T_BE_NULL;
import static io.github.mufca.libgdx.constant.ErrorConstants.CHARACTER_ID_CAN_T_BE_NULL;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class SecondaryStatistics {

    private final Long characterId;

    private Float hitPoints;
    private Float stamina;
    private Float mana;

    public SecondaryStatistics(Long characterId, Float hitPoints, Float stamina, Float mana) {
        this.characterId = Objects.requireNonNull(characterId, CHARACTER_ID_CAN_T_BE_NULL);
        this.hitPoints = Objects.requireNonNull(hitPoints, STATISTIC_CAN_T_BE_NULL.formatted("HitPoints"));
        this.stamina = Objects.requireNonNull(stamina, STATISTIC_CAN_T_BE_NULL.formatted("Stamina"));
        this.mana = Objects.requireNonNull(mana, STATISTIC_CAN_T_BE_NULL.formatted("Mana"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId, hitPoints, stamina, mana);
    }

    @Override
    public String toString() {
        return "SecondaryStatistics[" +
            "characterId=" + characterId + ", " +
            "hitPoints=" + hitPoints + ", " +
            "stamina=" + stamina + ", " +
            "mana=" + mana + ']';
    }
}
