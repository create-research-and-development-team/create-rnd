package net.createrndteam.creaternd.fabric;

import com.jozufozu.flywheel.core.PartialModel;


public class AllCreateRNDPartials {
    //public static final PartialModel



    //;

    private static PartialModel block(String path) {
        return new PartialModel(CreateRNDFabric.asResource("block/" + path));
    }
    private static PartialModel entity(String path) {
        return new PartialModel(CreateRNDFabric.asResource("entity/" + path));
    }

    public static void init() {
        // init static fields
    }
}
