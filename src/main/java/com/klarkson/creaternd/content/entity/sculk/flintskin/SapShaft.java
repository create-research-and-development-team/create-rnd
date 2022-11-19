package com.klarkson.creaternd.content.entity.sculk.flintskin;

import com.google.common.collect.ImmutableMap;
import com.klarkson.creaternd.CreateRND;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.contraptions.relays.elementary.ShaftBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.system.NonnullDefault;

import javax.annotation.Nullable;
import java.util.Optional;

@NonnullDefault
public class SapShaft extends Behavior<FlintskinMob> {
    public final static int MINIMUM_SHAFT_SPEED = 15;

    @Nullable
    private BlockPos targetShaft;

    public SapShaft() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED));
    }

    protected boolean checkExtraStartConditions(ServerLevel level, FlintskinMob flintSkin) {
        if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(level, flintSkin)) return false;

        targetShaft = this.getNearestShaft(level, flintSkin.blockPosition());

        return targetShaft != null;
    }

    @Nullable
    private BlockPos getNearestShaft(ServerLevel level, BlockPos blockPos) {
        Optional<BlockPos> foundShaft = BlockPos.findClosestMatch(blockPos, 8, 4,
                (pos) -> validPos(pos, level));

        if(foundShaft.isEmpty()) return null;

        return foundShaft.get();
    }

    private boolean validPos(BlockPos pos, ServerLevel level) {
        BlockState blockstate = level.getBlockState(pos);
        Block block = blockstate.getBlock();

        if(!(block instanceof ShaftBlock)) return false;

        if(((ShaftBlock)block).getTileEntity(level, pos) == null) return false;

        return ((ShaftBlock)block).getTileEntity(level, pos).getSpeed() > MINIMUM_SHAFT_SPEED;
    }

    protected void start(ServerLevel level, FlintskinMob flintskin, long timestamp) {
        if (targetShaft != null) {
            flintskin.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(targetShaft));
            flintskin.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(targetShaft), 1F, 0));
        }
    }

    protected void stop(ServerLevel level, FlintskinMob flintskin, long timestamp) {
        flintskin.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        flintskin.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    protected void tick(ServerLevel level, FlintskinMob flintskin, long timestamp) {
        if(targetShaft != null && targetShaft.closerToCenterThan(flintskin.position(), 1.0D)) {
            if(validPos(targetShaft, level)) {
                Direction.Axis axis = level.getBlockState(targetShaft).getValue(RotatedPillarKineticBlock.AXIS);
                level.setBlockAndUpdate(targetShaft, AllBlocks.COGWHEEL.get().defaultBlockState().setValue(RotatedPillarKineticBlock.AXIS, axis));
                flintskin.discard();
            }
        }
    }

    protected boolean canStillUse(ServerLevel level, FlintskinMob flintskin, long timestamp) {
        if(targetShaft == null) return false;
        return validPos(targetShaft, level);
    }
}
