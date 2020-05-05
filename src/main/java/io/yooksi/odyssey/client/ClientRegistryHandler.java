package io.yooksi.odyssey.client;

import io.yooksi.odyssey.common.Defines;
import io.yooksi.odyssey.entity.ModEntityTypes;
import net.minecraft.client.renderer.entity.LlamaRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Defines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistryHandler {

  /**
   * We need to register our renderers on the client because rendering code does not exist on the server
   * and trying to use it on a dedicated server will crash the game.
   * <p>
   * This method will be called by Forge when it is time for the mod to do its client-side setup
   * This method will always be called after the Registry events.
   * This means that all Blocks, Items, TileEntityTypes, etc. will all have been registered already
   * <p>
   * Comment source: https://github.com/Cadiboo/Example-Mod/blob/1.15.2/src/main/java/io/github/cadiboo/examplemod/client/ClientModEventSubscriber.java
   */
  @SubscribeEvent
  public static void on(FMLClientSetupEvent event) {

    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CAMEL.get(), LlamaRenderer::new);
  }

}
