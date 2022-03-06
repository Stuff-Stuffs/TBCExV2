package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

import java.util.function.Function;

public final class BattleParticipantEquipmentType<T extends BattleParticipantEquipment> {
    private final Codec<T> codec;
    private final RegistryEntry.Reference<BattleParticipantEquipmentType<?>> reference;

    public BattleParticipantEquipmentType(final Codec<T> codec) {
        this.codec = codec;
        reference = BattleParticipantEquipmentTypes.REGISTRY.createEntry(this);
    }

    public <K> DataResult<BattleParticipantEquipment> decode(final DynamicOps<K> ops, final K val) {
        return codec.parse(ops, val).map(Function.identity());
    }

    public <K> DataResult<K> encode(final DynamicOps<K> ops, final BattleParticipantEquipment equipment) {
        if (equipment.getType() != this) {
            throw new TBCExException("Type mismatch");
        }
        return codec.encodeStart(ops, (T) equipment);
    }

    public RegistryEntry.Reference<BattleParticipantEquipmentType<?>> getReference() {
        return reference;
    }

    @Override
    public String toString() {
        final Identifier id = BattleParticipantEquipmentTypes.REGISTRY.getId(this);
        if (id == null) {
            return "Unregistered BattleParticipantEquipmentType";
        }
        return "BattleParticipantEquipmentType{" + id + "}";
    }
}
