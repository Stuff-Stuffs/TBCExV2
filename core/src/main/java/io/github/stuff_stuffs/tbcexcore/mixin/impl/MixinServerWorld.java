package io.github.stuff_stuffs.tbcexcore.mixin.impl;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleWorld;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.world.ServerBattleWorldImpl;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.Spawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class MixinServerWorld implements BattleWorldHolder {
    private ServerBattleWorldImpl battleWorld;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(final MinecraftServer server, final Executor workerExecutor, final LevelStorage.Session session, final ServerWorldProperties properties, final RegistryKey<World> worldKey, final RegistryEntry<DimensionType> registryEntry, final WorldGenerationProgressListener worldGenerationProgressListener, final ChunkGenerator chunkGenerator, final boolean debugWorld, final long seed, final List<Spawner> spawners, final boolean shouldTickTime, final CallbackInfo ci) {
        final Path worldDirectory = session.getWorldDirectory(worldKey);
        final Path battleWorldDirectory = worldDirectory.resolve("tbcex_battle_world");
        battleWorld = new ServerBattleWorldImpl(battleWorldDirectory, worldKey);
    }

    @Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At("HEAD"))
    private void tickInject(final BooleanSupplier booleanSupplier, final CallbackInfo ci) {
        battleWorld.tick();
    }

    @Inject(method = "save", at = @At("HEAD"))
    private void saveInject(ProgressListener progressListener, boolean flush, boolean savingDisabled, CallbackInfo ci) {
        battleWorld.save();
    }

    @Override
    public BattleWorld tbcex$getBattleWorld() {
        return battleWorld;
    }
}
