package com.klarkson.creaternd.render.geckolib.model;

import com.klarkson.creaternd.CreateRND;

import com.klarkson.creaternd.content.entity.companion.types.Klarkson;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class KlarksonModel extends AnimatedGeoModel<Klarkson>{
    @Override
    public ResourceLocation getModelResource(Klarkson object)
    {
        return new ResourceLocation(CreateRND.MODID, "geo/klarkson.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Klarkson object)
    {
        return new ResourceLocation(CreateRND.MODID, "textures/entity/klarkson.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Klarkson object)
    {
        return new ResourceLocation(CreateRND.MODID, "animations/klarkson.animations.json");
    }

}

