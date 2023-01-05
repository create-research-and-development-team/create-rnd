package net.createrndteam.creaternd.forge.mixin.create.client;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.utility.outliner.Outline;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.core.api.ships.properties.ShipTransform;
import org.valkyrienskies.mod.common.VSClientGameUtils;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(Outline.class)
public abstract class MixinOutline {
    @Shadow
    protected void putVertex(final PoseStack.Pose pose, final VertexConsumer builder, final float x, final float y,
        final float z, final float u, final float v,
        final Direction normal) {
    }

    @Inject(
        method = "putVertex(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/Vec3;FFLnet/minecraft/core/Direction;)V",
        at = @At(value = "HEAD"), cancellable = true
    )
    public void injectPutVertex(final PoseStack ms, final VertexConsumer builder, final Vec3 pos, final float u,
        final float v,
        final Direction normal,
        final CallbackInfo ci) {
        final Vector3d vec3d = new Vector3d(pos.x, pos.y, pos.z);

        final Level level = Minecraft.getInstance().level;
        if (level != null) {
            final ClientShip ship = (ClientShip)VSGameUtilsKt.getShipManagingPos(level, vec3d);
            if (ship != null) {
                final ShipTransform transform = ship.getRenderTransform();
                final Vector3d transformedPos = transform.getShipToWorld().transformPosition(vec3d);
                putVertex(ms.last(), builder, (float) transformedPos.x, (float) transformedPos.y,
                    (float) transformedPos.z, u, v, normal);
                ci.cancel();
            }
        }
    }
    @Redirect(
        method = "renderCuboidLine",
        at = @At(value = "INVOKE",
            target = "Lcom/jozufozu/flywheel/util/transform/TransformStack;translate(Lnet/minecraft/world/phys/Vec3;)Ljava/lang/Object;")
    )
    private Object redirectTranslate(final TransformStack instance, final Vec3 vec3) {
        VSClientGameUtils.transformRenderIfInShipyard((PoseStack) instance, vec3.x, vec3.y, vec3.z);
        return instance;
    }
}
