package com.klarkson.creaternd.content.item.tool.handheldSaw;

import com.klarkson.creaternd.CreateRND;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HandheldSawModel extends AnimatedGeoModel<HandheldSawItem> {
    @Override
    public ResourceLocation getModelResource(HandheldSawItem object) {
        return new ResourceLocation(CreateRND.MODID, "geo/handheld_saw.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HandheldSawItem object) {
        return new ResourceLocation(CreateRND.MODID, "textures/item/handheld_drill.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HandheldSawItem animatable) {
        return new ResourceLocation(CreateRND.MODID, "animations/handheld_saw.animation.json");
    }
}
