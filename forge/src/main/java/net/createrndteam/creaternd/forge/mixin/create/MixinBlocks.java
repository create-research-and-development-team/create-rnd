package net.createrndteam.creaternd.forge.mixin.create;

import com.simibubi.create.content.contraptions.components.millstone.MillstoneBlock;
import com.simibubi.create.content.contraptions.processing.BasinBlock;
import com.simibubi.create.content.logistics.block.chute.AbstractChuteBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.util.Iterator;

@Mixin(value = {
    MillstoneBlock.class,
    BasinBlock.class,
    AbstractChuteBlock.class
})
public class MixinBlocks {

    @Redirect(
        method = "updateEntityAfterFallOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;blockPosition()Lnet/minecraft/core/BlockPos;"
        ),
        require = 0
    )
    protected BlockPos redirectBlockPosition(final Entity entity) {
        final Iterator<Ship> ships =
            VSGameUtilsKt.getShipsIntersecting(entity.level, entity.getBoundingBox()).iterator();
        if (ships.hasNext()) {
            final Vector3d pos = ships.next().getWorldToShip()
                .transformPosition(VectorConversionsMCKt.toJOML(entity.position()));
            return new BlockPos(Math.floor(pos.x), Math.floor(pos.y), Math.floor(pos.z));
        } else {
            return entity.blockPosition();
        }
    }

    @Redirect(
        method = "updateEntityAfterFallOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;position()Lnet/minecraft/world/phys/Vec3;"
        ),
        require = 0
    )
    Vec3 redirectPosition(final Entity entity) {
        final Iterator<Ship> ships =
            VSGameUtilsKt.getShipsIntersecting(entity.level, entity.getBoundingBox()).iterator();
        if (ships.hasNext()) {
            return VectorConversionsMCKt.toMinecraft(ships.next().getWorldToShip()
                .transformPosition(VectorConversionsMCKt.toJOML(entity.position())));
        } else {
            return entity.position();
        }
    }

}
