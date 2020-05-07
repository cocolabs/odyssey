package io.yooksi.odyssey.entity.passive;

import io.yooksi.odyssey.entity.ModEntityTypes;
import io.yooksi.odyssey.entity.ai.goal.CamelWanderGoal;
import io.yooksi.odyssey.entity.ai.goal.EatGrassDrinkWaterGoal;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.Set;

public class CamelEntity
    extends LlamaEntity {

  public static final String NAME = "camel";

  private static MethodHandle goalSelector$goalsGetter;

  static {

    try {

      goalSelector$goalsGetter = MethodHandles.lookup().unreflectGetter(
          /*
          MC 1.15.1: net/minecraft/entity/ai/goal/GoalSelector.goals
          Name: d => field_220892_d => goals
          Comment: None
          Side: BOTH
          AT: public net.minecraft.entity.ai.goal.GoalSelector field_220892_d # goals
           */
          ObfuscationReflectionHelper.findField(GoalSelector.class, "field_220892_d")
      );

    } catch (Throwable t) {
      throw new RuntimeException(String.format("Error unreflecting field: %s", "field_220892_d"), t);
    }
  }

  private static final DataParameter<Integer> DATA_WHEAT_COUNT = EntityDataManager.createKey(CamelEntity.class, DataSerializers.VARINT);

  private static final int WHEAT_COUNT_REQUIRED_TO_TAME = 4;

  private int eatDrinkTimer;
  private EatGrassDrinkWaterGoal eatDrinkGoal;

  public CamelEntity(EntityType<? extends LlamaEntity> entityType, World world) {

    super(entityType, world);
  }

  // ---------------------------------------------------------------------------
  // - Data
  // ---------------------------------------------------------------------------

  @Override
  protected void registerData() {

    super.registerData();
    this.dataManager.register(DATA_WHEAT_COUNT, 0);
  }

  private void setWheatCount(int count) {

    this.dataManager.set(DATA_WHEAT_COUNT, Math.max(0, Math.min(WHEAT_COUNT_REQUIRED_TO_TAME, count)));
  }

  private int getWheatCount() {

    return this.dataManager.get(DATA_WHEAT_COUNT);
  }

  public int getEatDrinkTimer() {

    return this.eatDrinkTimer;
  }

  public LivingEntity getOwner() {

    try {
      UUID uuid = this.getOwnerUniqueId();
      return uuid == null ? null : this.world.getPlayerByUuid(uuid);

    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  // ---------------------------------------------------------------------------
  // - Goals
  // ---------------------------------------------------------------------------

  @Override
  protected void registerGoals() {

    super.registerGoals();

    // We need to remove the goals we don't care about. The RunAroundLikeCrazyGoal
    // is triggered when the player is riding an untamed animal.
    try {
      //noinspection unchecked
      Set<PrioritizedGoal> goals = (Set<PrioritizedGoal>) goalSelector$goalsGetter.invokeExact(this.goalSelector);

      for (Iterator<PrioritizedGoal> it = goals.iterator(); it.hasNext(); ) {
        PrioritizedGoal prioritizedGoal = it.next();
        Goal goal = prioritizedGoal.getGoal();

        if (goal instanceof RunAroundLikeCrazyGoal
            || goal instanceof LlamaFollowCaravanGoal
            || goal instanceof WaterAvoidingRandomWalkingGoal) {
          it.remove();
        }
      }

    } catch (Throwable t) {
      throw new RuntimeException(String.format("Error accessing unreflected field: %s", "field_220892_d"), t);
    }

    this.eatDrinkGoal = new EatGrassDrinkWaterGoal(this, 0.7);
    this.goalSelector.addGoal(5, this.eatDrinkGoal);
    this.goalSelector.addGoal(6, new CamelWanderGoal(this, 0.7));
  }

  // ---------------------------------------------------------------------------
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void livingTick() {

    if (this.world.isRemote) {
      this.eatDrinkTimer = Math.max(0, this.eatDrinkTimer - 1);
    }

    super.livingTick();
  }

  @Override
  protected void updateAITasks() {

    this.eatDrinkTimer = this.eatDrinkGoal.getActionTimer();
    super.updateAITasks();
  }

  @OnlyIn(Dist.CLIENT)
  public void handleStatusUpdate(byte id) {

    if (id == 10) {
      this.eatDrinkTimer = EatGrassDrinkWaterGoal.EAT_DRINK_TIMER_START_VALUE;

    } else {
      super.handleStatusUpdate(id);
    }
  }

  // ---------------------------------------------------------------------------
  // - Breeding
  // ---------------------------------------------------------------------------

  @Override
  public boolean isBreedingItem(ItemStack stack) {

    return stack.getItem() == Items.WHEAT;
  }

  @Override
  public LlamaEntity createChild(AgeableEntity ageable) {

    return ModEntityTypes.CAMEL.get().create(this.world);
  }

  @Override
  public boolean canMateWith(AnimalEntity otherAnimal) {

    return otherAnimal != this && otherAnimal instanceof CamelEntity && this.canMate() && ((CamelEntity) otherAnimal).canMate();
  }

  // ---------------------------------------------------------------------------
  // - Interaction
  // ---------------------------------------------------------------------------

  @Override
  public boolean processInteract(PlayerEntity player, @Nonnull Hand hand) {

    ItemStack itemstack = player.getHeldItem(hand);

    if (this.isBreedingItem(itemstack)) {
      boolean isAdult = (this.getGrowingAge() == 0);

      // If the camel is an adult and not tame, consume a wheat and increase
      // the camel's wheat counter. If four pieces of wheat are consumed,
      // set the camel tamed by the player.

      if (!this.world.isRemote && isAdult && !this.isTame()) {

        int count = this.getWheatCount();

        if (count < WHEAT_COUNT_REQUIRED_TO_TAME) {
          this.setWheatCount(count + 1);
          this.consumeItemFromStack(player, itemstack);
          player.func_226292_a_(hand, true);

          if (count + 1 == WHEAT_COUNT_REQUIRED_TO_TAME) {
            this.setTamedBy(player);
          }

          return true;
        }
      }

      // If the camel is an adult and can breed (its love counter is zero),
      // consume a wheat and set the camel in love.

      if (!this.world.isRemote && isAdult && this.canBreed()) {
        this.consumeItemFromStack(player, itemstack);
        this.setInLove(player);
        player.func_226292_a_(hand, true);
        return true;
      }

      // If the camel is a child, consume a wheat and increment the child's age.

      if (this.isChild()) {
        this.consumeItemFromStack(player, itemstack);
        this.ageUp((int) ((float) (-this.getGrowingAge() / 20) * 0.1f), true);
        return true;
      }
    }

    return false;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void writeAdditional(CompoundNBT compound) {

    super.writeAdditional(compound);
    compound.putInt("WheatCount", this.getWheatCount());
  }

  @Override
  public void readAdditional(CompoundNBT compound) {

    super.readAdditional(compound);
    this.setWheatCount(compound.getInt("WheatCount"));
  }
}
