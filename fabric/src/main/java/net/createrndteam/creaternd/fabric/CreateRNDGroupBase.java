package net.createrndteam.creaternd.fabric;

import com.simibubi.create.content.AllSections;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.fabricators_of_create.porting_lib.util.ItemGroupUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;

import static net.createrndteam.creaternd.CreateRND.MOD_ID;

public abstract class CreateRNDGroupBase extends CreativeModeTab {

    public CreateRNDGroupBase(String id) {
        super(ItemGroupUtil.expandArrayAndGetId(), MOD_ID + "." + id);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void fillItemList(NonNullList<ItemStack> items) {
        addItems(items, true);
        addBlocks(items);
        addItems(items, false);
    }

    @Environment(EnvType.CLIENT)
    public void addBlocks(NonNullList<ItemStack> items) {
        for (RegistryEntry<? extends Block> entry : getBlocks()) {
            Block def = entry.get();
            Item item = def.asItem();
            if (item != Items.AIR)
                def.fillItemCategory(this, items);
        }
    }

    @Environment(EnvType.CLIENT)
    public void addItems(NonNullList<ItemStack> items, boolean specialItems) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        for (RegistryEntry<? extends Item> entry : getItems()) {
            Item item = entry.get();
            if (item instanceof BlockItem)
                continue;
            ItemStack stack = new ItemStack(item);
            BakedModel model = itemRenderer.getModel(stack, null, null, 0);
            if (model.isGui3d() != specialItems)
                continue;
            item.fillItemCategory(this, items);
        }
    }

    protected Collection<RegistryEntry<Block>> getBlocks() {
        return getSections().stream()
                .flatMap(s -> CreateRNDFabric.REGISTRATE
                        .getAll(s, Registry.BLOCK_REGISTRY)
                        .stream())
                .collect(Collectors.toList());
    }

    protected Collection<RegistryEntry<Item>> getItems() {
        return getSections().stream()
                .flatMap(s -> CreateRNDFabric.REGISTRATE
                        .getAll(s, Registry.ITEM_REGISTRY)
                        .stream())
                .collect(Collectors.toList());
    }

    protected EnumSet<AllSections> getSections() {
        return EnumSet.allOf(AllSections.class);
    }
}
