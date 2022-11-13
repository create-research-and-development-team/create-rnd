package com.klarkson.creaternd;

import com.klarkson.creaternd.content.block.BlockHandler;
import com.klarkson.creaternd.content.item.ItemHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class ModGroup {

    public static final CreativeModeTab GROUP = new CreativeModeTab(CreateRND.MODID) {
        @Override
        public ItemStack makeIcon() {
            return BlockHandler.TESTBLOCK.asStack();
        }
        @Override
        public void fillItemList(NonNullList<ItemStack> list) {
            list.addAll(Arrays.asList(
                    BlockHandler.TESTBLOCK.asStack(),
                    ItemHandler.HANDHELD_SAW.asStack(),
                    ItemHandler.HANDHELD_DRILL.asStack(),
                    ItemHandler.FLINTSKIN_SPAWN_EGG.asStack(),
                    ItemHandler.RAW_LOBSTER.asStack(),
                    ItemHandler.COOKED_LOBSTER.asStack(),
                    ItemHandler.LOBSTER_ROLL.asStack()
            ));
        }
    };

    public static void register() {
        CreateRND.registrate().creativeModeTab(() -> GROUP, "Create: R&D");
    }
}
