package com.klarkson.creaternd.Events;



import com.klarkson.creaternd.CreateRND;
import com.klarkson.creaternd.content.entity.EntityHandler;
import com.klarkson.creaternd.content.entity.sculk.FlintskinMob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CreateRND.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            SpawnPlacements.register(EntityHandler.FLINTSKIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, FlintskinMob::canSpawn);
        });
    }

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityHandler.FLINTSKIN.get(), FlintskinMob.getExampleAttributes().build());
    }
}