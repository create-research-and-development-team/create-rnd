package com.klarkson.creaternd.content.item;

import com.klarkson.creaternd.CreateRND;
import com.klarkson.creaternd.ModGroup;
import com.klarkson.creaternd.content.entity.GeckoEntityHandler;
import com.klarkson.creaternd.content.item.tool.handheldDrill.HandheldDrillItem;
import com.klarkson.creaternd.content.item.tool.handheldSaw.HandheldSawItem;
import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class ItemHandler {

    private static final CreateRegistrate REGISTRATE = CreateRND.registrate()
            .creativeModeTab(() -> ModGroup.GROUP);

    public static final ItemEntry<HandheldSawItem> HANDHELD_SAW = REGISTRATE.item("handheld_saw",
                    (p) -> new HandheldSawItem(Tiers.DIAMOND, 5, -3.0f, p))
            .register();

    public static final ItemEntry<HandheldDrillItem> HANDHELD_DRILL = REGISTRATE.item("handheld_drill",
                    (p) -> new HandheldDrillItem(Tiers.DIAMOND, 1, -2.8f, p))
            .register();

    public static final ItemEntry<ForgeSpawnEggItem> FLINTSKIN_SPAWN_EGG = REGISTRATE.item("flintskin_spawn_egg",
                    (p) -> new ForgeSpawnEggItem(GeckoEntityHandler.FLINTSKIN, 0x273738, 0x10E5D4, p))
            .register();

    public static final ItemEntry<Item> RAW_LOBSTER = REGISTRATE.item("raw_lobster", Item::new)
            .properties(p -> p.food(new FoodProperties.Builder().nutrition(4)
                    .saturationMod(0.2F)
                    .build()))
            .register();
    public static final ItemEntry<Item> COOKED_LOBSTER = REGISTRATE.item("cooked_lobster", Item::new)
            .properties(p -> p.food(new FoodProperties.Builder().nutrition(8)
                    .saturationMod(0.6F)
                    .build()))
            .register();
    public static final ItemEntry<Item> LOBSTER_ROLL = REGISTRATE.item("lobster_roll", Item::new)
            .properties(p -> p.food(new FoodProperties.Builder().nutrition(16)
                    .saturationMod(1F)
                    .build()))
            .register();

    public static void register() {
        Create.registrate().addToSection(HANDHELD_SAW, AllSections.CURIOSITIES);
        Create.registrate().addToSection(HANDHELD_DRILL, AllSections.CURIOSITIES);
    }
}
