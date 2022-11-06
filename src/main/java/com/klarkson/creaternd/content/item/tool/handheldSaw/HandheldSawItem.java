package com.klarkson.creaternd.content.item.tool.handheldSaw;

import com.simibubi.create.content.curiosities.armor.BackTankUtil;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        boolean ret = super.mineBlock(tool, level, block, pos, player);

        if(ret && isCorrectToolForDrops(block) && !level.isClientSide){
            List<List<ItemStack>> items = new ArrayList<>();
            veinMine((ServerLevel) level, pos, player, tool, BlockTags.LOGS, items, MAX_VEIN_SIZE);

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

    private void veinMine(ServerLevel level, BlockPos pos, LivingEntity player, ItemStack tool, TagKey<Block> blocksToMine, List<List<ItemStack>> itemDrops, int totalBlocks) {
        if(totalBlocks <= 0) {
            return;
        }

        // Using a set to prevent duplicate entries
        Set<BlockPos> positionQueue = new HashSet<>();
        positionQueue.add(pos);
        do {
            Set<BlockPos> newPositionQueue = new HashSet<>();
            for(BlockPos position : positionQueue) {

                BlockState blockMined = level.getBlockState(position);
                if(blockMined.is(blocksToMine)){
                    itemDrops.add(Block.getDrops(blockMined, level, position, null, player, tool));
                    level.destroyBlock(position, false);

                    totalBlocks--;
                    if(totalBlocks <= 0) {
                        return;
                    }

                    for(int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            for(int k = -1; k < 2; k++) {
                                if(i == 0 && j == 0 && k == 0) continue;
                                newPositionQueue.add(position.offset(i, j, k));
                            }
                        }
                    }
                }
            }
            positionQueue = new HashSet<>(newPositionQueue);
        } while (!positionQueue.isEmpty());

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
