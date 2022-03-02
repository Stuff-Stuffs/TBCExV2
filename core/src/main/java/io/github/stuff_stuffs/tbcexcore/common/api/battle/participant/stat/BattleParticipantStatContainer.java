package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat;

import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.util.Map;

public final class BattleParticipantStatContainer {
    private final Map<BattleParticipantStat, Container> containers;

    public BattleParticipantStatContainer() {
        containers = new Reference2ReferenceOpenHashMap<>();
        for (final BattleParticipantStat stat : BattleParticipantStats.REGISTRY) {
            containers.put(stat, new Container(stat));
        }
    }

    public double getStat(final BattleParticipantStat stat) {
        return containers.get(stat).compute();
    }

    public BattleParticipantStatModifierHandle addModifier(final BattleParticipantStat stat, final BattleParticipantStatModifier modifier) {
        return containers.get(stat).addModifier(modifier);
    }

    static final class Container {
        private final BattleParticipantStat stat;
        private final Reference2ObjectLinkedOpenHashMap<BattleParticipantStatModifierHandle, BattleParticipantStatModifier> modifiers;

        private Container(final BattleParticipantStat stat) {
            this.stat = stat;
            modifiers = new Reference2ObjectLinkedOpenHashMap<>();
        }

        void destroy(final BattleParticipantStatModifierHandle handle) {
            modifiers.remove(handle);
        }

        public double compute() {
            double t = 0;
            for (final BattleParticipantStatModifier modifier : modifiers.values()) {
                t = modifier.modify(t, stat);
            }
            return t;
        }

        public BattleParticipantStatModifierHandle addModifier(final BattleParticipantStatModifier modifier) {
            final BattleParticipantStatModifierHandle handle = new BattleParticipantStatModifierHandle(this, modifier);
            modifiers.putAndMoveToLast(handle, modifier);
            return handle;
        }
    }
}
