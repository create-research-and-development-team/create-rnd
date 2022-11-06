package com.klarkson.creaternd.content.item.tool.handheldDrill;

import com.klarkson.creaternd.CreateRND;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HandheldDrillModel extends AnimatedGeoModel<HandheldDrillItem> {
    @Override
    public ResourceLocation getModelResource(HandheldDrillItem object) {
        return new ResourceLocation(CreateRND.MODID, "geo/handheld_drill.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HandheldDrillItem object) {
        return new ResourceLocation(CreateRND.MODID, "textures/item/handheld_drill.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HandheldDrillItem animatable) {
        return new ResourceLocation(CreateRND.MODID, "animations/handheld_drill.animation.json");
    }
}