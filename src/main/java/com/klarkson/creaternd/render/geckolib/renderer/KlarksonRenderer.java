package com.klarkson.creaternd.render.geckolib.renderer;

import com.klarkson.creaternd.content.entity.companion.types.Klarkson;
import com.klarkson.creaternd.render.geckolib.model.KlarksonModel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class KlarksonRenderer extends GeoProjectilesRenderer<Klarkson> {
    public KlarksonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new KlarksonModel());
    }

    @Override
    public void renderEarly(Klarkson animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource,
                            VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue,
                            float alpha) {
        this.renderEarlyMat = poseStack.last().pose().copy();
        this.animatable = animatable;
        poseStack.scale(0.85f,0.85f,0.85f);
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer,
                packedLight, packedOverlay, red, green, blue, alpha);
    }
}
