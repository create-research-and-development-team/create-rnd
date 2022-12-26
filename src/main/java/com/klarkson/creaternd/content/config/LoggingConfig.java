package com.klarkson.creaternd.content.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LoggingConfig {
    public final ForgeConfigSpec.ConfigValue<Boolean> logging;
    public final ForgeConfigSpec.ConfigValue<Boolean> loggingWarningSeen;

    public LoggingConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Logging");
        logging = builder
                .comment("Enables/Disables profile logging [false/true|default:false]")
                .translation("config.creaternd.logging")
                .define("logging", false);
        loggingWarningSeen = builder
                .comment("Has the logging warning been shown [false/true|default:false]")
                .translation("config.creaternd.loggingWarningSeen")
                .define("loggingWarningSeen", false);
        builder.pop();
    }
}
