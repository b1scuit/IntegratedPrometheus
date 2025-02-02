package org.b1scuit.integratedprometheus.metricsmanager;

import io.prometheus.metrics.core.metrics.CounterWithCallback;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import net.minecraft.world.item.ItemStack;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.api.network.*;
import org.cyclops.integrateddynamics.core.persist.world.NetworkWorldStorage;
import org.cyclops.integratedtunnels.core.network.ItemNetwork;

import java.util.Map;

import static java.util.Map.*;


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

        CounterWithCallback.builder()
        .name("integrated_dynamics_network_element_value_item_count")
        .help("Number of items per network element value")
        .labelNames("network_id", "element_id", "item_name", "item_description_id")
        .callback((callback) -> {
            Map<String, ItemMetrics> itemMap = new java.util.HashMap<String, ItemMetrics>(of());

            for (INetwork network : worldStorage.getNetworks()) {
                IFullNetworkListener[] listeners = network.getFullNetworkListeners();
                for (IFullNetworkListener listener : listeners) {
                    if (listener instanceof ItemNetwork itemNetwork) {
                        for (int j : itemNetwork.getChannels()) {
                            IIngredientComponentStorage<ItemStack, Integer> items = itemNetwork.getChannel(j);
                            for (ItemStack item: items){
                                String networkId = Integer.toString(network.hashCode());
                                String elementId = Integer.toString(itemNetwork.hashCode());

                                if (itemMap.containsKey(item.getDescriptionId())) {
                                    // It exists in the map
                                    ItemMetrics itemMetric = itemMap.get(item.getDescriptionId());
                                    itemMetric.itemCount += item.getCount();
                                    itemMap.replace(item.getDescriptionId(), itemMetric);
                                } else {
                                    // It doesn't exist in the map
                                    itemMap.put(item.getDescriptionId(), new ItemMetrics(networkId, elementId, item.getDescriptionId(), item.getCount()));
                                }
                            }
                        }
                    }
                }
            }

            itemMap.forEach((key, value) -> {
                callback.call(value.itemCount, value.networkId, value.elementId, value.itemName, key);
            });

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

    public class ItemMetrics{
        public String networkId;
        public String elementId;
        public String itemName;
        public Integer itemCount;

        public ItemMetrics(String networkId, String elementId, String itemName, Integer itemCount) {
            this.networkId = networkId;
            this.elementId = elementId;
            this.itemName = itemName;
            this.itemCount = itemCount;
        }
    }
}
