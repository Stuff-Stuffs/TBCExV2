package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment.BattleParticipantEquipment;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemStack;
import org.jetbrains.annotations.Nullable;

public interface BattleParticipantInventory extends BattleParticipantInventoryView {
    BattleParticipantEquipment getEquipment(BattleParticipantEquipmentSlot slot);

    @Nullable BattleParticipantInventoryHandle getInventoryHandle(BattleParticipantEquipmentSlot slot);

    @Nullable BattleParticipantItemStack take(BattleParticipantInventoryHandle handle, int amount);

    BattleParticipantInventoryHandle give(BattleParticipantItemStack stack);

    boolean equip(BattleParticipantInventoryHandle handle, BattleParticipantEquipmentSlot slot);

    boolean unequip(BattleParticipantEquipmentSlot slot);
}
