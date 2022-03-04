package io.github.stuff_stuffs.tbcexcore.common.api.battle.effect;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class BattleEffectTypes {
    public static final Registry<BattleEffectType<?, ?>> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<BattleEffectType<?, ?>>(RegistryKey.ofRegistry(TBCExCore.createId("battle_effect")), Lifecycle.stable(), BattleEffectType::getReference)).buildAndRegister();

    public static void init() {
    }

    private BattleEffectTypes() {
    }
}
