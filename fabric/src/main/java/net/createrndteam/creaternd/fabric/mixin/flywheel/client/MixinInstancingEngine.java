package net.createrndteam.creaternd.fabric.mixin.flywheel.client;

import com.jozufozu.flywheel.backend.RenderLayer;
import com.jozufozu.flywheel.backend.instancing.instancing.InstancedMaterialGroup;
import com.jozufozu.flywheel.backend.instancing.instancing.InstancingEngine;
import com.jozufozu.flywheel.core.shader.WorldProgram;
import com.mojang.math.Matrix4f;
import java.util.stream.Stream;

import net.createrndteam.creaternd.fabric.mixinducks.MixinInstancingEngineDuck;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = InstancingEngine.class, remap = false)
public abstract class MixinInstancingEngine<P extends WorldProgram> implements MixinInstancingEngineDuck {
    @Unique
    private static final Logger LOGGER = LogManager.getLogger("VS2 flywheel.client.MixinInstancingEngine");

    @Shadow
    protected abstract Stream<InstancedMaterialGroup<P>> getGroupsToRender(@Nullable RenderLayer layer);

    @Override
    public void render(final Matrix4f viewProjection, final double camX, final double camY, final double camZ,
        final RenderLayer layer) {
        //LOGGER.warn("step1");
        this.getGroupsToRender(layer).forEach(g -> {
            //LOGGER.warn("step2");
            g.render(viewProjection, camX, camY, camZ, layer);
            //LOGGER.warn("step3");
        });
        //LOGGER.warn("step4");
    }
}
