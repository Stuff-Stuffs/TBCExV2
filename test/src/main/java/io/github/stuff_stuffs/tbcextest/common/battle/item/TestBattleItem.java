package io.github.stuff_stuffs.tbcextest.common.battle.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItem;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemCategory;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemRarity;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemType;
import io.github.stuff_stuffs.tbcextest.common.TBCExTest;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Set;

public class TestBattleItem implements BattleParticipantItem {
    public static final Codec<TestBattleItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("name").forGetter(item -> item.name), Codec.STRING.xmap(Identifier::new, Identifier::toString).fieldOf("categoryId").forGetter(item -> item.categoryId), BattleParticipantItemRarity.CODEC.fieldOf("rarity").forGetter(item -> item.rarity)).apply(instance, TestBattleItem::new));
    private final String name;
    private final Identifier categoryId;
    private final BattleParticipantItemRarity rarity;

    public TestBattleItem(final String name, final Identifier categoryId, final BattleParticipantItemRarity rarity) {
        this.name = name;
        this.categoryId = categoryId;
        this.rarity = rarity;
    }

    @Override
    public Text getName() {
        return new LiteralText(name);
    }

    @Override
    public BattleParticipantItemType<?> getType() {
        return TBCExTest.TEST_BATTLE_ITEM_TYPE;
    }

    @Override
    public Set<BattleParticipantItemCategory> getCategory() {
        return Set.of(BattleParticipantItemCategory.get(categoryId));
    }

    @Override
    public BattleParticipantItemRarity getRarity() {
        return rarity;
    }

    @Override
    public List<ItemStack> convert(final int count) {
        return List.of(new ItemStack(TBCExTest.TEST_ITEMS[0], count));
    }
}
