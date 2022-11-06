package com.klarkson.creaternd.content.item.tool.handheldSaw;

import com.simibubi.create.content.curiosities.armor.BackTankUtil;
import com.simibubi.create.foundation.utility.AbstractBlockBreakQueue;
import com.simibubi.create.foundation.utility.TreeCutter;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.network.PacketDistributor;
import org.lwjgl.system.NonnullDefault;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.*;
import java.util.function.Consumer;

@NonnullDefault
public class HandheldSawItem extends AxeItem implements IAnimatable, ISyncable {
    private static final int MAX_VEIN_SIZE = 64;
    private static final String SWINGING_BOOL = "swinging";
    private static final String WAS_SWINGING_BOOL = "wasSwinging";
    private static final int ANIM_IDLE = 0;
    private static final int ANIM_SAW = 1;
    private static final String CONTROLLER_NAME = "handheldSawController";
    public static final int MAX_DAMAGE = 420;

    public final AnimationFactory ANIMATION_FACTORY = GeckoLibUtil.createFactory(this);

    public BlockPos breakingPos;
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

            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
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
        Optional<AbstractBlockBreakQueue> dynamicTree = TreeCutter.findDynamicTree(block.getBlock(), pos);

        if (dynamicTree.isPresent()) {
            breakingLevel = level;
            breakingPos = pos;
            dynamicTree.get()
                    .destroyBlocks(level, player, this::dropItemFromCutTree);
            return true;
        }

        return super.mineBlock(tool, level, block, pos, player);
    }

    public void dropItemFromCutTree(BlockPos pos, ItemStack stack) {
        Vec3 dropPos = VecHelper.getCenterOf(pos);
        ItemEntity entity = new ItemEntity(breakingLevel, dropPos.x, dropPos.y, dropPos.z, stack);
        breakingLevel.addFreshEntity(entity);
    }

    private <P extends AxeItem & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
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
        data.addAnimationController(new AnimationController<>(this, CONTROLLER_NAME, 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.ANIMATION_FACTORY;
    }

    @Override
    public boolean onEntitySwing(ItemStack item, LivingEntity entity)
    {
        CompoundTag tag = ensureTagged(item);
        tag.putBoolean(SWINGING_BOOL, true);

        return true;
    }

    @Override
    public void inventoryTick(ItemStack item, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide && selected) {
            final int id = GeckoLibUtil.guaranteeIDForStack(item, (ServerLevel) level);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity);

            CompoundTag tag = ensureTagged(item);

            boolean swinging = tag.getBoolean(SWINGING_BOOL);
            boolean wasSwinging = tag.getBoolean(WAS_SWINGING_BOOL);

            if(swinging && !wasSwinging) {
                GeckoLibNetwork.syncAnimation(target, this, id, ANIM_SAW);
            }
            else if (!swinging && wasSwinging) {
                GeckoLibNetwork.syncAnimation(target, this, id, ANIM_IDLE);
            }

            tag.putBoolean(WAS_SWINGING_BOOL, swinging);
            tag.putBoolean(SWINGING_BOOL, false);
        }

        super.inventoryTick(item, level, entity, slot, selected);
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

    private CompoundTag ensureTagged(ItemStack item) {
        CompoundTag tag = item.getTag();
        if(tag == null) {
            tag = new CompoundTag();
            item.setTag(tag);
        }
        return tag;
    }
}
