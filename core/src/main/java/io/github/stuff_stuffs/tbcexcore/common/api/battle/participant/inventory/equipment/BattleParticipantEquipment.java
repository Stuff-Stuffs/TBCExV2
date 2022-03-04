package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantEquipmentSlot;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;

import java.util.Set;

public interface BattleParticipantEquipment {
    Codec<BattleParticipantEquipment> CODEC = CodecUtil.createDependentPairCodecFirst(BattleParticipantEquipmentTypes.REGISTRY.getCodec(), new CodecUtil.DependentEncoder<>() {
        @Override
        public <T> DataResult<T> encode(BattleParticipantEquipmentType<?> coValue, BattleParticipantEquipment value, DynamicOps<T> ops) {
            return coValue.encode(ops, value);
        }
    }, new CodecUtil.DependentDecoder<>() {
        @Override
        public <T> DataResult<BattleParticipantEquipment> decode(BattleParticipantEquipmentType<?> coValue, T value, DynamicOps<T> ops) {
            return coValue.decode(ops, value);
        }
    }, BattleParticipantEquipment::getType);

    BattleParticipantEquipmentType<?> getType();

    Set<BattleParticipantEquipmentSlot> getEquipableSlots();

    Set<BattleParticipantEquipmentSlot> getBlockedSlots(BattleParticipantEquipmentSlot slot);

    void init(BattleParticipantEquipmentSlot slot, BattleParticipantState state);

    void deinit();
}
