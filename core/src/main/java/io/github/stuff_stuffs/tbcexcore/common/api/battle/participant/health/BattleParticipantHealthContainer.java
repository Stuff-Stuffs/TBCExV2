package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.health;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage.BattleParticipantDamagePacket;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;

public interface BattleParticipantHealthContainer extends BattleParticipantHealthContainerView {
    boolean damage(BattleParticipantDamagePacket damage, Tracer<ActionTrace> tracer);
}
