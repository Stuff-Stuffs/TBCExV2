package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

public interface BattleItem {
    BattleItemType<?> getType();

    //MUST OVERRIDE
    @Override
    boolean equals(Object o);

    //MUST OVERRIDE
    @Override
    int hashCode();
}
