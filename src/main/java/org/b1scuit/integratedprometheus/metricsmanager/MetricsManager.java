package org.b1scuit.integratedprometheus.metricsmanager;

import io.prometheus.metrics.core.metrics.CounterWithCallback;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.api.network.INetwork;
import org.cyclops.integrateddynamics.api.network.INetworkElement;
import org.cyclops.integrateddynamics.api.network.IPartNetworkElement;
import org.cyclops.integrateddynamics.core.persist.world.NetworkWorldStorage;


public class MetricsManager {
    NetworkWorldStorage worldStorage = NetworkWorldStorage.getInstance(IntegratedDynamics._instance);

    public MetricsManager() {
        // Define metrics
        defineMetrics();
    }

    private void defineMetrics() {
        PrometheusRegistry registry = PrometheusRegistry.defaultRegistry;
        // Metric: Total number of networks
        CounterWithCallback.builder()
                .name("integrated_dynamics_network_count")
                .help("Total number of networks")
                .callback((callback) -> callback.call(worldStorage.getNetworks().size()))
                .register(registry);
        // Metric: Number of network elements per network
        CounterWithCallback.builder()
                .name("integrated_dynamics_network_element_count")
                .help("Number of network elements per network")
                .labelNames("network_id")
                .callback((callback) -> {
                    for (INetwork network : worldStorage.getNetworks()) {
                        String networkId = Integer.toString(network.hashCode());
                        callback.call(network.getElements().size(),networkId);
                    }
                }).register(registry);

        CounterWithCallback.builder()
                .name("integrated_dynamics_network_element_value_count")
                .help("Energy usage per network in FE")
                .labelNames("network_id","element_id", "priority", "channel")
                .callback((callback) -> {
                    for (INetwork network : worldStorage.getNetworks()) {
                        for (INetworkElement element : network.getElements()) {
                            networkItemCallback(callback, element, network);
                        }
                    }
                }).register(registry);

    }

    public void networkItemCallback(CounterWithCallback.Callback callback, INetworkElement networkElement, INetwork network) {
        // Label initial values
        String priority = "0", channel = "0";
        String networkId = Integer.toString(network.hashCode());
        String elementId = Integer.toString(networkElement.hashCode());


        if (networkElement instanceof IPartNetworkElement) {
            priority = Integer.toString(networkElement.getPriority());
            channel = Integer.toString(networkElement.getChannel());
        }

             // Execute the callback
        callback.call(
                0,
                networkId,
                elementId,
                priority,
                channel
        );
    }
}