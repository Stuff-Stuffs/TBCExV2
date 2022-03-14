package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.inventory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantEquipmentSlot;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantEquipmentSlots;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventory;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventoryHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment.BattleParticipantEquipment;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemStack;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.EquippableBattleParticipantItem;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.item.BattleItem;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.PairIterator;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class BattleParticipantInventoryImpl implements BattleParticipantInventory {
    public static final Codec<BattleParticipantInventoryImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, BattleParticipantItemStack.CODEC).fieldOf("stacks").forGetter(BattleParticipantInventoryImpl::getCodedStacks),
            CodecUtil.createLinkedMapCodec(BattleParticipantEquipmentSlots.REGISTRY.getCodec(), Codec.LONG).fieldOf("handleBySlot").forGetter(inv -> inv.handleBySlot),
            Codec.unboundedMap(BattleParticipantEquipmentSlots.REGISTRY.getCodec(), BattleParticipantEquipment.CODEC).fieldOf("equipmentBySlot").forGetter(inv -> inv.equipmentBySlot),
            Codec.LONG.fieldOf("nextId").forGetter(inv -> inv.nextId)
    ).apply(instance, BattleParticipantInventoryImpl::new));
    private final Long2ObjectMap<BattleParticipantItemStack> stacks;
    private final Reference2LongLinkedOpenHashMap<BattleParticipantEquipmentSlot> handleBySlot;
    private final Map<BattleParticipantEquipmentSlot, BattleParticipantEquipment> equipmentBySlot;
    private long nextId;
    private BattleParticipantHandle handle;
    private BattleParticipantState state;
    private boolean init = false;

    public BattleParticipantInventoryImpl(final BattleParticipant participant) {
        stacks = new Long2ObjectOpenHashMap<>();
        handleBySlot = new Reference2LongLinkedOpenHashMap<>();
        equipmentBySlot = new Reference2ObjectOpenHashMap<>();
        nextId = 0;
        final PairIterator<ItemStack, Integer> iterator = participant.tbcex$getInventoryIterator();
        while (iterator.next()) {
            final ItemStack stack = iterator.getLeft();
            final int slot = iterator.getRight();
            if (stack.getItem() instanceof BattleItem battleItem) {
                final BattleParticipantItemStack participantItemStack = battleItem.toBattleParticipantItem(stack);
                stacks.put(slot, participantItemStack);
            }
        }
    }

    private BattleParticipantInventoryImpl(final Map<String, BattleParticipantItemStack> stacks, final Map<BattleParticipantEquipmentSlot, Long> handleBySlot, final Map<BattleParticipantEquipmentSlot, BattleParticipantEquipment> equipmentBySlot, final long nextId) {
        this.stacks = new Long2ObjectOpenHashMap<>();
        this.handleBySlot = new Reference2LongLinkedOpenHashMap<>();
        this.equipmentBySlot = new Reference2ObjectOpenHashMap<>();
        this.nextId = nextId;
        for (Map.Entry<String, BattleParticipantItemStack> entry : stacks.entrySet()) {
            this.stacks.put(Long.parseLong(entry.getKey()), entry.getValue());
        }
        for (final Map.Entry<BattleParticipantEquipmentSlot, Long> entry : handleBySlot.entrySet()) {
            this.handleBySlot.putAndMoveToLast(entry.getKey(), entry.getValue());
        }
        this.equipmentBySlot.putAll(equipmentBySlot);
    }

    private Map<String, BattleParticipantItemStack> getCodedStacks() {
        Map<String, BattleParticipantItemStack> coded = new Object2ReferenceOpenHashMap<>(stacks.size());
        for (Long2ObjectMap.Entry<BattleParticipantItemStack> entry : stacks.long2ObjectEntrySet()) {
            coded.put(Long.toString(entry.getLongKey()), entry.getValue());
        }
        return coded;
    }

    public void init(final BattleParticipantHandle handle, final BattleParticipantState state, Tracer<ActionTrace> tracer) {
        if (!init) {
            this.handle = handle;
            this.state = state;
            init = true;
            for (final BattleParticipantEquipmentSlot slot : handleBySlot.keySet()) {
                equipmentBySlot.get(slot).init(slot, state, tracer);
            }
        }
    }

    public void deinit(Tracer<ActionTrace> tracer) {
        for (BattleParticipantEquipmentSlot slot : handleBySlot.keySet()) {
            equipmentBySlot.get(slot).deinit(tracer);
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
        final long key = handleBySlot.getOrDefault(slot, Long.MIN_VALUE);
        if (key == Long.MIN_VALUE) {
            return null;
        }
        return new BattleParticipantInventoryHandle(handle, key);
    }

    @Override
    public @Nullable BattleParticipantItemStack take(final BattleParticipantInventoryHandle handle, final int amount, final Tracer<ActionTrace> tracer) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        if (!handle.getParent().equals(this.handle)) {
            throw new TBCExException("BattleParticipantHandle mismatch");
        }
        if (isEquipped(handle)) {
            return null;
        }
        final BattleParticipantItemStack stack = stacks.get(handle.getId());
        if (stack == null) {
            return null;
        }
        final int max = stack.getCount();
        if (max < amount) {
            stacks.remove(handle.getId());
            return stack;
        } else {
            final BattleParticipantItemStack split = new BattleParticipantItemStack(stack.getItem(), stack.getCount() - amount);
            final BattleParticipantItemStack ret = new BattleParticipantItemStack(stack.getItem(), amount);
            stacks.put(handle.getId(), split);
            return ret;
        }
    }

    @Override
    public BattleParticipantInventoryHandle give(final BattleParticipantItemStack stack, final Tracer<ActionTrace> tracer) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        BattleParticipantInventoryHandle handle = null;
        for (final Long2ObjectMap.Entry<BattleParticipantItemStack> entry : stacks.long2ObjectEntrySet()) {
            if (entry.getValue().canCombine(stack)) {
                handle = new BattleParticipantInventoryHandle(this.handle, entry.getLongKey());
                break;
            }
        }
        if (handle != null) {
            stacks.put(handle.getId(), new BattleParticipantItemStack(stack.getItem(), stack.getCount() + stacks.get(handle.getId()).getCount()));
            return handle;
        }
        handle = new BattleParticipantInventoryHandle(this.handle, nextId++);
        stacks.put(handle.getId(), stack);
        return handle;
    }

    @Override
    public boolean equip(final BattleParticipantInventoryHandle handle, final BattleParticipantEquipmentSlot slot, final Tracer<ActionTrace> tracer) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        if (equipmentBySlot.get(slot) != null) {
            return false;
        }
        final BattleParticipantItemStack stack = stacks.get(handle.getId());
        if (stack == null || !(stack.getItem() instanceof EquippableBattleParticipantItem equippable)) {
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
        handleBySlot.put(slot, handle.getId());
        equipment.init(slot, state, tracer);
        return true;
    }

    @Override
    public boolean unequip(final BattleParticipantEquipmentSlot slot, final Tracer<ActionTrace> tracer) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        if (equipmentBySlot.get(slot) == null) {
            return false;
        }
        final boolean tryUnequip = true;
        if (tryUnequip) {
            final BattleParticipantEquipment equipment = equipmentBySlot.remove(slot);
            handleBySlot.removeLong(slot);
            equipment.deinit(tracer);
            return true;
        }
        return false;
    }

    @Override
    public BattleParticipantItemStack getStack(final BattleParticipantInventoryHandle handle) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        if (!handle.getParent().equals(this.handle)) {
            throw new TBCExException("BattleParticipantHandle mismatch");
        }
        return stacks.get(handle.getId());
    }

    @Override
    public boolean isEquipped(final BattleParticipantInventoryHandle handle) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        if (!handle.getParent().equals(this.handle)) {
            throw new TBCExException("BattleParticipantHandle mismatch");
        }
        return handleBySlot.containsValue(handle.getId());
    }

    @Override
    public boolean isEquipped(final BattleParticipantEquipmentSlot slot) {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        return equipmentBySlot.containsKey(slot);
    }

    @Override
    public PairIterator<BattleParticipantItemStack, BattleParticipantInventoryHandle> getIterator() {
        if (!init) {
            throw new TBCExException("Tried to access inventory before it was initialized");
        }
        final Map<BattleParticipantInventoryHandle, BattleParticipantItemStack> mapped = new Object2ReferenceOpenHashMap<>(stacks.size());
        for (final Long2ObjectMap.Entry<BattleParticipantItemStack> entry : stacks.long2ObjectEntrySet()) {
            mapped.put(new BattleParticipantInventoryHandle(handle, entry.getLongKey()), entry.getValue());
        }
        return PairIterator.fromMap(mapped).swap();
    }
}
