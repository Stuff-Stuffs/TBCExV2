package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment.BattleParticipantEquipment;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;

public interface EquippableBattleParticipantItem extends BattleParticipantItem {
    BattleParticipantEquipment createEquipment(BattleParticipantStateView view);
}
