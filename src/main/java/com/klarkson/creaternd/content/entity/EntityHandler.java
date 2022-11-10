package com.klarkson.creaternd.content.entity;

import com.klarkson.creaternd.CreateRND;
import com.klarkson.creaternd.content.entity.sculk.FlintskinMob;
import com.tterrag.registrate.util.entry.EntityEntry;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityHandler {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CreateRND.MODID);

    public static final RegistryObject<EntityType<FlintskinMob>> FLINTSKIN = ENTITIES.register("flintskin",
            () -> EntityType.Builder.of(FlintskinMob::new, MobCategory.CREATURE).sized(0.4f, 0.4f).build(CreateRND.MODID + ":flintskin"));

    public static void register() {}
}