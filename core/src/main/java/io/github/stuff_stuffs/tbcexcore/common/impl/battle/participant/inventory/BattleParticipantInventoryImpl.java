package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.inventory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantEquipmentSlot;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantEquipmentSlots;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventory;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventoryHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment.BattleParticipantEquipment;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleItemStack;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.EquippableBattleItem;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.PairIterator;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class BattleParticipantInventoryImpl implements BattleParticipantInventory {
    public static final Codec<BattleParticipantInventoryImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.unboundedMap(BattleParticipantInventoryHandle.CODEC, BattleItemStack.CODEC).fieldOf("stacks").forGetter(inv -> inv.stacks), CodecUtil.createLinkedMapCodec(BattleParticipantEquipmentSlots.REGISTRY.getCodec(), BattleParticipantInventoryHandle.CODEC).fieldOf("handleBySlot").forGetter(inv -> inv.handleBySlot), Codec.unboundedMap(BattleParticipantEquipmentSlots.REGISTRY.getCodec(), BattleParticipantEquipment.CODEC).fieldOf("equipmentBySlot").forGetter(inv -> inv.equipmentBySlot), Codec.LONG.fieldOf("nextId").forGetter(inv -> inv.nextId)).apply(instance, BattleParticipantInventoryImpl::new));
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

    private BattleParticipantInventoryImpl(final Map<BattleParticipantInventoryHandle, BattleItemStack> stacks, final Map<BattleParticipantEquipmentSlot, BattleParticipantInventoryHandle> handleBySlot, final Map<BattleParticipantEquipmentSlot, BattleParticipantEquipment> equipmentBySlot, final long nextId) {
        this.stacks = new Object2ReferenceOpenHashMap<>();
        this.handleBySlot = new Reference2ObjectLinkedOpenHashMap<>();
        this.equipmentBySlot = new Reference2ObjectOpenHashMap<>();
        this.nextId = nextId;
        this.stacks.putAll(stacks);
        for (final Map.Entry<BattleParticipantEquipmentSlot, BattleParticipantInventoryHandle> entry : handleBySlot.entrySet()) {
            this.handleBySlot.putAndMoveToLast(entry.getKey(), entry.getValue());
        }
        this.equipmentBySlot.putAll(equipmentBySlot);
    }

    public void init(final BattleParticipantHandle handle, final BattleParticipantState state) {
        if (!init) {
            this.handle = handle;
            this.state = state;
            init = true;
            for (final BattleParticipantEquipmentSlot slot : handleBySlot.keySet()) {
                equipmentBySlot.get(slot).init(slot, state);
            }
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
        if (!handle.getParent().equals(this.handle)) {
            throw new TBCExException("BattleParticipantHandle mismatch");
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
    public BattleParticipantInventoryHandle give(final BattleItemStack stack) {
        BattleParticipantInventoryHandle handle = null;
        for (final Map.Entry<BattleParticipantInventoryHandle, BattleItemStack> entry : stacks.entrySet()) {
            if (entry.getValue().canCombine(stack)) {
                handle = entry.getKey();
                break;
            }
        }
        if (handle != null) {
            stacks.put(handle, new BattleItemStack(stack.getItem(), stack.getCount() + stacks.get(handle).getCount()));
            return handle;
        }
        handle = new BattleParticipantInventoryHandle(this.handle, nextId++);
        stacks.put(handle, stack);
        return handle;
    }

    @Override
    public boolean equip(final BattleParticipantInventoryHandle handle, final BattleParticipantEquipmentSlot slot) {
        if (equipmentBySlot.get(slot) != null) {
            return false;
        }
        final BattleItemStack stack = stacks.get(handle);
        if (stack == null || !(stack.getItem() instanceof EquippableBattleItem equippable)) {
            return false;
        }
        final BattleParticipantEquipment equipment = equippable.createEquipment(state);
        if (!equipment.getEquipableSlots().contains(slot)) {
            return false;
        }
        final Set<BattleParticipantEquipmentSlot> blockedSlots = equipment.getBlockedSlots(slot);
        for (final BattleParticipantEquipmentSlot blockedSlot : blockedSlots) {
            if (equipmentBySlot.get(blockedSlot) != null) {
                return false;
            }
        }
        equipmentBySlot.put(slot, equipment);
        handleBySlot.put(slot, handle);
        equipment.init(slot, state);
        return true;
    }

    @Override
    public boolean unequip(final BattleParticipantEquipmentSlot slot) {
        if (equipmentBySlot.get(slot) == null) {
            return false;
        }
        final boolean tryUnequip = true;
        if (tryUnequip) {
            final BattleParticipantEquipment equipment = equipmentBySlot.remove(slot);
            handleBySlot.remove(slot);
            equipment.deinit();
            return true;
        }
        return false;
    }

    @Override
    public BattleItemStack getStack(final BattleParticipantInventoryHandle handle) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        if (!handle.getParent().equals(this.handle)) {
            throw new TBCExException("BattleParticipantHandle mismatch");
        }
        return stacks.get(handle);
    }

    @Override
    public boolean isEquipped(final BattleParticipantInventoryHandle handle) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        if (!handle.getParent().equals(this.handle)) {
            throw new TBCExException("BattleParticipantHandle mismatch");
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
