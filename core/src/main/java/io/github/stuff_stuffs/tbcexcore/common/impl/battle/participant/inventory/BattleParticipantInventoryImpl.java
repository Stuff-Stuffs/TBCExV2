package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.inventory;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantEquipmentSlot;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventory;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventoryHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment.BattleParticipantEquipment;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleItemStack;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.PairIterator;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BattleParticipantInventoryImpl implements BattleParticipantInventory {
    private final Map<BattleParticipantInventoryHandle, BattleItemStack> stacks;
    private final Reference2ObjectLinkedOpenHashMap<BattleParticipantEquipmentSlot, BattleParticipantInventoryHandle> handleBySlot;
    private final Map<BattleParticipantEquipmentSlot, BattleParticipantEquipment> equipmentBySlot;
    private long nextId;
    private BattleParticipantHandle handle;
    private BattleParticipantState state;
    private boolean init = false;

    public BattleParticipantInventoryImpl() {
        stacks = new Object2ReferenceOpenHashMap<>();
        handleBySlot = new Reference2ObjectLinkedOpenHashMap<>();
        equipmentBySlot = new Reference2ObjectOpenHashMap<>();
        nextId = 0;
    }

    public void init(final BattleParticipantHandle handle, final BattleParticipantState state) {
        if (!init) {
            this.handle = handle;
            this.state = state;
            init = true;
        }
    }

    @Override
    public BattleParticipantEquipment getEquipment(final BattleParticipantEquipmentSlot slot) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        return equipmentBySlot.get(slot);
    }

    @Override
    public BattleParticipantInventoryHandle getInventoryHandle(final BattleParticipantEquipmentSlot slot) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        return handleBySlot.get(slot);
    }

    @Override
    public @Nullable BattleItemStack take(final BattleParticipantInventoryHandle handle, final int amount) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        if (isEquipped(handle)) {
            return null;
        }
        final BattleItemStack stack = stacks.get(handle);
        if (stack == null) {
            return null;
        }
        final int max = stack.getCount();
        if (max < amount) {
            stacks.remove(handle);
            return stack;
        } else {
            final BattleItemStack split = new BattleItemStack(stack.getItem(), stack.getCount() - amount);
            final BattleItemStack ret = new BattleItemStack(stack.getItem(), amount);
            stacks.put(handle, split);
            return ret;
        }
    }

    @Override
    public BattleParticipantInventoryHandle give(BattleItemStack stack) {
        BattleParticipantInventoryHandle handle = null;
        for (Map.Entry<BattleParticipantInventoryHandle, BattleItemStack> entry : stacks.entrySet()) {
            if(entry.getValue().canCombine(stack)) {
                handle = entry.getKey();
                break;
            }
        }
        if(handle!=null) {
            stacks.put(handle, new BattleItemStack(stack.getItem(), stack.getCount() + stacks.get(handle).getCount()));
            return handle;
        }
        handle = new BattleParticipantInventoryHandle(this.handle, nextId++);
        stacks.put(handle, stack);
        return handle;
    }

    @Override
    public BattleItemStack getStack(final BattleParticipantInventoryHandle handle) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        return stacks.get(handle);
    }

    @Override
    public boolean isEquipped(final BattleParticipantInventoryHandle handle) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        return handleBySlot.containsValue(handle);
    }

    @Override
    public boolean isEquipped(final BattleParticipantEquipmentSlot slot) {
        return equipmentBySlot.containsKey(slot);
    }

    @Override
    public PairIterator<BattleItemStack, BattleParticipantInventoryHandle> getIterator() {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        return PairIterator.fromMap(stacks).swap();
    }
}
