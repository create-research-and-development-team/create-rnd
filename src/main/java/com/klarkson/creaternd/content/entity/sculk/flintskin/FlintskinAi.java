package com.klarkson.creaternd.content.entity.sculk.flintskin;

import com.google.common.collect.ImmutableList;
import com.klarkson.creaternd.content.entity.sculk.HearingMobAi;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.ArrayList;
import java.util.List;

public class FlintskinAi extends HearingMobAi<FlintskinMob> {
    private static final List<MemoryModuleType<?>> MEMORY_TYPES = List.of(MemoryModuleType.AVOID_TARGET);
    private static final List<SensorType<? extends Sensor<? super FlintskinMob>>> SENSOR_TYPES = new ArrayList<>();

    public FlintskinAi(FlintskinMob flintskin) {
        super(flintskin);
    }

    public Brain<FlintskinMob> makeBrain(Dynamic<?> dynamicBrain) {
        Brain brain = super.makeBrain(dynamicBrain, MEMORY_TYPES, SENSOR_TYPES);
        initRetreatActivity(brain);
        return brain;
    }

    @Override
    public void updateActivity() {
        mob.getBrain().setActiveActivityToFirstValid(ImmutableList.of(
                Activity.HIDE,
                Activity.AVOID,
                Activity.INVESTIGATE,
                Activity.IDLE));
    }

    @Override
    public void initCoreActivity(Brain<FlintskinMob> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new SapShaft(),
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()));
    }

    private void initRetreatActivity(Brain<FlintskinMob> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.3F, 15, true)),
                MemoryModuleType.AVOID_TARGET);
    }

    public void retreatFromNearestTarget(LivingEntity mostRecentDetection) {
        Brain<?> brain = mob.getBrain();
        LivingEntity nearestTarget = BehaviorUtils.getNearestTarget(mob, brain.getMemory(MemoryModuleType.AVOID_TARGET), mostRecentDetection);
        setAvoidTarget(nearestTarget);
    }

    private void setAvoidTarget(LivingEntity avoidMob) {
        mob.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, avoidMob, TimeUtil.rangeOfSeconds(3, 6).sample(mob.level.random));
    }
}
