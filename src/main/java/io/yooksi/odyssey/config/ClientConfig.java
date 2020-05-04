package io.yooksi.odyssey.config;

import io.yooksi.odyssey.common.Defines;
import io.yooksi.odyssey.core.TimeCycle;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

	public static ForgeConfigSpec.LongValue timeCycleSpeed;

	public ClientConfig(ForgeConfigSpec.Builder builder) {

		timeCycleSpeed = builder
				.comment("Defines the day/night cycle speed proportional to vanilla.")
				.translation("config." + Defines.MODID + "timeCycleSpeed")
				.defineInRange("timeCycleSpeed", -12,
						-TimeCycle.MAX_SPEED, TimeCycle.MAX_SPEED);

		// Finish building configurations
		builder.build();
	}
}