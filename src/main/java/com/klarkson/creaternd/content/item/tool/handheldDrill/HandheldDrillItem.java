package com.klarkson.creaternd.content.item.tool.handheldDrill;

import com.simibubi.create.content.curiosities.armor.BackTankUtil;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@NonnullDefault
public class HandheldDrillItem extends PickaxeItem implements IAnimatable, ISyncable {
    private static final int ANGLE_LIMIT = 55;

    private static final String SWINGING_BOOL = "swinging";
    private static final String WAS_SWINGING_BOOL = "wasSwinging";
    private static final int ANIM_IDLE = 0;
    private static final int ANIM_DRILL = 1;
    private static final String CONTROLLER_NAME = "handheldDrillController";
    public static final int MAX_DAMAGE = 360;
    public final AnimationFactory ANIMATION_FACTORY = GeckoLibUtil.createFactory(this);

    public HandheldDrillItem(Tier tier, int alwaysOne, float attackSpeedBonus, Properties properties) {
        super(tier, alwaysOne, attackSpeedBonus, properties.defaultDurability(MAX_DAMAGE));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new HandheldDrillRenderer();

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
        if (enchantment == Enchantments.BLOCK_FORTUNE)
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

        if(ret && isCorrectToolForDrops(tool, block) && !level.isClientSide){
            List<List<ItemStack>> items = new ArrayList<>();
            mineLarge((ServerLevel) level, pos, player, tool, BlockTags.MINEABLE_WITH_PICKAXE, items);

            int blocksMined = items.size();

            tool.hurtAndBreak(blocksMined-1, player, (p) -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));

            for(List<ItemStack> drops : items) {
                for(ItemStack item : drops) {
                    new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), item).spawnAtLocation(item);
                }
            }
        }

        return ret;
    }

    private void mineLarge(ServerLevel level, BlockPos pos, LivingEntity player, ItemStack tool, TagKey<Block> blocksToMine, List<List<ItemStack>> itemDrops) {
        Direction direction = player.getDirection();

        float xRot = player.getXRot();
        if(xRot <= -ANGLE_LIMIT || xRot >= ANGLE_LIMIT) {
            direction = Direction.UP;
        }

        Direction[] directions1 = new Direction[3];
        directions1[1] = null;
        Direction[] directions2 = new Direction[3];
        directions2[1] = null;
        switch(direction.getAxis()) {
            case X:
                directions1[0] = Direction.NORTH;
                directions1[2] = Direction.SOUTH;
                directions2[0] = Direction.UP;
                directions2[2] = Direction.DOWN;
                break;
            case Y:
                directions1[0] = Direction.NORTH;
                directions1[2] = Direction.SOUTH;
                directions2[0] = Direction.EAST;
                directions2[2] = Direction.WEST;
                break;
            case Z:
                directions1[0] = Direction.WEST;
                directions1[2] = Direction.EAST;
                directions2[0] = Direction.UP;
                directions2[2] = Direction.DOWN;
                break;
        }

        for(Direction dir1 : directions1) {
            for(Direction dir2 : directions2) {
                BlockPos testPos = pos;

                if(dir1 != null) {
                    testPos = testPos.relative(dir1);
                }
                if(dir2 != null) {
                    testPos = testPos.relative(dir2);
                }

                BlockState blockMined = level.getBlockState(testPos);
                if(blockMined.is(blocksToMine)) {
                    itemDrops.add(Block.getDrops(blockMined, level, testPos, null, player, tool));
                    level.destroyBlock(testPos, false);
                }
            }
        }

    }

    private <P extends PickaxeItem & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
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
        // Do not set transitionLengtTicks to 0, it crashes with a null pointer exception
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
                GeckoLibNetwork.syncAnimation(target, this, id, ANIM_DRILL);
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

        if (state == ANIM_DRILL) {
            controller.setAnimation(new AnimationBuilder().addAnimation("drill", ILoopType.EDefaultLoopTypes.LOOP));
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
