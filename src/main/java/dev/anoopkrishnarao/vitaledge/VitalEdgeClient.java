package dev.anoopkrishnarao.vitaledge;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VitalEdgeClient implements ClientModInitializer {

    public static final String MOD_ID = "vitaledge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        VitalEdgeConfigManager.load();
        LOGGER.info("VitalEdge initialised.");
    }
}
