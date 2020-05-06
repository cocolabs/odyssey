package io.yooksi.odyssey.entity.ai.goal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class EatGrassDrinkWaterGoal
    extends RandomWalkingGoal {

  public static final int EAT_DRINK_TIMER_START_VALUE = 60;

  private static final Predicate<BlockState> IS_GRASS = BlockStateMatcher.forBlock(Blocks.GRASS);
  private static final Predicate<BlockState> IS_GRASS_BLOCK = BlockStateMatcher.forBlock(Blocks.GRASS_BLOCK);

  private static final int BLOCK_SEARCH_COUNT = 10;

  private MobEntity entity;
  private World world;
  private int actionTimer;
  private BlockPos targetBlockPos;

  public EatGrassDrinkWaterGoal(CreatureEntity entity, double speed) {

    super(entity, speed);
    this.entity = entity;
    this.world = entity.world;
    this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
  }

  public boolean shouldExecute() {

    if (this.entity.getIdleTime() >= 100) {
      return false;
    }

    if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
      return false;
    }

    // Try to locate a grass, grass block, or water for the entity to path to.
    //
    // Loop until a valid position is found or the max tries are exceeded:
    // 1. Select a pathable position that is a grass, or above a grass block or water source
    // 2. Locate a pathable position one block adjacent to the selected position that also has a solid block below it
    // 3. Store the position of the block to path to
    // 4. Store the position of the block to eat

    for (int i = 0; i < BLOCK_SEARCH_COUNT; i++) {

      Vec3d posVec = this.getPosition();

      if (posVec == null) {
        return false;
      }

      BlockPos blockPos = new BlockPos(posVec);

      if (this.isBlockPosValid(blockPos)) { // 1
        BlockPos adjacentPos = this.getAdjacentPos(blockPos); // 2

        if (adjacentPos == null) {
          continue;
        }

        this.x = adjacentPos.getX(); // 3
        this.y = adjacentPos.getY();
        this.z = adjacentPos.getZ();

        this.targetBlockPos = blockPos; // 4

        return true;
      }
    }

    return false;
  }

  @Nullable
  private BlockPos getAdjacentPos(BlockPos blockPos) {

    for (int i = 0; i < 4; i++) {
      Direction direction = Direction.byHorizontalIndex(i);
      BlockPos offset = blockPos.offset(direction);

      if (this.entity.getNavigator().canEntityStandOnPos(offset)) {
        return offset;
      }
    }

    return null;
  }

  private boolean isBlockPosValid(BlockPos blockPos) {

    BlockState blockState = this.world.getBlockState(blockPos);

    if (IS_GRASS.test(blockState)) {
      return true;

    } else {
      BlockPos blockPosDown = blockPos.down();
      BlockState blockStateDown = this.world.getBlockState(blockPosDown);
      IFluidState fluidState = this.world.getFluidState(blockPosDown);
      if (blockStateDown.getBlock() != Blocks.SANDSTONE) {
        System.out.println(blockStateDown);
      }
      return fluidState.isTagged(FluidTags.WATER) || IS_GRASS_BLOCK.test(blockStateDown);
    }
  }

  @Override
  public void startExecuting() {

    super.startExecuting();
    this.actionTimer = EAT_DRINK_TIMER_START_VALUE;
  }

  @Override
  public void resetTask() {

    super.resetTask();
    this.actionTimer = 0;
  }

  @Override
  public boolean shouldContinueExecuting() {

    return this.actionTimer > 0;
  }

  public int getActionTimer() {

    return this.actionTimer;
  }

  @Override
  public void tick() {

    if (!this.entity.getNavigator().noPath()) {
      // If the entity is still pathing, short-circuit.
      return;
    }

    // Set the entity's look position
    this.entity.getLookController()
        .setLookPosition(this.targetBlockPos.getX(), this.entity.getPosYEye(), this.targetBlockPos.getZ());

    this.world.setEntityState(this.entity, (byte) 10);
    this.actionTimer = Math.max(0, this.actionTimer - 1);

    if (this.actionTimer == 4) {
      BlockPos blockPos = this.targetBlockPos;

      if (IS_GRASS.test(this.world.getBlockState(blockPos))) {

        if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.entity)) {
          this.world.destroyBlock(blockPos, false);
        }

        this.entity.eatGrassBonus();

      } else if (this.world.getFluidState(blockPos).isTagged(FluidTags.WATER)) {
        this.entity.eatGrassBonus();

      } else {
        BlockPos blockPosDown = blockPos.down();
        Block blockDown = this.world.getBlockState(blockPosDown).getBlock();
        IFluidState fluidState = this.world.getFluidState(blockPosDown);

        if (fluidState.isTagged(FluidTags.WATER)) {
          this.entity.eatGrassBonus();

        } else if (blockDown == Blocks.GRASS_BLOCK) {

          if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.entity)) {
            this.world.playEvent(2001, blockPosDown, Block.getStateId(Blocks.GRASS_BLOCK.getDefaultState()));
            this.world.setBlockState(blockPosDown, Blocks.DIRT.getDefaultState(), 2);
          }

          this.entity.eatGrassBonus();
        }
      }
    }
  }
}
