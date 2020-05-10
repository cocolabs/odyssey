package io.yooksi.odyssey.client;

import io.yooksi.odyssey.client.renderer.entity.CamelRenderer;
import io.yooksi.odyssey.common.Defines;
import io.yooksi.odyssey.entity.ModEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Defines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistryHandler {

	/**
	 * <p>We need to register our renderers on the client because rendering code does not exist on the server
	 * and trying to use it on a dedicated server will crash the game.</p>
	 * <p>This method will be called by Forge when it is time for the mod to do its client-side setup and
	 * will always be called after the Registry events. This means that all Blocks, Items, TileEntityTypes,
	 * etc. will all have been registered already.</p>
	 *
	 * @see <a href="https://git.io/JfClI">Cadiboo/Example-Mod - ClientModEventSubscriber.java</a>
	 */
	@SubscribeEvent
	public static void on(FMLClientSetupEvent event) {

		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CAMEL.get(), CamelRenderer::new);
	}
}
