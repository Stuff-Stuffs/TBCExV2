package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class BattleParticipantEquipmentSlots {
    public static final Registry<BattleParticipantEquipmentSlot> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<>(RegistryKey.ofRegistry(TBCExCore.createId("battle_equipment_slot")), Lifecycle.stable(), BattleParticipantEquipmentSlot::getReference)).buildAndRegister();

    public static void init() {
    }

    private BattleParticipantEquipmentSlots() {
    }
}
