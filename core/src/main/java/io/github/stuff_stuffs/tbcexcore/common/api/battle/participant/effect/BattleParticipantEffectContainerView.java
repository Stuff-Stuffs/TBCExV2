package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

public interface BattleParticipantEffectContainerView {
    boolean hasEffect(BattleParticipantEffectType<?, ?> type);

    <View extends BattleParticipantEffect> View getEffect(BattleParticipantEffectType<View, ?> type);
}
