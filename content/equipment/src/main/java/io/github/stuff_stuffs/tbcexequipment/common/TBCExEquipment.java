package io.github.stuff_stuffs.tbcexequipment.common;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class TBCExEquipment implements ModInitializer {
    public static final String MOD_ID = "tbcexequipment";

    @Override
    public void onInitialize() {

    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }
}
