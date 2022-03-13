package io.github.stuff_stuffs.tbcexcore.client.render;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemTypes;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

import java.util.Map;

public final class TBCExCoreRenderRegistries {
    private static final Map<BattleParticipantItemType<?>, BattleParticipantItemRenderer> ITEM_RENDERERS = new Reference2ObjectOpenHashMap<>();
    private static boolean FROZEN = false;

    public static void addItemRenderer(final BattleParticipantItemType<?> type, final BattleParticipantItemRenderer renderer) {
        if (FROZEN) {
            throw new TBCExException("Tried to register BattleParticipantItemRenderer after registries were frozen, type: " + type);
        }
        if (ITEM_RENDERERS.put(type, renderer) != null) {
            throw new TBCExException("Duplicate item renderer for type: " + type);
        }
    }

    public static BattleParticipantItemRenderer getItemRenderer(final BattleParticipantItemType<?> type) {
        return ITEM_RENDERERS.get(type);
    }

    public static void freeze() {
        if (!FROZEN) {
            FROZEN = true;
            for (final BattleParticipantItemType<?> type : BattleParticipantItemTypes.REGISTRY) {
                if (getItemRenderer(type) == null) {
                    throw new TBCExException("Missing item renderer for type: " + type);
                }
            }
        }
    }

    private TBCExCoreRenderRegistries() {
    }
}
