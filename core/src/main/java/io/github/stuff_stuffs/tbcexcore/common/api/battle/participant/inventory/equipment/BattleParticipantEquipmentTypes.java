package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class BattleParticipantEquipmentTypes {
    public static final Registry<BattleParticipantEquipmentType<?>> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<BattleParticipantEquipmentType<?>>(RegistryKey.ofRegistry(TBCExCore.createId("battle_equipment_type")), Lifecycle.stable(), BattleParticipantEquipmentType::getReference)).buildAndRegister();

    public static void init() {
    }

    private BattleParticipantEquipmentTypes() {
    }
}
