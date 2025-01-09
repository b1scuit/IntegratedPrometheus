package org.b1scuit.integratedprometheus;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import org.b1scuit.integratedprometheus.prometheus.Prometheus;

import java.io.IOException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(IntegratedPrometheus.MODID)
public class IntegratedPrometheus
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "integratedprometheus";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final Prometheus prometheus;

    public IntegratedPrometheus(FMLJavaModLoadingContext context)
    {
        prometheus = new Prometheus();
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Initalising Prometheus");
        try {
            prometheus.initialize();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
