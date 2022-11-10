package com.klarkson.creaternd.render.geckolib.model;
// Made with Blockbench 4.4.3
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.klarkson.creaternd.CreateRND;

import com.klarkson.creaternd.content.entity.sculk.FlintskinMob;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class FlintSkinModel extends EntityModel<FlintskinMob> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CreateRND.MODID, "sculk_leech"), "main");
	private final ModelPart body;

	public FlintSkinModel(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 2.0F));

		PartDefinition thorax1 = body.addOrReplaceChild("thorax1", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.5F));

		PartDefinition cube_r1 = thorax1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(30, 17).mirror().addBox(-1.25F, -2.0F, -0.25F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.8505F, 2.7901F, 1.6232F, 0.0F, 0.0F));

		PartDefinition cube_r2 = thorax1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 32).addBox(-2.2F, -0.6599F, -2.6005F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 0.1F, 0.35F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r3 = thorax1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -0.4599F, -2.6005F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.25F, 1.5708F, 0.0F, 0.0F));

		PartDefinition thorax2 = thorax1.addOrReplaceChild("thorax2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 3.75F));

		PartDefinition cube_r4 = thorax2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 41).addBox(-1.7F, -0.2151F, -0.2089F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 1.8916F, 0.1552F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r5 = thorax2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -0.0151F, -0.2089F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.7916F, 0.0552F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r6 = thorax2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(30, 17).mirror().addBox(-0.5F, 0.5401F, -2.6005F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.75F, -0.1F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition tail = thorax2.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 4.75F));

		PartDefinition cube_r7 = tail.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(11, 14).addBox(-1.0F, 0.118F, -5.0517F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.7304F, -1.1635F, 3.0543F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r8 = head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(7, 22).addBox(-2.7F, -3.2F, -2.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, 0.2F, 0.1F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r9 = head.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -3.0F, -2.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.1F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition rib1 = head.addOrReplaceChild("rib1", CubeListBuilder.create().texOffs(26, 9).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 11).addBox(-5.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -1.5F, -3.0F));

		PartDefinition cube_r10 = rib1.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(26, 0).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.8112F, 0.0F, 0.1585F, 0.0F, 0.1745F, 0.0F));

		PartDefinition cube_r11 = rib1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(26, 7).addBox(-0.15F, -1.0F, -1.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0259F, 0.0F, 0.0109F, 0.0F, -0.1745F, 0.0F));

		PartDefinition rib2 = head.addOrReplaceChild("rib2", CubeListBuilder.create().texOffs(26, 9).addBox(-0.9F, -0.9F, -0.9F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(26, 11).addBox(-4.5F, -0.9F, -0.9F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.25F, -0.3F, -2.675F));

		PartDefinition cube_r12 = rib2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(26, 0).addBox(0.0F, -0.9F, -0.9F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.2748F, 0.0F, 0.1098F, 0.0F, 0.1309F, 0.0F));

		PartDefinition cube_r13 = rib2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(26, 7).addBox(0.0F, -0.9F, -0.9F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1175F, 0.0F, -0.0077F, 0.0F, -0.1309F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(FlintskinMob entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}