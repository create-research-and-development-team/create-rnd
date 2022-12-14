package com.klarkson.creaternd;

import com.klarkson.creaternd.content.block.BlockHandler;
import com.klarkson.creaternd.content.config.ConfigHandler;
import com.klarkson.creaternd.content.entity.EntityHandler;
import com.klarkson.creaternd.content.entity.GeckoEntityHandler;
import com.klarkson.creaternd.content.item.ItemHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

import java.io.IOException;

import static com.klarkson.creaternd.api.logging.logCollection;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateRND.MODID)
public class CreateRND
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "creaternd";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(MODID);
    public CreateRND()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext mlContext = ModLoadingContext.get();

        ItemHandler.register();
        BlockHandler.register();
        ConfigHandler.register(mlContext);

        GeckoLibMod.DISABLE_IN_DEV = true;
        GeckoLib.initialize();
        GeckoEntityHandler.REG.register(modEventBus);
        EntityHandler.ENTITIES.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateRNDClient.prepareClient(modEventBus, forgeEventBus));
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(MODID, path);
    }

}
