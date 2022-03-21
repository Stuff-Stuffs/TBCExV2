package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant;

import io.github.stuff_stuffs.tbcexcharacter.common.TBCExCharacter;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemCategory;
import net.minecraft.text.LiteralText;

public final class CharacterBattleParticipantItemCategories {
    public static final BattleParticipantItemCategory HELMETS = BattleParticipantItemCategory.create(TBCExCharacter.createId("helmets"), new LiteralText("Helmets"));
    public static final BattleParticipantItemCategory CHEST_PLATES = BattleParticipantItemCategory.create(TBCExCharacter.createId("chest_plates"), new LiteralText("Chestplates"));
    public static final BattleParticipantItemCategory LEGGINGS = BattleParticipantItemCategory.create(TBCExCharacter.createId("leggings"), new LiteralText("Leggings"));
    public static final BattleParticipantItemCategory BOOTS = BattleParticipantItemCategory.create(TBCExCharacter.createId("boots"), new LiteralText("Boots"));

    public static void init() {
    }

    private CharacterBattleParticipantItemCategories() {
    }
}
