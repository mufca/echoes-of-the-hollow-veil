package io.github.mufca.libgdx.system.regen;

import static io.github.mufca.libgdx.constant.ErrorConstants.UNKNOWN_CHARACTER;
import static io.github.mufca.libgdx.system.time.Ticks.SECOND;

import io.github.mufca.libgdx.datastructure.character.logic.components.CharacterStats;
import io.github.mufca.libgdx.datastructure.character.logic.components.StartRegenEvent;
import io.github.mufca.libgdx.context.npc.NPCRepository;
import io.github.mufca.libgdx.datastructure.player.Player;
import io.github.mufca.libgdx.scheduler.eventbus.EventBus;
import io.github.mufca.libgdx.system.time.TimeSystem;
import io.github.mufca.libgdx.util.LogHelper;

public class RegenSystem {

    private static final String CAN_T_REGEN_PRIMARY_STATISTIC_GENERAL_ERROR = "Can't regen primary statistic";
    private static final String CAN_T_REGEN_PRIMARY_STATISTIC_ERROR = "Can't regen primary statistic, but received %s for %s stat.";
    private static final String REGEN_CANCELED_DEBUG = "Canceled regen for character with id %d for %s stat as it reached maximum.";
    private final long playerId;
    private final CharacterStats playerStats;
    private final NPCRepository npcRepository;
    private final TimeSystem timeSystem;

    public RegenSystem(Player player, NPCRepository npcRepository, EventBus eventBus, TimeSystem timeSystem) {
        this.playerId = player.characterId();
        this.playerStats = player.characterStats();
        this.npcRepository = npcRepository;
        this.timeSystem = timeSystem;
//        eventBus.subscribe(StartRegenEvent.class, this::startRegen);
    }

    public void startRegen(StartRegenEvent startRegenEvent) {
        LogHelper.debug(this, "Starting regen for %d".formatted(startRegenEvent.characterId()));
        Long characterId = startRegenEvent.characterId();
        var characterStats = characterId.equals(playerId) ? playerStats :
            npcRepository.get(characterId)
                .orElseThrow(() -> new IllegalArgumentException(UNKNOWN_CHARACTER.formatted(characterId)))
                .characterStats();
        timeSystem.scheduleRepeating(startRegenEvent, SECOND.duration(), () -> regen(characterStats, startRegenEvent));
    }

    private void regen(CharacterStats characterStats, StartRegenEvent startRegenEvent) {
        var statTag = startRegenEvent.statTag();
        if (statTag.isPrimary()) {
            var className = StartRegenEvent.class.getSimpleName();
            LogHelper.error(this, CAN_T_REGEN_PRIMARY_STATISTIC_ERROR.formatted(className, statTag.name()));
            throw new IllegalArgumentException(CAN_T_REGEN_PRIMARY_STATISTIC_GENERAL_ERROR);
        }
        characterStats.byTag(statTag, characterStats.byTag(statTag) + 3);
        if (characterStats.isMaxByTag(statTag)) {
            timeSystem.cancel(startRegenEvent);
            LogHelper.debug(this, REGEN_CANCELED_DEBUG.formatted(startRegenEvent.characterId(), statTag.name()));
        }
    }
}
