package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment.BattleParticipantEquipment;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemStack;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import org.jetbrains.annotations.Nullable;

public interface BattleParticipantInventory extends BattleParticipantInventoryView {
    BattleParticipantEquipment getEquipment(BattleParticipantEquipmentSlot slot);

    @Nullable BattleParticipantInventoryHandle getInventoryHandle(BattleParticipantEquipmentSlot slot);

    @Nullable BattleParticipantItemStack take(BattleParticipantInventoryHandle handle, int amount, Tracer<ActionTrace> tracer);

    BattleParticipantInventoryHandle give(BattleParticipantItemStack stack, Tracer<ActionTrace> tracer);

    boolean equip(BattleParticipantInventoryHandle handle, BattleParticipantEquipmentSlot slot, Tracer<ActionTrace> tracer);

    boolean unequip(BattleParticipantEquipmentSlot slot, Tracer<ActionTrace> tracer);
}
