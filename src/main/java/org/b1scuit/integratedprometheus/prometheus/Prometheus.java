package org.b1scuit.integratedprometheus.prometheus;

import com.mojang.logging.LogUtils;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;

public class Prometheus {
    private static final Logger LOGGER = LogUtils.getLogger();

    public void initialize() throws InterruptedException, IOException{
        JvmMetrics.builder().register();
        InetAddress inetAddress = InetAddress.getByName("0.0.0.0");

        HTTPServer server = HTTPServer.builder()
                .inetAddress(inetAddress)
                .port(9400).buildAndStart();
        LOGGER.info("HTTPServer listening on port http://"+inetAddress+" :" + server.getPort() + "/metrics");
    }

}
