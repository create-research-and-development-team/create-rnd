package com.klarkson.creaternd.render.geckolib.renderer;

import com.klarkson.creaternd.content.entity.companion.types.Klarkson;
import com.klarkson.creaternd.render.geckolib.model.KlarksonModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class KlarksonRenderer extends GeoProjectilesRenderer<Klarkson> {
    public KlarksonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new KlarksonModel());
    }
}
