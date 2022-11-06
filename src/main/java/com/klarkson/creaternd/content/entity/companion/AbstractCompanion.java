package com.klarkson.creaternd.content.entity.companion;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.network.ISyncable;

public abstract class AbstractCompanion extends Entity implements IAnimatable, ISyncable {
    public AbstractCompanion(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }
}
