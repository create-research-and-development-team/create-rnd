package com.klarkson.creaternd.content.entity.sculk;

import com.klarkson.creaternd.content.entity.GeckoEntityHandler;
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
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.BiConsumer;

public class FlintskinMob extends Animal implements IAnimatable, ISyncable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private HearingHelper hearingHelper;

    public FlintskinMob(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return GeckoEntityHandler.FLINTSKIN.get().create(level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder getExampleAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 25.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    // TODO: Make a custom sound effect
    public void signalReceived(ServerLevel level, Entity causalEntity) {
        this.playSound(SoundEvents.SCULK_CLICKING, 1f, 1.2f+this.getVoicePitch());
    }

    public void tick() {
        this.hearingHelper.tick();
        super.tick();
    }

    public void customServerAiStep() {
        this.hearingHelper.customServerAiStep();
        super.customServerAiStep();
    }

    public static boolean canSpawn(EntityType<FlintskinMob> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkAnimalSpawnRules(entityType, level, spawnType, pos, random) && pos.getY() > 100;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        this.hearingHelper.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        this.hearingHelper.readAdditionalSaveData(tag);
    }

    public @NotNull Brain<?> makeBrain(Dynamic<?> dynamicBrain) {
        this.hearingHelper = Optional.ofNullable(this.hearingHelper).orElse(new HearingHelper(this, this::signalReceived, 40));
        return this.hearingHelper.makeBrain(dynamicBrain);
    }

    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> gameEventFunction) {
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
}