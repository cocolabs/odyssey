package io.yooksi.odyssey.config;

import io.yooksi.odyssey.common.Defines;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = Defines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OdysseyConfig {

	public static final ClientConfig CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;

	static {
		final Pair<ClientConfig, ForgeConfigSpec> specPair =
				new ForgeConfigSpec.Builder().configure(ClientConfig::new);

		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	private static long timeCycleSpeed;

	public static void bakeConfig() {
		timeCycleSpeed = ClientConfig.timeCycleSpeed.get();
	}

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {

		if (configEvent.getConfig().getSpec() == CLIENT_SPEC) {
			bakeConfig();
		}
	}

	public static long getTimeCycleSpeed() {
		return timeCycleSpeed;
	}
}