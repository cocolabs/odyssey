package io.yooksi.odyssey.entity.passive;

import io.yooksi.odyssey.entity.ModEntityTypes;
import io.yooksi.odyssey.entity.ai.goal.CamelFollowOwnerGoal;
import io.yooksi.odyssey.entity.ai.goal.CamelSitGoal;
import io.yooksi.odyssey.entity.ai.goal.CamelWanderGoal;
import io.yooksi.odyssey.entity.ai.goal.EatGrassDrinkWaterGoal;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
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
import java.util.UUID;

/**
 * @author codetaylor
 */
public class CamelEntity
		extends LlamaEntity {

	public static final String NAME = "camel";

	public static final int SIT_TIMER_START_VALUE = 20;

	/**
	 * Setting this to true will remove all of the camel's goals and enable a
	 * constant walk animation in the model.
	 * <p>
	 * Useful for adjusting the walk animation.
	 */
	public static final boolean DEBUG_WALK_ANIMATION = false;

	/**
	 * This will provide access to the superclass's goals, letting us remove the
	 * goals we don't want.
	 */
	private static MethodHandle goalSelector$goalsGetter;

	static
	{

		try
		{

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
		}
		catch (Throwable t)
		{
			throw new RuntimeException(String.format("Error unreflecting field: %s", "field_220892_d"), t);
		}
	}

	/**
	 * This stores the value of how much wheat the camel has been fed.
	 */
	private static final DataParameter<Integer> DATA_WHEAT_COUNT =
			EntityDataManager.createKey(CamelEntity.class, DataSerializers.VARINT);

	/**
	 * This stores whether or not the camel is sitting.
	 */
	private static final DataParameter<Boolean> DATA_SITTING =
			EntityDataManager.createKey(CamelEntity.class, DataSerializers.BOOLEAN);

	private static final int WHEAT_COUNT_REQUIRED_TO_TAME = 4;

	private int eatDrinkTimer;
	private EatGrassDrinkWaterGoal eatDrinkGoal;
	private int sitTimer;
	private CamelSitGoal sitGoal;

	public CamelEntity(EntityType<? extends LlamaEntity> entityType, World world) {

		super(entityType, world);
		this.sitTimer = 20;
	}

	// ---------------------------------------------------------------------------
	// - Data
	// ---------------------------------------------------------------------------

	@Override
	protected void registerData() {

		super.registerData();
		this.dataManager.register(DATA_WHEAT_COUNT, 0);
		this.dataManager.register(DATA_SITTING, false);
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

		try
		{
			UUID uuid = this.getOwnerUniqueId();
			return uuid == null ? null : this.world.getPlayerByUuid(uuid);
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}

	public boolean isOwner(LivingEntity entity) {

		return (entity == this.getOwner());
	}

	public boolean isSitting() {

		return this.dataManager.get(DATA_SITTING);
	}

	public void setSitting(boolean sitting) {

		this.dataManager.set(DATA_SITTING, sitting);
		this.sitGoal.setSitting(sitting);

		if (sitting)
		{
			this.world.setEntityState(this, (byte) 127);
		}
	}

	public int getSitTimer() {

		return this.sitTimer;
	}

	// ---------------------------------------------------------------------------
	// - Goals
	// ---------------------------------------------------------------------------

	@Override
	protected void registerGoals() {

		super.registerGoals();

		// We need to remove the goals we don't care about. The RunAroundLikeCrazyGoal
		// is triggered when the player is riding an untamed animal.
		try
		{
			//noinspection unchecked
			Set<PrioritizedGoal> goals =
					(Set<PrioritizedGoal>) goalSelector$goalsGetter.invokeExact(this.goalSelector);

			for (Iterator<PrioritizedGoal> it = goals.iterator(); it.hasNext(); )
			{
				PrioritizedGoal prioritizedGoal = it.next();
				Goal goal = prioritizedGoal.getGoal();

				if (goal instanceof RunAroundLikeCrazyGoal
						|| goal instanceof LlamaFollowCaravanGoal
						|| goal instanceof WaterAvoidingRandomWalkingGoal)
				{
					it.remove();
				}
			}

			if (DEBUG_WALK_ANIMATION)
			{
				goals.clear();
			}
		}
		catch (Throwable t)
		{
			throw new RuntimeException(String.format("Error accessing unreflected field: %s",
					"field_220892_d"), t);
		}

		this.sitGoal = new CamelSitGoal(this);
		this.eatDrinkGoal = new EatGrassDrinkWaterGoal(this, 0.7);

		if (!DEBUG_WALK_ANIMATION)
		{
			this.goalSelector.addGoal(2, this.sitGoal);
			this.goalSelector.addGoal(5, this.eatDrinkGoal);
			this.goalSelector.addGoal(6, new CamelFollowOwnerGoal(this, 1, 10, 2));
			this.goalSelector.addGoal(6, new CamelWanderGoal(this, 0.7));
		}
	}

	// ---------------------------------------------------------------------------
	// - Update
	// ---------------------------------------------------------------------------

	@Override
	public void livingTick() {

		if (this.world.isRemote)
		{
			this.eatDrinkTimer = Math.max(0, this.eatDrinkTimer - 1);

			// Change direction of the sitting timer depending on whether the camel
			// is sitting down or standing up.
			if (this.isSitting())
			{
				this.sitTimer = Math.max(0, this.sitTimer - 1);
			}
			else
			{
				this.sitTimer = Math.min(SIT_TIMER_START_VALUE, this.sitTimer + 1);
			}
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

		if (id == 10)
		{
			// 10 is used by the sheep to eat, we'll just use the same value.
			this.eatDrinkTimer = EatGrassDrinkWaterGoal.EAT_DRINK_TIMER_START_VALUE;
		}
		else if (id == 127)
		{
			// We're using the id of 127 to start the sit timer on the client because
			// it seemed like an unused value.
			this.sitTimer = SIT_TIMER_START_VALUE;
		}
		else
		{
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
		boolean isAdult = (this.getGrowingAge() >= 0);

		if (this.isBreedingItem(itemstack))
		{

			// If the camel is an adult and not tame, consume a wheat and increase
			// the camel's wheat counter. If four pieces of wheat are consumed,
			// set the camel tamed by the player.

			if (!this.world.isRemote && isAdult && !this.isTame())
			{

				int count = this.getWheatCount();

				if (count < WHEAT_COUNT_REQUIRED_TO_TAME)
				{
					this.setWheatCount(count + 1);
					this.consumeItemFromStack(player, itemstack);
					player.func_226292_a_(hand, true);

					if (count + 1 == WHEAT_COUNT_REQUIRED_TO_TAME)
					{
						this.setTamedBy(player);
					}

					return true;
				}
			}

			// If the camel is an adult and can breed (its love counter is zero),
			// consume a wheat and set the camel in love.

			if (!this.world.isRemote && isAdult && this.canBreed())
			{
				this.consumeItemFromStack(player, itemstack);
				this.setInLove(player);
				player.func_226292_a_(hand, true);
				return true;
			}

			// If the camel is a child, consume a wheat and increment the child's age.

			if (this.isChild())
			{
				this.consumeItemFromStack(player, itemstack);
				this.ageUp((int) ((float) (-this.getGrowingAge() / 20) * 0.1f), true);
				return true;
			}
		}
		else if (!this.world.isRemote && isAdult && this.isOwner(player) && this.isTame() && hand == Hand.MAIN_HAND)
		{

			// If an adult camel's owner clicks on it with a non-wheat item,
			// toggle the sitting status.

			this.setSitting(!this.isSitting());
			this.isJumping = false;
			this.navigator.clearPath();
			this.setAttackTarget(null);
		}

		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {

		if (this.isInvulnerableTo(source))
		{
			return false;
		}
		else
		{
			Entity entity = source.getTrueSource();

			// The sit goal will be null on the client.
			if (this.sitGoal != null)
			{
				this.sitGoal.setSitting(false);
			}

			if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity))
			{
				amount = (amount + 1.0F) / 2.0F;
			}

			return super.attackEntityFrom(source, amount);
		}
	}

	// ---------------------------------------------------------------------------
	// - Serialization
	// ---------------------------------------------------------------------------

	@Override
	public void writeAdditional(CompoundNBT compound) {

		super.writeAdditional(compound);
		compound.putInt("WheatCount", this.getWheatCount());
		compound.putBoolean("Sitting", this.isSitting());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {

		super.readAdditional(compound);
		this.setWheatCount(compound.getInt("WheatCount"));
		this.setSitting(compound.getBoolean("Sitting"));
	}
}
