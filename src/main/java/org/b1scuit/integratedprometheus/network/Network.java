package org.b1scuit.integratedprometheus.network;

import com.mojang.logging.LogUtils;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.api.network.INetwork;
import org.cyclops.integrateddynamics.api.network.INetworkElement;
import org.cyclops.integrateddynamics.core.persist.world.NetworkWorldStorage;
import org.slf4j.Logger;

public class Network {
    private static final Logger LOGGER = LogUtils.getLogger();


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void GetNetwork() {
        NetworkWorldStorage worldStorage = NetworkWorldStorage.getInstance(IntegratedDynamics._instance);

        // All networks
        for (INetwork network : worldStorage.getNetworks()) {
            LOGGER.info("Network: " + network);
            for (INetworkElement element : network.getElements()) {
                LOGGER.info("Element: " + element);
            }
        }

    }
}
