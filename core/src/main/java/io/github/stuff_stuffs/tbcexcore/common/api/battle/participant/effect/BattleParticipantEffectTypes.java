package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.effect.LevelStatEffect;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.effect.RacialStatEffect;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class BattleParticipantEffectTypes {
    public static final Registry<BattleParticipantEffectType<?>> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<BattleParticipantEffectType<?>>(RegistryKey.ofRegistry(TBCExCore.createId("battle_effect")), Lifecycle.stable(), BattleParticipantEffectType::getReference)).buildAndRegister();
    public static final BattleParticipantEffectType<RacialStatEffect> RACIAL_STAT_BATTLE_PARTICIPANT_EFFECT = new BattleParticipantEffectType<>(new LiteralText("Racial"), RacialStatEffect.CODEC, RacialStatEffect::combine, RacialStatEffect::extract);
    public static final BattleParticipantEffectType<LevelStatEffect> LEVEL_STAT_BATTLE_PARTICIPANT_EFFECT = new BattleParticipantEffectType<>(new LiteralText("Level"), LevelStatEffect.CODEC, LevelStatEffect::combine, LevelStatEffect::extract);

    private BattleParticipantEffectTypes() {
    }
}
