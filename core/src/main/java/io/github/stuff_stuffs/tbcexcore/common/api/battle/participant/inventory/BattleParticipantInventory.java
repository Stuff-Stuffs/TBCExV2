package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment.BattleParticipantEquipment;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleItemStack;
import org.jetbrains.annotations.Nullable;

public interface BattleParticipantInventory extends BattleParticipantInventoryView {
    BattleParticipantEquipment getEquipment(BattleParticipantEquipmentSlot slot);

    BattleParticipantInventoryHandle getInventoryHandle(BattleParticipantEquipmentSlot slot);

    @Nullable BattleItemStack take(BattleParticipantInventoryHandle handle, int amount);

    BattleParticipantInventoryHandle give(BattleItemStack stack);
}
