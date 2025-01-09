package org.b1scuit.integratedprometheus.prometheus;

import com.mojang.logging.LogUtils;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import org.b1scuit.integratedprometheus.network.Network;
import org.slf4j.Logger;

import java.io.IOException;

public class Prometheus {
    protected final Network network = new Network();

    private static final Logger LOGGER = LogUtils.getLogger();

    public void initialize() throws InterruptedException, IOException{
        JvmMetrics.builder().register();
        HTTPServer server = HTTPServer.builder()
                .port(9400)
                .buildAndStart();
        LOGGER.info("HTTPServer listening on port http://localhost:" + server.getPort() + "/metrics");

        network.GetNetwork();
    }
}
