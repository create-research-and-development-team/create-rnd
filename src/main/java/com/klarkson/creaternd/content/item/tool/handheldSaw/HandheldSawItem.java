package com.klarkson.creaternd.content.item.tool.handheldSaw;

import com.simibubi.create.content.curiosities.armor.BackTankUtil;
import com.simibubi.create.foundation.utility.AbstractBlockBreakQueue;
import com.simibubi.create.foundation.utility.TreeCutter;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.network.PacketDistributor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.system.NonnullDefault;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.*;
import java.util.function.Consumer;

// TODO: Switch from GeckoLib to Flywheel Partials
@NonnullDefault
public class HandheldSawItem extends AxeItem implements IAnimatable, ISyncable {
    private static final int ANIM_IDLE = 0;
    private static final int ANIM_SAW = 1;
    private static final String CONTROLLER_NAME = "handheldSawController";
    public static final int MAX_DAMAGE = 2048;

    public final AnimationFactory ANIMATION_FACTORY = GeckoLibUtil.createFactory(this);

    public Level breakingLevel;

    public HandheldSawItem(Tier tier, float attackBonus, float attackSpeedBonus, Properties properties) {
        super(tier, attackBonus, attackSpeedBonus, properties.defaultDurability(MAX_DAMAGE));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new HandheldSawRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.BLOCK_EFFICIENCY)
            return true;
        if (enchantment == Enchantments.UNBREAKING)
            return true;
        if (enchantment == Enchantments.MENDING)
            return true;

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    private int maxUses(){
        return 1000;
    }
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return BackTankUtil.isBarVisible(stack, maxUses());
    }
    @Override
    public int getBarWidth(ItemStack stack) {
        return BackTankUtil.getBarWidth(stack, maxUses());
    }
    @Override
    public int getBarColor(ItemStack stack) {
        return BackTankUtil.getBarColor(stack, maxUses());
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack item, int amount, T entity, Consumer<T> onBroken) {
        if(BackTankUtil.canAbsorbDamage(entity, maxUses())) {
            return 0;
        } else {
            return amount;
        }
    }

    @Override
    public boolean mineBlock(ItemStack tool, Level level, BlockState block, BlockPos pos, LivingEntity player) {
        boolean ret = super.mineBlock(tool, level, block, pos, player);

        if(player.isCrouching()) return ret;

        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

        breakingLevel = level;

        Optional<AbstractBlockBreakQueue> dynamicTree = TreeCutter.findDynamicTree(block.getBlock(), pos);
        if (dynamicTree.isPresent()) {
            dynamicTree.get()
                    .destroyBlocks(level, player, this::dropItemFromCutTree);
            return true;
        }

        TreeCutter.findTree(level, pos).destroyBlocks(level, player, this::dropItemFromCutTree);
        return ret;
    }

    public void dropItemFromCutTree(BlockPos pos, ItemStack stack) {
        Vec3 dropPos = VecHelper.getCenterOf(pos);
        ItemEntity entity = new ItemEntity(breakingLevel, dropPos.x, dropPos.y, dropPos.z, stack);
        breakingLevel.addFreshEntity(entity);
    }


    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || newStack.getItem() != oldStack.getItem();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        // Do not set transitionLengthTicks to 0, it crashes with a null pointer exception
        data.addAnimationController(new AnimationController<>(this, CONTROLLER_NAME, 1, event -> PlayState.CONTINUE));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.ANIMATION_FACTORY;
    }

    @Override
    public boolean onEntitySwing(ItemStack item, LivingEntity player) {
        player.swinging = true;
        player.swingTime = -2;
        return true;
    }

    @Override
    public void inventoryTick(ItemStack item, Level level, Entity player, int count, boolean selected) {
        if(!level.isClientSide) {
            LivingEntity livingPlayer = (LivingEntity) player;

            final int id = GeckoLibUtil.guaranteeIDForStack(item, (ServerLevel) player.level);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player);

            final AnimationController controller = GeckoLibUtil.getControllerForID(this.ANIMATION_FACTORY, id, CONTROLLER_NAME);

            if((!selected || !livingPlayer.swinging) && checkIfNotAnimOrNull(controller, "idle")) {
                GeckoLibNetwork.syncAnimation(target, this, id, ANIM_IDLE);
            } else if(selected && livingPlayer.swinging && checkIfNotAnimOrNull(controller, "saw")) {
                GeckoLibNetwork.syncAnimation(target, this, id, ANIM_SAW);
            }
        }
    }

    @Override
    public void onAnimationSync(int id, int state) {
        // Always use GeckoLibUtil to get AnimationControllers when you don't have
        // access to an AnimationEvent
        final AnimationController controller = GeckoLibUtil.getControllerForID(this.ANIMATION_FACTORY, id, CONTROLLER_NAME);

        if (state == ANIM_SAW) {
            controller.setAnimation(new AnimationBuilder().addAnimation("saw", ILoopType.EDefaultLoopTypes.LOOP));
        } else if (state == ANIM_IDLE) {
            controller.setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
    }

    private boolean checkIfNotAnimOrNull(@NonNull AnimationController controller, @NonNull String anim) {
        if(controller.getCurrentAnimation() == null) return true;

        return !Objects.equals(controller.getCurrentAnimation().animationName, anim);
    }
}
