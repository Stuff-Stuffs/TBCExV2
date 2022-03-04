package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class BattleItemTypes {
    public static final Registry<BattleItemType<?>> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<BattleItemType<?>>(RegistryKey.ofRegistry(TBCExCore.createId("battle_item_type")), Lifecycle.stable(), BattleItemType::getReference)).buildAndRegister();

    public static void init() {
    }

    private BattleItemTypes() {
    }
}
