package com.klarkson.creaternd.content.entity;

import com.klarkson.creaternd.CreateRND;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityHandler {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CreateRND.MODID);

    public static void register() {}
}