package com.klarkson.creaternd.content.item;

import com.klarkson.creaternd.CreateRND;
import com.klarkson.creaternd.ModGroup;
import com.klarkson.creaternd.content.item.tool.handheldDrill.HandheldDrillItem;
import com.klarkson.creaternd.content.item.tool.handheldSaw.HandheldSawItem;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;

public class ItemHandler {

    private static final CreateRegistrate REGISTRATE = CreateRND.registrate()
            .creativeModeTab(() -> ModGroup.GROUP);

    public static final ItemEntry<HandheldSawItem> HANDHELD_SAW = REGISTRATE.item("handheld_saw",
                    (p) -> new HandheldSawItem(Tiers.NETHERITE, 5, 3.0f, p))
            .register();

    public static final ItemEntry<HandheldDrillItem> HANDHELD_DRILL = REGISTRATE.item("handheld_drill",
                    (p) -> new HandheldDrillItem(Tiers.NETHERITE, 5, 3.0f, p))
            .register();

    public static void register() {}
}
