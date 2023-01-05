package net.createrndteam.creaternd.forge;

import net.createrndteam.creaternd.CreateRND;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreateRND.MOD_ID)
public class CreateRNDForge {
    boolean happendClientSetup = false;
    static IEventBus MOD_BUS;

    public CreateRNDForge() {
        // Submit our event bus to let architectury register our content on the right time
        MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_BUS.addListener(this::clientSetup);

        MOD_BUS.addListener(this::onModelRegistry);
        MOD_BUS.addListener(this::clientSetup);
        MOD_BUS.addListener(this::entityRenderers);

        CreateRND.init();
    }

    void clientSetup(final FMLClientSetupEvent event) {
        if (happendClientSetup) return;
        happendClientSetup = true;

        CreateRND.initClient();

    }

    void entityRenderers(final EntityRenderersEvent.RegisterRenderers event) {

    }

    void onModelRegistry(final ModelRegistryEvent event) {

    }
}
