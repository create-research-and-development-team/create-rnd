package com.klarkson.creaternd.content.entity.sculk.flintskin;

import com.klarkson.creaternd.content.entity.sculk.HearingMobAi;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

import java.util.ArrayList;
import java.util.List;

public class FlintskinAi extends HearingMobAi<FlintskinMob> {
    private static final List<MemoryModuleType<?>> MEMORY_TYPES = new ArrayList<>();
    private static final List<SensorType<? extends Sensor<? super FlintskinMob>>> SENSOR_TYPES = new ArrayList<>();

    public FlintskinAi(FlintskinMob flintskin) {
        super(flintskin);
    }

    public Brain<FlintskinMob> makeBrain(Dynamic<?> dynamicBrain) {
        return super.makeBrain(dynamicBrain, MEMORY_TYPES, SENSOR_TYPES);
    }
}
