package net.createrndteam.creaternd.fabric.config;

import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class CWConfigBase extends ConfigBase {
    @Override
    protected void registerAll(final ForgeConfigSpec.Builder builder) {
        if (this.allValues != null) super.registerAll(builder);
    }
}
