package com.example.lanoffline;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue ALLOW_OFFLINE = BUILDER
            .comment("If true, worlds opened to LAN skip Mojang authentication so offline-mode players can join.",
                     "Anyone can claim any username (including yours), so only share the world with people you trust.")
            .define("allowOfflineAccounts", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean allowOffline() {
        try {
            return ALLOW_OFFLINE.get();
        } catch (IllegalStateException notLoadedYet) {
            return true;
        }
    }
}
