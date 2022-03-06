package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.health;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage.BattleParticipantDamagePacket;

public interface BattleParticipantHealthContainer extends BattleParticipantHealthContainerView {
    boolean damage(BattleParticipantDamagePacket damage);
}
