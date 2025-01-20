package org.b1scuit.integratedprometheus;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;
import org.cyclops.cyclopscore.tracking.Analytics;
import org.cyclops.cyclopscore.tracking.Versions;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfig {
    @ConfigurableProperty(category = "general", comment = "If the API should be enabled.", requiresMcRestart = true)
    public static boolean startApi = true;
    @ConfigurableProperty(category = "general", comment = "The port the API should be exposed on.")
    public static int apiPort = 3000;
    @ConfigurableProperty(category = "general", comment = "The base IP the API is exposed on")
    public static String apiBaseUrl = "0.0.0.0";
    public GeneralConfig() {
        super(IntegratedPrometheus._instance, "general");
    }
}
