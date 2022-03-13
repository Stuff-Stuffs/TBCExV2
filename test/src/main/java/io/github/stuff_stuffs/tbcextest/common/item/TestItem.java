package io.github.stuff_stuffs.tbcextest.common.item;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemCategory;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemRarity;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemStack;
import io.github.stuff_stuffs.tbcexcore.common.item.BattleItem;
import io.github.stuff_stuffs.tbcextest.common.battle.item.TestBattleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class TestItem extends Item implements BattleItem {
    private final int seed;

    public TestItem(final Settings settings, final int seed) {
        super(settings);
        this.seed = seed;
    }

    @Override
    public BattleParticipantItemStack toBattleParticipantItem(final ItemStack stack) {
        final Collection<BattleParticipantItemCategory> allCategories = BattleParticipantItemCategory.getAllCategories();
        final Random random = new Random(seed);
        int i = random.nextInt(allCategories.size());
        final Iterator<BattleParticipantItemCategory> iterator = allCategories.iterator();
        while (i > 0) {
            iterator.next();
            i--;
        }
        final BattleParticipantItemRarity.RarityClass[] values = BattleParticipantItemRarity.RarityClass.values();
        final BattleParticipantItemRarity.RarityClass rarityClass = values[random.nextInt(values.length)];
        final TestBattleItem battleItem = new TestBattleItem("TestItem#" + seed, BattleParticipantItemCategory.getId(iterator.next()), new BattleParticipantItemRarity(random.nextDouble(), rarityClass));
        return new BattleParticipantItemStack(battleItem, stack.getCount());
    }
}
