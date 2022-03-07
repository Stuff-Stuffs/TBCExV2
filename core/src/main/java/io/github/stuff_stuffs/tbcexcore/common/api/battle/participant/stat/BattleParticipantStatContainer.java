package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.util.Map;

public final class BattleParticipantStatContainer {
    private static final BattleParticipantStatModifier.Phase[] PHASES = BattleParticipantStatModifier.Phase.values();
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

    public BattleParticipantStatModifierHandle addModifier(final BattleParticipantStat stat, final BattleParticipantStatModifier modifier, final BattleParticipantStatModifier.Phase phase, Tracer<ActionTrace> tracer) {
        return containers.get(stat).addModifier(modifier, phase, tracer);
    }

    static final class Container {
        private final BattleParticipantStat stat;
        private final Reference2ObjectLinkedOpenHashMap<BattleParticipantStatModifierHandle, BattleParticipantStatModifier>[] modifiers;

        private Container(final BattleParticipantStat stat) {
            this.stat = stat;

            modifiers = new Reference2ObjectLinkedOpenHashMap[PHASES.length];
            for (int i = 0; i < PHASES.length; i++) {
                modifiers[i] = new Reference2ObjectLinkedOpenHashMap<>();
            }
        }

        void destroy(final BattleParticipantStatModifierHandle handle, Tracer<ActionTrace> tracer) {
            for (final Reference2ObjectLinkedOpenHashMap<BattleParticipantStatModifierHandle, BattleParticipantStatModifier> map : modifiers) {
                map.remove(handle);
            }
        }

        public double compute() {
            double t = 0;
            for (final Reference2ObjectLinkedOpenHashMap<BattleParticipantStatModifierHandle, BattleParticipantStatModifier> map : modifiers) {
                for (final BattleParticipantStatModifier modifier : map.values()) {
                    t = modifier.modify(t, stat);
                }
            }
            return t;
        }

        public BattleParticipantStatModifierHandle addModifier(final BattleParticipantStatModifier modifier, final BattleParticipantStatModifier.Phase phase, Tracer<ActionTrace> tracer) {
            final BattleParticipantStatModifierHandle handle = new BattleParticipantStatModifierHandle(this);
            modifiers[phase.ordinal()].putAndMoveToLast(handle, modifier);
            return handle;
        }
    }
}
