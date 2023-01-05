package net.createrndteam.creaternd.forge.mixin.flywheel.client;

import com.jozufozu.flywheel.api.instance.Instance;
import com.jozufozu.flywheel.backend.instancing.InstanceManager;
import com.jozufozu.flywheel.backend.instancing.InstancedRenderRegistry;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstanceManager;
import com.jozufozu.flywheel.backend.instancing.instancing.InstancingEngine;
import com.jozufozu.flywheel.core.Contexts;
import com.jozufozu.flywheel.core.shader.WorldProgram;
import kotlin.ranges.IntRange;
import net.createrndteam.creaternd.fabric.mixinducks.MixinTileInstanceManagerDuck;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Vector3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.WeakHashMap;

@Mixin(value = BlockEntityInstanceManager.class)
@ParametersAreNonnullByDefault
public abstract class MixinTileInstanceManager extends InstanceManager<BlockEntity> implements
        MixinTileInstanceManagerDuck {

    public WeakHashMap<ClientShip, InstancingEngine<WorldProgram>> getShipInstancingEngines() {
        return shipMaterialManagers;
    }

    @Unique
    private final WeakHashMap<ClientShip, InstancingEngine<WorldProgram>> shipMaterialManagers =
        new WeakHashMap<>();

    public MixinTileInstanceManager(final InstancingEngine<?> materialManager) {
        super(materialManager);
    }

    @Inject(
        method = "createRaw(Lnet/minecraft/world/level/block/entity/BlockEntity;)Lcom/jozufozu/flywheel/backend/instancing/AbstractInstance;",
        at = @At("HEAD"),
        cancellable = true
    )
    void preCreateRaw(final BlockEntity blockEntity, final CallbackInfoReturnable<Instance> cir) {
        final Level nullableLevel = blockEntity.getLevel();
        if (nullableLevel instanceof final ClientLevel level) {
            final ClientShip ship = VSGameUtilsKt.getShipObjectManagingPos(
                level, blockEntity.getBlockPos());
            if (ship != null) {
                final InstancingEngine<WorldProgram> manager = shipMaterialManagers.computeIfAbsent(ship,
                    k -> InstancingEngine.builder(Contexts.WORLD).setIgnoreOriginCoordinate(true).build());
                final Vector3i c = ship.getChunkClaim().getCenterBlockCoordinates(new IntRange(0, 0), new Vector3i());
                ((InstancingEngineAccessor) manager).setOriginCoordinate(new BlockPos(c.x, c.y, c.z));

                cir.setReturnValue(InstancedRenderRegistry.createInstance(manager, blockEntity));
            }
        }
    }
}
