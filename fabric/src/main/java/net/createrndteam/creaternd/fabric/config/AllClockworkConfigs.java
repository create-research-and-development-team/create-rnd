package net.createrndteam.creaternd.fabric.config;

import com.simibubi.create.foundation.block.BlockStressValues;
import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import net.createrndteam.creaternd.CreateRND;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class AllCreateRNDConfigs {
    private static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    public static CWClient CLIENT;
    public static CWCommon COMMON;
    public static CWServer SERVER;

    public static ConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    private static <T extends CWConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }

    public static void register() {
        CLIENT = register(CWClient::new, ModConfig.Type.CLIENT);
        COMMON = register(CWCommon::new, ModConfig.Type.COMMON);
        SERVER = register(CWServer::new, ModConfig.Type.SERVER);

        for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
            ModLoadingContext.registerConfig(CreateRND.MOD_ID, pair.getKey(), pair.getValue().specification);

        BlockStressValues.registerProvider(CreateRND.MOD_ID, SERVER.kinetics.stressValues);

        ModConfigEvent.LOADING.register(AllCreateRNDConfigs::onLoad);
        ModConfigEvent.RELOADING.register(AllCreateRNDConfigs::onReload);
    }

    public static void onLoad(ModConfig modConfig) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                    .getSpec())
                config.onLoad();
    }

    public static void onReload(ModConfig modConfig) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                    .getSpec())
                config.onReload();
    }

}