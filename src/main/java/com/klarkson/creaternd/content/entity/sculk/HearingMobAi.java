package com.klarkson.creaternd.content.entity.sculk;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.ArrayList;
import java.util.List;

public abstract class HearingMobAi<T extends PathfinderMob> {
    PathfinderMob mob;

    public final List<? extends MemoryModuleType<?>> ADDITIONAL_MEMORY_TYPES = List.of(MemoryModuleType.DISTURBANCE_LOCATION, MemoryModuleType.VIBRATION_COOLDOWN, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH);
    public final List<SensorType<? extends Sensor<? super T>>> ADDITIONAL_SENSOR_TYPES = new ArrayList<>();

    public HearingMobAi(T mob) {
        this.mob = mob;
    }

    protected Brain<T> makeBrain(Dynamic<?> dynamicBrain, List<MemoryModuleType<?>> memoryTypes, List<SensorType<? extends Sensor<? super T>>> sensorTypes) {
        memoryTypes.addAll(ADDITIONAL_MEMORY_TYPES);
        sensorTypes.addAll(ADDITIONAL_SENSOR_TYPES);

        Brain.Provider<T> provider = Brain.provider(memoryTypes, sensorTypes);
        Brain<T> brain = provider.makeBrain(dynamicBrain);

        initCoreActivity(brain);
        initIdleActivity(brain);
        initHearingActivity(brain);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    public void updateActivity() {
        mob.getBrain().setActiveActivityToFirstValid(ImmutableList.of(
                Activity.INVESTIGATE,
                Activity.IDLE));
    }

    public void initHearingActivity(Brain<T> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.INVESTIGATE,
                5,
                ImmutableList.of(
                        new GoToTargetLocation<>(
                                MemoryModuleType.DISTURBANCE_LOCATION,
                                2,
                                0.8F)),
                MemoryModuleType.DISTURBANCE_LOCATION);
    }

    public void initCoreActivity(Brain<T> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()));
    }

    public void initIdleActivity(Brain<T> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(new RunOne<>(ImmutableList.of(
                        Pair.of(new RandomStroll(0.5F), 2),
                        Pair.of(new DoNothing(30, 60), 1)))));
    }

    public void setDisturbanceLocation(BlockPos pos) {
        if (mob.level.getWorldBorder().isWithinBounds(pos)) {
            mob.getBrain().setMemoryWithExpiry(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(pos), 100L);
            mob.getBrain().setMemoryWithExpiry(MemoryModuleType.DISTURBANCE_LOCATION, pos, 100L);
            mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        }
    }
}
