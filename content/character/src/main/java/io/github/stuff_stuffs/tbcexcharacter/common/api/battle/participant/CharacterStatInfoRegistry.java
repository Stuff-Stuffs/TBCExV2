package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

import java.util.Map;

public final class CharacterStatInfoRegistry {
    private static final Map<BattleParticipantStat, StatInfo> INFO_MAP = new Reference2ObjectOpenHashMap<>();
    private static final StatInfo DEFAULT = new StatInfo(false);

    public static void register(final BattleParticipantStat stat, final boolean purchasable) {
        final StatInfo statInfo = new StatInfo(purchasable);
        if (INFO_MAP.put(stat, statInfo) != null) {
            throw new TBCExException("Tried to register a stat info twice with key " + stat);
        }
    }

    public static StatInfo getStatInfo(final BattleParticipantStat stat) {
        return INFO_MAP.getOrDefault(stat, DEFAULT);
    }

    public static final class StatInfo {
        private final boolean purchasable;

        private StatInfo(final boolean purchasable) {
            this.purchasable = purchasable;
        }

        public boolean isPurchasable() {
            return purchasable;
        }
    }

    private CharacterStatInfoRegistry() {
    }
}
