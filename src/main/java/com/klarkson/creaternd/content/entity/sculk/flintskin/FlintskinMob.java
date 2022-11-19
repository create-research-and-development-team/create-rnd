package com.klarkson.creaternd.content.entity.sculk.flintskin;

import com.klarkson.creaternd.content.entity.GeckoEntityHandler;
import com.klarkson.creaternd.content.entity.sculk.HearingHelper;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NonnullDefault;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.BiConsumer;

@NonnullDefault
public class FlintskinMob extends Animal implements IAnimatable, ISyncable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final HearingHelper hearingHelper;

    @Nullable
    private FlintskinAi flintskinAi;

    public FlintskinMob(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.getNavigation().setCanFloat(true);
        this.hearingHelper = new HearingHelper(this, this::signalReceived, 40);
        flintskinAi = new FlintskinAi(this);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return GeckoEntityHandler.FLINTSKIN.get().create(level);
    }

    public static AttributeSupplier.Builder getExampleAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 25.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    // TODO: Make a custom sound effect
    public void signalReceived(BlockPos pos, @Nullable Entity causalEntity) {
        this.playSound(SoundEvents.SCULK_CLICKING, 1f, 1.2f+this.getVoicePitch());

        flintskinAi.setDisturbanceLocation(pos);

        if(causalEntity != null) {
            if(causalEntity instanceof Player player) {
                flintskinAi.retreatFromNearestTarget(player);
            }
        }
    }

    public void tick() {
        this.hearingHelper.tick();
        super.tick();
    }

    public void customServerAiStep() {
        this.hearingHelper.customServerAiStep();
        super.customServerAiStep();
        flintskinAi.updateActivity();
    }

    public static boolean canSpawn(EntityType<FlintskinMob> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkAnimalSpawnRules(entityType, level, spawnType, pos, random) && pos.getY() > 100;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.hearingHelper.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.hearingHelper.readAdditionalSaveData(tag);
    }

    public Brain<?> makeBrain(Dynamic<?> dynamicBrain) {
        this.flintskinAi = Optional.ofNullable(this.flintskinAi).orElse(new FlintskinAi(this));
        return flintskinAi.makeBrain(dynamicBrain);
    }

    public Brain<FlintskinMob> getBrain() {
        return (Brain<FlintskinMob>)super.getBrain();
    }

    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> gameEventFunction) {
        this.hearingHelper.updateDynamicGameEventListener(gameEventFunction);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, e -> PlayState.CONTINUE));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {

    }

    public boolean dampensVibrations() {
        return true;
    }
}