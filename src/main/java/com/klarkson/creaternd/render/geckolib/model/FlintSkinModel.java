package com.klarkson.creaternd.render.geckolib.model;

import com.klarkson.creaternd.CreateRND;

import com.klarkson.creaternd.content.entity.sculk.FlintskinMob;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FlintSkinModel extends AnimatedGeoModel<FlintskinMob> {
	@Override
	public ResourceLocation getModelResource(FlintskinMob object)
	{
		return new ResourceLocation(CreateRND.MODID, "geo/flintskin.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(FlintskinMob object)
	{
		return new ResourceLocation(CreateRND.MODID, "textures/entity/flintskin.png");
	}

	@Override
	public ResourceLocation getAnimationResource(FlintskinMob object)
	{
		return new ResourceLocation(CreateRND.MODID, "animations/flintskin.animations.json");
	}
}