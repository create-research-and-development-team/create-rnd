package com.klarkson.creaternd.render.geckolib.renderer;


import com.klarkson.creaternd.CreateRND;
import com.klarkson.creaternd.content.entity.sculk.FlintskinMob;
import com.klarkson.creaternd.render.geckolib.model.FlintSkinModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FlintSkinRenderer extends MobRenderer<FlintskinMob, FlintSkinModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CreateRND.MODID, "textures/entity/flintskin.png");

    public FlintSkinRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new FlintSkinModel(ctx.bakeLayer(FlintSkinModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(FlintskinMob entity) {
        return TEXTURE;
    }
}