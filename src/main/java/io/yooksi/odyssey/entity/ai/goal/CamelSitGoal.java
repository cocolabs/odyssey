package io.yooksi.odyssey.entity.ai.goal;

import io.yooksi.odyssey.entity.passive.CamelEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * This is based off of the tameable entity sit goal. We can't use vanilla's sit goal because the camel
 * entity does not extend {@link net.minecraft.entity.passive.TameableEntity}.
 *
 * @author codetaylor
 * @see net.minecraft.entity.ai.goal.SitGoal
 */
public class CamelSitGoal
		extends Goal {

	private CamelEntity camelEntity;
	private boolean sitting;

	public CamelSitGoal(CamelEntity camelEntity) {

		this.camelEntity = camelEntity;
		this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
	}

	@Override
	public boolean shouldContinueExecuting() {

		return this.sitting;
	}

	@Override
	public boolean shouldExecute() {

		if (!this.camelEntity.isTame())
		{
			return false;
		}
		else if (this.camelEntity.isInWaterOrBubbleColumn())
		{
			return false;
		}
		else if (!this.camelEntity.onGround)
		{
			return false;
		}
		else
		{
			LivingEntity livingentity = this.camelEntity.getOwner();

			if (livingentity == null)
			{
				return true;
			}
			else
			{
				return (!(this.camelEntity.getDistanceSq(livingentity) < 144.0D) || livingentity.getRevengeTarget() == null) && this.sitting;
			}
		}
	}

	@Override
	public void startExecuting() {

		this.camelEntity.getNavigator().clearPath();
		this.camelEntity.setSitting(true);
	}

	@Override
	public void resetTask() {

		this.camelEntity.setSitting(false);
	}

	public void setSitting(boolean sitting) {

		this.sitting = sitting;
	}
}
