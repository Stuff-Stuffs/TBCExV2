package io.github.stuff_stuffs.tbcexcore.common.api.battle.state;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.effect.BattleEffectContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.event.map.MutEventMap;

public interface BattleState extends BattleStateView {
    @Override
    MutEventMap getEventMap();

    @Override
    BattleParticipantState getParticipant(BattleParticipantHandle handle);

    @Override
    BattleEffectContainer getEffects();
}
