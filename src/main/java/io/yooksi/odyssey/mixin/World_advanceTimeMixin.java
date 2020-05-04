package io.yooksi.odyssey.mixin;

import io.yooksi.odyssey.core.TimeCycle;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class World_advanceTimeMixin extends CapabilityProvider<World> {

	protected World_advanceTimeMixin(Class<World> baseClass) {
		super(baseClass);
	}

	@Shadow protected WorldInfo worldInfo;

	@Shadow public abstract void setDayTime(long time);

	@Inject(method = "advanceTime", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/World;setDayTime(J)V"),
			cancellable = true
	)
	private void onAdvanceTime(CallbackInfo ci) {

		final long cycleSpeed = TimeCycle.getSpeed();
		// When we are trying to slow down day cycle speed
		if (cycleSpeed < 0)
		{
			// Update only after a defined amount of ticks has passed
			long currentGameTime = worldInfo.getGameTime();
			if (TimeCycle.lastGameTime + Math.abs(cycleSpeed) < currentGameTime)
			{
				this.setDayTime(this.worldInfo.getDayTime() + TimeCycle.DEFAULT_SPEED);
				TimeCycle.lastGameTime = worldInfo.getGameTime();
			}
		}
		// When we are trying to speed up day cycle speed
		else if (cycleSpeed > 0) {
			this.setDayTime(this.worldInfo.getDayTime() + cycleSpeed);
		}
		ci.cancel();
	}
}
