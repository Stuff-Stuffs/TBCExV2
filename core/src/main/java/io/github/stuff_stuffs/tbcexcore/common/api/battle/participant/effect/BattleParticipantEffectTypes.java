package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class BattleParticipantEffectTypes {
    public static final Registry<BattleParticipantEffectType<?, ?>> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<BattleParticipantEffectType<?, ?>>(RegistryKey.ofRegistry(TBCExCore.createId("battle_participant_effect")), Lifecycle.stable(), BattleParticipantEffectType::getReference)).buildAndRegister();

    public static void init() {
    }

    private BattleParticipantEffectTypes() {
    }
}
