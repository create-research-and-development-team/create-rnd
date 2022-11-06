package com.klarkson.creaternd.content.item;

import com.klarkson.creaternd.CreateRND;
import com.klarkson.creaternd.ModGroup;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ItemHandler {

    private static final CreateRegistrate REGISTRATE = CreateRND.registrate()
            .creativeModeTab(() -> ModGroup.GROUP);


    public static void register() {}
}
