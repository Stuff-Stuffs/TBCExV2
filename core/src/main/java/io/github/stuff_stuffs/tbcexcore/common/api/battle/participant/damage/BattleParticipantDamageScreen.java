package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
import it.unimi.dsi.fastutil.objects.Reference2DoubleOpenHashMap;

import java.util.Map;

public final class BattleParticipantDamageScreen {
    public static final Codec<BattleParticipantDamageScreen> CODEC = Codec.unboundedMap(BattleParticipantDamageTypes.REGISTRY.getCodec(), Codec.DOUBLE).xmap(BattleParticipantDamageScreen::new, screen -> screen.screenByType);
    private final Reference2DoubleMap<BattleParticipantDamageType> screenByType;

    private BattleParticipantDamageScreen(final Reference2DoubleMap<BattleParticipantDamageType> screenByType) {
        this.screenByType = screenByType;
    }

    private BattleParticipantDamageScreen(final Map<BattleParticipantDamageType, Double> screenByType) {
        this.screenByType = new Reference2DoubleOpenHashMap<>(screenByType);
    }

    public BattleParticipantDamagePacket screen(final BattleParticipantDamagePacket packet) {
        final BattleParticipantDamagePacket.Builder builder = BattleParticipantDamagePacket.builder();
        for (final BattleParticipantDamageType type : BattleParticipantDamageTypes.REGISTRY) {
            final long damage = packet.getDamageOfType(type);
            if (damage > 0) {
                double screenSum = 0;
                for (final BattleParticipantDamageType ancestor : type.getAncestors()) {
                    screenSum += screenByType.getOrDefault(ancestor, 0);
                    if (screenSum > 1) {
                        break;
                    }
                }
                screenSum = Math.min(screenSum, 1);
                final long amount = (long) (packet.getDamageOfType(type) * (1 - screenSum));
                if (amount > 0) {
                    builder.set(type, amount);
                }
            }
        }
        return builder.build();
    }
}
