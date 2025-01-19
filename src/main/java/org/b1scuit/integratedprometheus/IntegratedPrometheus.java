package org.b1scuit.integratedprometheus;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.b1scuit.integratedprometheus.metricsmanager.MetricsManager;
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

    public IntegratedPrometheus()
    {
        prometheus = new Prometheus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Initalising Prometheus");
        try {
            new MetricsManager();

            prometheus.initialize();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
