package com.klarkson.creaternd.content.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;

public class ConfigHandler {
    protected static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final LoggingConfig LOGGING = new LoggingConfig(ConfigHandler.BUILDER);

    public static final ForgeConfigSpec spec = BUILDER.build();

    public static void register(ModLoadingContext context) {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, spec);
    }
}
