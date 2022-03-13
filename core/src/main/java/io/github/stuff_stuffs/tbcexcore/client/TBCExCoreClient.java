package io.github.stuff_stuffs.tbcexcore.client;

import io.github.stuff_stuffs.tbcexcore.client.network.BattlePlayerUpdateReceiver;
import io.github.stuff_stuffs.tbcexcore.client.network.BattleUpdateReceiver;
import io.github.stuff_stuffs.tbcexcore.client.network.BattleUpdateRequestSender;
import io.github.stuff_stuffs.tbcexcore.client.render.TBCExCoreRenderRegistries;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleWorld;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattlePlayerEntity;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class TBCExCoreClient implements ClientModInitializer {
    private static final Map<UUID, BattleHandle> BATTLE_BY_PLAYER = new Object2ObjectOpenHashMap<>();
    private static final List<Consumer<WorldRenderContext>> RENDER_PRIMITIVES = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        BattleUpdateReceiver.init();
        BattlePlayerUpdateReceiver.init();
        ClientPlayConnectionEvents.INIT.register((handler, client) -> TBCExCoreRenderRegistries.freeze());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            BATTLE_BY_PLAYER.clear();
            RENDER_PRIMITIVES.clear();
        });
        ClientTickEvents.START_WORLD_TICK.register(world -> {
            for (final AbstractClientPlayerEntity player : world.getPlayers()) {
                final BattleHandle handle = BATTLE_BY_PLAYER.get(player.getUuid());
                ((BattlePlayerEntity) player).tbcex$setCurrentBattle(handle);
            }
            final BattleHandle handle = ((BattlePlayerEntity) MinecraftClient.getInstance().player).tbcex$getCurrentBattle();
            if(handle !=null) {
                final BattleWorld battleWorld = ((BattleWorldHolder) world).tbcex$getBattleWorld();
                battleWorld.getBattle(handle);
            }
        });
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            final MatrixStack matrices = context.matrixStack();
            matrices.push();
            for (final Consumer<WorldRenderContext> renderPrimitive : RENDER_PRIMITIVES) {
                renderPrimitive.accept(context);
            }
            matrices.pop();
            RENDER_PRIMITIVES.clear();
        });
    }

    public static void addRenderPrimitive(final Consumer<WorldRenderContext> primitive) {
        RENDER_PRIMITIVES.add(primitive);
    }

    public static void setBattle(final UUID uuid, @Nullable final BattleHandle handle) {
        if (handle == null) {
            BATTLE_BY_PLAYER.remove(uuid);
        } else {
            BATTLE_BY_PLAYER.put(uuid, handle);
        }
    }

    public static @Nullable BattleHandle getBattle(final UUID uuid) {
        return BATTLE_BY_PLAYER.getOrDefault(uuid, null);
    }
}
