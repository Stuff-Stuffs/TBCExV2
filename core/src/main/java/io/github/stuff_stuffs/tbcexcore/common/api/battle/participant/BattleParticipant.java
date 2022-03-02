package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;

import java.util.List;

public interface BattleParticipant {
    List<BattleParticipantEffect> getEffects();
}
