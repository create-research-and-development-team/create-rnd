package com.klarkson.creaternd.content.item.tool.handheldDrill;

import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class HandheldDrillRenderer extends GeoItemRenderer<HandheldDrillItem> {
    public HandheldDrillRenderer() {
        super(new HandheldDrillModel());
    }
}