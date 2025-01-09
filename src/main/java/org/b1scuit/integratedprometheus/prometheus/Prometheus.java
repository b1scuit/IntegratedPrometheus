package org.b1scuit.integratedprometheus.prometheus;

import com.mojang.logging.LogUtils;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import org.b1scuit.integratedprometheus.metricsmanager.MetricsManager;
import org.slf4j.Logger;

import java.io.IOException;

public class Prometheus {
    private static final Logger LOGGER = LogUtils.getLogger();

    public void initialize() throws InterruptedException, IOException{
        // Keeping this here in cause Luke wants it
        // JvmMetrics.builder().register();
        new MetricsManager();

        HTTPServer server = HTTPServer.builder()
                .port(9400).buildAndStart();
        LOGGER.info("HTTPServer listening on port http://localhost:" + server.getPort() + "/metrics");
    }
}
