package io.github.stuff_stuffs.tbcexgui.common;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class TBCExGui implements ModInitializer {
    public static final String MOD_ID = "tbcex_gui_v2";

    @Override
    public void onInitialize() {

    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }
}
