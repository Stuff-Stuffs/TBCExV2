package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class BattleParticipantItemTypes {
    public static final Registry<BattleParticipantItemType<?>> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<BattleParticipantItemType<?>>(RegistryKey.ofRegistry(TBCExCore.createId("battle_item_type")), Lifecycle.stable(), BattleParticipantItemType::getReference)).buildAndRegister();

    public static void init() {
    }

    private BattleParticipantItemTypes() {
    }
}
