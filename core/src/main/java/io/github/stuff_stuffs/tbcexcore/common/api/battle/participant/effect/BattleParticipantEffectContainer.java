package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;

public interface BattleParticipantEffectContainer extends BattleParticipantEffectContainerView {
    void addEffect(final BattleParticipantEffect effect, Tracer<ActionTrace> tracer);

    void removeEffect(final BattleParticipantEffectType<?, ?> type, Tracer<ActionTrace> tracer);

    <View extends BattleParticipantEffect, Mut extends View> Mut getEffectMut(final BattleParticipantEffectType<View, Mut> type);
}
