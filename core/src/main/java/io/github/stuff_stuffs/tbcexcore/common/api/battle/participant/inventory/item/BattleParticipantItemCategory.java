package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Collections;

public final class BattleParticipantItemCategory {
    private static final BiMap<Identifier, BattleParticipantItemCategory> CATEGORY_MAP = HashBiMap.create();
    private final Text name;

    private BattleParticipantItemCategory(final Text name) {
        this.name = name;
    }

    public static BattleParticipantItemCategory get(final Identifier identifier) {
        return CATEGORY_MAP.get(identifier);
    }

    public static Identifier getId(final BattleParticipantItemCategory category) {
        return CATEGORY_MAP.inverse().get(category);
    }

    public Text getName() {
        return name;
    }

    public static BattleParticipantItemCategory create(final Identifier identifier, final Text name) {
        final BattleParticipantItemCategory category = new BattleParticipantItemCategory(name);
        if (CATEGORY_MAP.put(identifier, category) != null) {
            throw new TBCExException("Duplicate battle item categories!");
        }
        return category;
    }

    public static Collection<BattleParticipantItemCategory> getAllCategories() {
        return Collections.unmodifiableCollection(CATEGORY_MAP.values());
    }
}
