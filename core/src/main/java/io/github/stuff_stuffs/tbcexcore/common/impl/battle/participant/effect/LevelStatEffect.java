package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.*;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.TBCExUtil;
import it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
import it.unimi.dsi.fastutil.objects.Reference2DoubleOpenHashMap;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevelStatEffect implements LevelStatEffectView {
    public static final Codec<LevelStatEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.unboundedMap(BattleParticipantStats.REGISTRY.getCodec(), Codec.DOUBLE).fieldOf("basics").forGetter(effect -> effect.basics)).apply(instance, LevelStatEffect::new));
    private final Reference2DoubleMap<BattleParticipantStat> basics;
    private final List<BattleParticipantStatModifierHandle> handles;

    private LevelStatEffect(final Map<BattleParticipantStat, Double> basics) {
        this.basics = new Reference2DoubleOpenHashMap<>(basics);
        handles = new ArrayList<>(basics.size());
    }

    @Override
    public BattleParticipantEffectType<?, ?> getType() {
        return BattleParticipantEffectTypes.LEVEL_STAT_BATTLE_PARTICIPANT_EFFECT;
    }

    @Override
    public void init(final BattleParticipantState state) {
        final BattleParticipantStatContainer container = state.getStatContainer();
        for (final Reference2DoubleMap.Entry<BattleParticipantStat> entry : basics.reference2DoubleEntrySet()) {
            final double v = entry.getDoubleValue();
            handles.add(container.addModifier(entry.getKey(), (current, stat) -> current + v, BattleParticipantStatModifier.Phase.ADDERS));
        }
    }

    @Override
    public void deinit() {
        handles.forEach(BattleParticipantStatModifierHandle::destroy);
        handles.clear();
    }

    public static LevelStatEffect combine(final LevelStatEffect first, final LevelStatEffect second) {
        return TBCExUtil.unimplemented();
    }

    public static <E extends Entity & BattleParticipant> LevelStatEffect extract(final E entity) {
        return TBCExUtil.unimplemented();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Reference2DoubleMap<BattleParticipantStat> basics = new Reference2DoubleOpenHashMap<>();

        public Builder set(final BattleParticipantStat stat, final double base) {
            basics.put(stat, base);
            return this;
        }

        public LevelStatEffect build() {
            return new LevelStatEffect(basics);
        }
    }
}
