package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.colour.Colour;
import io.github.stuff_stuffs.tbcexutil.common.colour.IntRgbColour;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.text.DecimalFormat;
import java.util.Comparator;

public final class BattleParticipantItemRarity {
    public static final Comparator<BattleParticipantItemRarity> COMPARATOR = Comparator.<BattleParticipantItemRarity, RarityClass>comparing(i -> i.rarityClass).thenComparingDouble(i -> i.progress);
    public static final Codec<BattleParticipantItemRarity> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.DOUBLE.fieldOf("progress").forGetter(rarity -> rarity.progress), RarityClass.CODEC.fieldOf("class").forGetter(rarity -> rarity.rarityClass)).apply(instance, BattleParticipantItemRarity::new));
    private static final DecimalFormat RARITY_FORMAT = new DecimalFormat("0.00");
    private static final RarityClass[] RARITY_CLASSES = RarityClass.values();
    private final double progress;
    private final RarityClass rarityClass;

    public BattleParticipantItemRarity(final double progress, final RarityClass rarityClass) {
        this.progress = progress;
        this.rarityClass = rarityClass;
        if (Double.isNaN(progress)) {
            throw new TBCExException("NaN rarity progress");
        }
        if (rarityClass != RarityClass.JUNK) {
            if (progress < 0) {
                throw new TBCExException("Less than zero rarity progress");
            }
        }
        if (rarityClass != RarityClass.LEGENDARY) {
            if (progress > 1) {
                throw new TBCExException("Rarity progress greater than one");
            }
        }
    }

    public RarityClass getRarityClass() {
        return rarityClass;
    }

    public double getProgress() {
        return progress;
    }

    public Text getAsText() {
        return new LiteralText(rarityClass.name());
    }

    public enum RarityClass {
        JUNK(0, new IntRgbColour(0x696A6A)),//grey
        COMMON(100.0, new IntRgbColour(0xFFFFFF)),//white
        UNCOMMON(10_000.0, new IntRgbColour(0xd18f9c)),//pink
        RARE(1_000_000.0, new IntRgbColour(0x00cc66)),//green
        EPIC(100_000_000.0, new IntRgbColour(0xE80000)),//red
        LEGENDARY(10_000_000_000.0, new IntRgbColour(0xFBF236));//gold
        public static final Codec<RarityClass> CODEC = Codec.STRING.comapFlatMap(RarityClass::fromDynamic, Enum::name);
        private final double start;
        private final Colour colour;

        RarityClass(final double start, Colour colour) {
            this.start = start;
            this.colour = colour;
        }

        public Colour getColour() {
            return colour;
        }

        private static DataResult<RarityClass> fromDynamic(final String s) {
            return switch (s) {
                case "JUNK" -> DataResult.success(JUNK);
                case "COMMON" -> DataResult.success(COMMON);
                case "UNCOMMON" -> DataResult.success(UNCOMMON);
                case "RARE" -> DataResult.success(RARE);
                case "EPIC" -> DataResult.success(EPIC);
                case "LEGENDARY" -> DataResult.success(LEGENDARY);
                default -> DataResult.error("No RarityClass with name: " + s);
            };
        }
    }

    public static BattleParticipantItemRarity getRarity(final double rarity) {
        if (rarity < 0) {
            throw new TBCExException("Negative rarity");
        }
        RarityClass greatestLessThan = null;
        int idx = -1;
        for (int i = 1; i < RARITY_CLASSES.length; i++) {
            if (RARITY_CLASSES[i].start > rarity) {
                idx = i;
                greatestLessThan = RARITY_CLASSES[i];
                break;
            }
        }
        if (greatestLessThan != null) {
            final RarityClass lessThan = RARITY_CLASSES[idx - 1];
            final double diff = greatestLessThan.start - lessThan.start;
            return new BattleParticipantItemRarity((rarity - lessThan.start) / diff, lessThan);
        } else {
            return new BattleParticipantItemRarity((rarity - RarityClass.LEGENDARY.start) / 1000.0, RarityClass.LEGENDARY);
        }
    }
}
