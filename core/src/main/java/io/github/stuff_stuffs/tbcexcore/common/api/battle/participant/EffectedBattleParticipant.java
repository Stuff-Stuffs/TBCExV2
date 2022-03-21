package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;

import java.util.function.Consumer;

public interface EffectedBattleParticipant {
    void tbcex$addEffects(Consumer<BattleParticipantEffect> effectAdder);
}
