package com.klarkson.creaternd.render.geckolib.renderer;

import com.klarkson.creaternd.content.entity.sculk.flintskin.FlintskinMob;
import com.klarkson.creaternd.render.geckolib.model.FlintSkinModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class FlintSkinRenderer extends GeoEntityRenderer<FlintskinMob> {
    public FlintSkinRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FlintSkinModel());
    }
}