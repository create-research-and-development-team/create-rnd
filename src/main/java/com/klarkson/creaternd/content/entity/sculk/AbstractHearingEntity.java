package com.klarkson.creaternd.content.entity.sculk;

import com.klarkson.creaternd.CreateRND;
import com.klarkson.creaternd.content.entity.EntityHandler;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import org.lwjgl.system.NonnullDefault;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;

@NonnullDefault
public abstract class AbstractHearingEntity extends Animal implements VibrationListener.VibrationListenerConfig {
    private final int VIBRATION_COOLDOWN_TICKS;

    private final DynamicGameEventListener<VibrationListener> dynamicGameEventListener;
    private static final List<SensorType<? extends Sensor<? super AbstractHearingEntity>>> SENSOR_TYPES = List.of();
    private static final List<MemoryModuleType<?>> MEMORY_TYPES = List.of(MemoryModuleType.VIBRATION_COOLDOWN);

    protected AbstractHearingEntity(EntityType<? extends Animal> type, Level level, int vibrationCooldown) {
        super(type, level);
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this, null, 0.0F, 0));
        this.VIBRATION_COOLDOWN_TICKS = vibrationCooldown;
    }

    public VibrationListener getListener() {
        return this.dynamicGameEventListener.getListener();
    }

    public abstract void signalReceived();

    public boolean shouldListen(ServerLevel level, GameEventListener listener, BlockPos pos, GameEvent event, GameEvent.Context context) {
        if (!this.isNoAi() && !this.isDeadOrDying() && !this.getBrain().hasMemoryValue(MemoryModuleType.VIBRATION_COOLDOWN) && level.getWorldBorder().isWithinBounds(pos) && !this.isRemoved() && this.level == level) {
            Entity entity = context.sourceEntity();

            assert entity != null;
            if(this.level != entity.level) return false;

            if (entity instanceof LivingEntity livingEntity) {
                return this.level == livingEntity.level && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity) && !this.isAlliedTo(livingEntity) && livingEntity.getType() != EntityHandler.FLINTSKIN.get() && !livingEntity.isInvulnerable() && !livingEntity.isDeadOrDying() && level.getWorldBorder().isWithinBounds(livingEntity.getBoundingBox());
            }

            return true;
        }

        return false;
    }

    // Deprecated to indicate that this generally shouldn't be overridden unless absolutely necessary
    @Deprecated
    public void onSignalReceive(ServerLevel level, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity, @Nullable Entity projectileOwner, float distance) {
        if (!this.isDeadOrDying()) {
            this.brain.setMemoryWithExpiry(MemoryModuleType.VIBRATION_COOLDOWN, Unit.INSTANCE, 40L);
            level.broadcastEntityEvent(this, (byte)61);
            signalReceived();
        }
    }

    @Override
    public void tick() {
        if (level instanceof ServerLevel serverlevel) {
            this.dynamicGameEventListener.getListener().tick(serverlevel);
        }

        super.tick();
    }

    public void customServerAiStep() {
        ServerLevel serverlevel = (ServerLevel)this.level;
        this.getBrain().tick(serverlevel, this);
        super.customServerAiStep();
    }

    protected Brain<?> makeBrain(Dynamic<?> dynamicBrain) {
        Brain.Provider<AbstractHearingEntity> provider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<AbstractHearingEntity> brain = provider.makeBrain(dynamicBrain);

        return brain;
    }

    public Brain<AbstractHearingEntity> getBrain() {
        return (Brain<AbstractHearingEntity>)super.getBrain();
    }

    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> gameEventFunction) {
        Level level = this.level;
        if (level instanceof ServerLevel serverlevel) {
            gameEventFunction.accept(this.dynamicGameEventListener, serverlevel);
        }
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        VibrationListener.codec(this).encodeStart(NbtOps.INSTANCE, this.dynamicGameEventListener.getListener()).resultOrPartial(CreateRND.LOGGER::error)
                .ifPresent((tagPresent) -> tag.put("listener", tagPresent));
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("listener", 10)) {
            VibrationListener.codec(this).parse(new Dynamic<>(NbtOps.INSTANCE, tag.getCompound("listener"))).resultOrPartial(CreateRND.LOGGER::error)
                    .ifPresent((tagPresent) -> this.dynamicGameEventListener.updateListener(tagPresent, this.level));
        }
    }
}
