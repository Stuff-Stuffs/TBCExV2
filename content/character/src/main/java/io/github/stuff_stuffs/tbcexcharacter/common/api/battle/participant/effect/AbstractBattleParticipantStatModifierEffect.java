package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStatContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStatModifier;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStatModifierHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractBattleParticipantStatModifierEffect implements BattleParticipantEffect {
    protected final Map<BattleParticipantStat, List<Entry>> entryMap;
    private final List<BattleParticipantStatModifierHandle> handles;

    protected AbstractBattleParticipantStatModifierEffect(final Map<BattleParticipantStat, List<Entry>> entryMap) {
        final Map<BattleParticipantStat, List<Entry>> map = new Reference2ObjectOpenHashMap<>(entryMap);
        final List<BattleParticipantStat> toRemove = new ArrayList<>();
        for (final Map.Entry<BattleParticipantStat, List<Entry>> entry : map.entrySet()) {
            if (entry.getValue().isEmpty()) {
                toRemove.add(entry.getKey());
            }
        }
        for (final BattleParticipantStat stat : toRemove) {
            map.remove(stat);
        }
        this.entryMap = Collections.unmodifiableMap(map);
        handles = new ArrayList<>();
    }

    @Override
    public void init(final BattleParticipantState state, final Tracer<ActionTrace> tracer) {
        final BattleParticipantStatContainer statContainer = state.getStatContainer();
        for (final Map.Entry<BattleParticipantStat, List<Entry>> entry : entryMap.entrySet()) {
            for (final Entry statEntry : entry.getValue()) {
                handles.add(statContainer.addModifier(entry.getKey(), statEntry.createModifier(), statEntry.phase(), tracer));
            }
        }
    }

    @Override
    public void deinit(final Tracer<ActionTrace> tracer) {
        for (final BattleParticipantStatModifierHandle handle : handles) {
            handle.destroy(tracer);
        }
        handles.clear();
    }

    public record Entry(
            double value,
            AbstractBattleParticipantStatModifierEffect.Entry.Op op,
            BattleParticipantStatModifier.Phase phase
    ) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Codec.DOUBLE.fieldOf("value").forGetter(Entry::value),
                        Op.CODEC.fieldOf("op").forGetter(Entry::op),
                        BattleParticipantStatModifier.Phase.CODEC.fieldOf("phase").forGetter(Entry::phase)
                ).apply(instance, Entry::new)
        );

        public BattleParticipantStatModifier createModifier() {
            return switch (op) {
                case ADD -> BattleParticipantStatModifier.add(value);
                case MULTIPLY -> BattleParticipantStatModifier.multiplier(value);
            };
        }

        public enum Op {
            ADD,
            MULTIPLY;
            public static final Codec<Op> CODEC = CodecUtil.createEnumCodec(Op.class);
        }
    }
}
