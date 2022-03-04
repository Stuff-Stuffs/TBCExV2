package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantEquipmentSlot;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;

import java.util.Set;

public interface BattleParticipantEquipment {
    BattleParticipantEquipmentType<?> getType();

    Set<BattleParticipantEquipmentSlot> getEquipableSlots();

    Set<BattleParticipantEquipmentSlot> getBlockedSlots(BattleParticipantEquipmentSlot slot);

    void init(BattleParticipantEquipmentSlot slot, BattleParticipantState state);

    void deinit();
}
