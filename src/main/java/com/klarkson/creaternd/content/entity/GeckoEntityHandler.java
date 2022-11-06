package com.klarkson.creaternd.content.entity;

import com.klarkson.creaternd.CreateRND;
import com.klarkson.creaternd.content.entity.companion.types.Klarkson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CreateRND.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GeckoEntityHandler {
    public static final DeferredRegister<EntityType<?>> REG = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CreateRND.MODID);
    public static final RegistryObject<EntityType<Klarkson>> KLARKSON = REG.register("klarkson", () -> EntityType.Builder.of(Klarkson::new, MobCategory.MISC).sized(0.1f,0.5f).build(new ResourceLocation(CreateRND.MODID, "klarkson").toString()));

    @SubscribeEvent
    public static void onCreateAttributes(EntityAttributeCreationEvent event) {
    }
    public static void register() {}

}
