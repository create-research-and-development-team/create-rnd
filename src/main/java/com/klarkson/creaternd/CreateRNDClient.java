package com.klarkson.creaternd;

import com.klarkson.creaternd.content.entity.GeckoEntityHandler;
import com.klarkson.creaternd.render.geckolib.renderer.KlarksonRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CreateRNDClient {

    public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(CreateRNDClient::registerRenderers);
    }

    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GeckoEntityHandler.KLARKSON.get(), KlarksonRenderer::new);
    }
}
