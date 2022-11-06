package com.klarkson.creaternd.content.block;

import com.klarkson.creaternd.CreateRND;
import com.klarkson.creaternd.ModGroup;
import com.klarkson.creaternd.api.CRNDTags;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockHandler {

    private static final CreateRegistrate REGISTRATE = CreateRND.registrate()
            .creativeModeTab(() -> ModGroup.GROUP);

    static {
        REGISTRATE.startSection(AllSections.UNASSIGNED);
    }
    ////TEST////

    public static final BlockEntry<Block> TESTBLOCK = REGISTRATE
            .block("testblock", Block::new)
            .initialProperties(() -> Blocks.NETHERITE_BLOCK)
            .lang("TestBlock")
            .simpleItem()
            .register();


    public static void register() {}

}
