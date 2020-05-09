package io.yooksi.odyssey.entity.ai.goal;

import io.yooksi.odyssey.entity.passive.CamelEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.EnumSet;

/**
 * This goal will make the camel follow its owner and teleport to it when it
 * gets out of range, like the wolf.
 *
 * @author codetaylor
 * @see net.minecraft.entity.ai.goal.FollowOwnerGoal
 */
public class CamelFollowOwnerGoal
		extends Goal {

	private CamelEntity camelEntity;
	private LivingEntity owner;
	private IWorldReader world;
	private double followSpeed;
	private int timeToRecalculatePath;
	private float startDist;
	private float stopDist;
	private float oldWaterCost;

	public CamelFollowOwnerGoal(CamelEntity camelEntity, double followSpeed, float startDist,
								float stopDist) {

		this.camelEntity = camelEntity;
		this.world = camelEntity.world;
		this.followSpeed = followSpeed;
		this.startDist = startDist;
		this.stopDist = stopDist;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {

		LivingEntity livingentity = this.camelEntity.getOwner();

		if (livingentity == null)
		{
			return false;
		}
		else if (livingentity.isSpectator())
		{
			return false;
		}
		else if (this.camelEntity.isSitting())
		{
			return false;
		}
		else if (this.camelEntity.getDistanceSq(livingentity) < (double) (this.startDist * this.startDist))
		{
			return false;
		}
		else
		{
			this.owner = livingentity;
			return true;
		}
	}

	@Override
	public boolean shouldContinueExecuting() {

		if (this.camelEntity.getNavigator().noPath())
		{
			return false;
		}
		else if (this.camelEntity.isSitting())
		{
			return false;
		}
		else
		{
			return !(this.camelEntity.getDistanceSq(this.owner) <= (double) (this.stopDist * this.stopDist));
		}
	}

	@Override
	public void startExecuting() {

		this.timeToRecalculatePath = 0;
		this.oldWaterCost = this.camelEntity.getPathPriority(PathNodeType.WATER);
		this.camelEntity.setPathPriority(PathNodeType.WATER, 0.0F);
	}

	@Override
	public void resetTask() {

		this.owner = null;
		this.camelEntity.getNavigator().clearPath();
		this.camelEntity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
	}

	@Override
	public void tick() {

		this.camelEntity.getLookController().setLookPositionWithEntity(this.owner, 10.0F,
				(float) this.camelEntity.getVerticalFaceSpeed());

		if (--this.timeToRecalculatePath <= 0)
		{
			this.timeToRecalculatePath = 10;

			if (!this.camelEntity.getLeashed() && !this.camelEntity.isPassenger())
			{

				if (this.camelEntity.getDistanceSq(this.owner) >= 32 * 32)
				{
					this.teleportToOwner();
				}
				else
				{
					this.camelEntity.getNavigator().tryMoveToEntityLiving(this.owner, this.followSpeed);
				}
			}
		}
	}

	private void teleportToOwner() {

		BlockPos blockpos = new BlockPos(this.owner);

		for (int i = 0; i < 10; ++i)
		{
			int x = this.randomInt(-3, 3);
			int y = this.randomInt(-1, 1);
			int z = this.randomInt(-3, 3);

			if (!this.teleportTo(blockpos.getX() + x, blockpos.getY() + y, blockpos.getZ() + z))
			{
				return;
			}
		}
	}

	private boolean teleportTo(int x, int y, int z) {

		if (Math.abs((double) x - this.owner.getPosX()) < 2.0 && Math.abs((double) z - this.owner.getPosZ()) < 2.0)
		{
			return true;
		}
		else if (!this.canTeleportToBlock(new BlockPos(x, y, z)))
		{
			return true;
		}
		else
		{
			this.camelEntity.setLocationAndAngles((float) x + 0.5f, y, (float) z + 0.5f,
					this.camelEntity.rotationYaw, this.camelEntity.rotationPitch);
			this.camelEntity.getNavigator().clearPath();
		}

		return false;
	}

	private boolean canTeleportToBlock(BlockPos blockPos) {

		PathNodeType pathnodetype = WalkNodeProcessor.func_227480_b_(this.world, blockPos.getX(),
				blockPos.getY(), blockPos.getZ());

		if (pathnodetype != PathNodeType.WALKABLE)
		{
			return false;
		}
		else
		{
			BlockState blockstate = this.world.getBlockState(blockPos.down());

			if (blockstate.getBlock() instanceof LeavesBlock)
			{
				return false;
			}
			else
			{
				BlockPos blockpos = blockPos.subtract(new BlockPos(this.camelEntity));
				return this.world.hasNoCollisions(this.camelEntity,
						this.camelEntity.getBoundingBox().offset(blockpos));
			}
		}
	}

	private int randomInt(int min, int max) {

		return this.camelEntity.getRNG().nextInt(max - min + 1) + min;
	}
}
