package io.github.stuff_stuffs.tbcexcontent.common;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class TBCExContent implements ModInitializer {
    public static final String MOD_ID = "tbcex_content";

    @Override
    public void onInitialize() {

    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }
}
