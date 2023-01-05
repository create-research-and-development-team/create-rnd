package net.createrndteam.creaternd.fabric;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.createrndteam.creaternd.fabric.config.AllCreateRNDConfigs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.createrndteam.creaternd.CreateRND;
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric;

import static net.createrndteam.creaternd.CreateRND.MOD_ID;

public class CreateRNDFabric implements ModInitializer {
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public static final CreativeModeTab BASE_CREATIVE_TAB = new CreateRNDGroup();


    @Override
    public void onInitialize() {
        // force VS2 to load before eureka
        new ValkyrienSkiesModFabric().onInitialize();
        AllCreateRNDBlocks.register();
        AllCreateRNDItems.register();
        AllCreateRNDTileEntities.register();

        REGISTRATE.register();

        AllCreateRNDParticles.register();
        AllCreateRNDConfigs.register();
        CreateRND.init();
    }

    @Environment(EnvType.CLIENT)
    public static class Client implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
            CreateRND.initClient();
            AllCreateRNDPartials.init();
            AllCreateRNDParticles.registerFactories();
        }


    }

    public static class ModMenu implements ModMenuApi {
    }
}
