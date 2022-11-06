package com.klarkson.creaternd.content.item.tool.handheldSaw;

import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class HandheldSawRenderer extends GeoItemRenderer<HandheldSawItem> {
    public HandheldSawRenderer() {
        super(new HandheldSawModel());
    }
}
