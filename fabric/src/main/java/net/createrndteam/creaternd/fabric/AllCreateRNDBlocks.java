package net.createrndteam.creaternd.fabric;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class AllCreateRNDBlocks {

    static {
        CreateRNDFabric.REGISTRATE.creativeModeTab(() -> CreateRNDFabric.BASE_CREATIVE_TAB);
    }

    static {
        CreateRNDFabric.REGISTRATE.startSection(AllSections.KINETICS);
    }



    public static void register() {}
}
