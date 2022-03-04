package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

public interface BattleParticipantEffectContainer extends BattleParticipantEffectContainerView {
    void addEffect(final BattleParticipantEffect effect);

    void removeEffect(final BattleParticipantEffectType<?, ?> type);

    <View extends BattleParticipantEffect, Mut extends View> Mut getEffectMut(final BattleParticipantEffectType<View, Mut> type);
}
