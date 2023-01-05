package net.createrndteam.creaternd.fabric;

import com.simibubi.create.content.AllSections;

public class AllCreateRNDItems {
    static {
        CreateRNDFabric.REGISTRATE.creativeModeTab(() -> CreateRNDFabric.BASE_CREATIVE_TAB);
    }

    static {
        CreateRNDFabric.REGISTRATE.startSection(AllSections.KINETICS);
    }

    public static void register() {}
}
