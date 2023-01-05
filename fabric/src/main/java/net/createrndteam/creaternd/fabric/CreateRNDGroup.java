package net.createrndteam.creaternd.fabric;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.AllSections;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class CreateRNDGroup extends CreateRNDGroupBase {

    public CreateRNDGroup() {
        super("base");
    }

    @Override
    protected EnumSet<AllSections> getSections() {
        return EnumSet.complementOf(EnumSet.of(AllSections.PALETTES));
    }

    @Override
    public ItemStack makeIcon() {
        return AllItems.EXTENDO_GRIP.asStack();
    }
}
