package io.yooksi.odyssey;

import io.yooksi.odyssey.common.Defines;
import io.yooksi.odyssey.config.OdysseyConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Defines.MODID)
public class Odyssey {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Odyssey() {

        // Initialize mod logger
        ODLogger.init(LogManager.getLogger());

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register Mod configuration
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, OdysseyConfig.CLIENT_SPEC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {

        LOGGER.info("Pre-initialization phase");

    }
}
