package com.klarkson.creaternd.content.entity.sculk;

import com.klarkson.creaternd.CreateRND;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import org.lwjgl.system.NonnullDefault;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

@NonnullDefault
public class HearingHelper implements VibrationListener.VibrationListenerConfig {
    private final int VIBRATION_COOLDOWN_TICKS;

    private final Mob entity;
    private final BiConsumer<BlockPos, Entity> signalReceived;

    private final DynamicGameEventListener<VibrationListener> dynamicGameEventListener;

    public HearingHelper(Mob entity, BiConsumer<BlockPos, Entity> signalReceived, int vibrationCooldown) {
        this.entity = entity;
        this.signalReceived = signalReceived;
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this.entity, this.entity.getEyeHeight()), 16, this, null, 0.0F, 0));
        this.VIBRATION_COOLDOWN_TICKS = vibrationCooldown;
    }

    public VibrationListener getListener() {
        return this.dynamicGameEventListener.getListener();
    }

    public boolean shouldListen(ServerLevel level, GameEventListener listener, BlockPos pos, GameEvent event, GameEvent.Context context) {
        if (!entity.isNoAi() && !entity.isDeadOrDying() && !entity.getBrain().hasMemoryValue(MemoryModuleType.VIBRATION_COOLDOWN) && level.getWorldBorder().isWithinBounds(pos) && !entity.isRemoved() && entity.level == level) {
            Entity eventEntity = context.sourceEntity();

            if(eventEntity == null) return false;
            if(entity.level != eventEntity.level) return false;

            if (eventEntity instanceof LivingEntity livingEntity) {
                return entity.level == livingEntity.level && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity) && !entity.isAlliedTo(livingEntity) && livingEntity.getType() != entity.getType() && !livingEntity.isInvulnerable() && !livingEntity.isDeadOrDying() && level.getWorldBorder().isWithinBounds(livingEntity.getBoundingBox());
            }

            return true;
        }

        return false;
    }

    public void onSignalReceive(ServerLevel level, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity causalEntity, @Nullable Entity projectileOwner, float distance) {
        if (!entity.isDeadOrDying()) {
            getBrain().setMemoryWithExpiry(MemoryModuleType.VIBRATION_COOLDOWN, Unit.INSTANCE, VIBRATION_COOLDOWN_TICKS);
            signalReceived.accept(pos, causalEntity);
        }
    }

    public void tick() {
        if (entity.level instanceof ServerLevel serverlevel) {
            this.dynamicGameEventListener.getListener().tick(serverlevel);
        }
    }

    public void customServerAiStep() {
        ServerLevel serverlevel = (ServerLevel)entity.level;
        this.getBrain().tick(serverlevel, entity);
    }

    public Brain<Mob> getBrain() {
        return (Brain<Mob>)entity.getBrain();
    }

    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> gameEventFunction) {
        Level level = entity.level;
        if (level instanceof ServerLevel serverlevel) {
            gameEventFunction.accept(this.dynamicGameEventListener, serverlevel);
        }
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        VibrationListener.codec(this).encodeStart(NbtOps.INSTANCE, this.dynamicGameEventListener.getListener()).resultOrPartial(CreateRND.LOGGER::error)
                .ifPresent((tagPresent) -> tag.put("listener", tagPresent));
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("listener", 10)) {
            VibrationListener.codec(this).parse(new Dynamic<>(NbtOps.INSTANCE, tag.getCompound("listener"))).resultOrPartial(CreateRND.LOGGER::error)
                    .ifPresent((tagPresent) -> this.dynamicGameEventListener.updateListener(tagPresent, entity.level));
        }
    }
}
