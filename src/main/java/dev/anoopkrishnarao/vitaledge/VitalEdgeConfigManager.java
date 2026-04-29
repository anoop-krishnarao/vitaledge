package dev.anoopkrishnarao.vitaledge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VitalEdgeConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir()
        .resolve("vitaledge.json");

    private static class ConfigData {
        boolean enabled = true;
        float edgeThickness = 0.05f;
        int stepCount = 12;
        float smoothness = 0.8f;
        float opacity = 0.35f;
        boolean surgeEnabled = true;
        boolean biomeBlendEnabled = true;
        float biomeBlendStrength = 0.5f;
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save(); // write defaults on first run
            return;
        }

        try {
            String json = Files.readString(CONFIG_PATH);
            ConfigData data = GSON.fromJson(json, ConfigData.class);
            if (data == null) return;

            VitalEdgeConfig.enabled = data.enabled;
            VitalEdgeConfig.edgeThickness = data.edgeThickness;
            VitalEdgeConfig.stepCount = data.stepCount;
            VitalEdgeConfig.smoothness = data.smoothness;
            VitalEdgeConfig.opacity = data.opacity;
            VitalEdgeConfig.surgeEnabled = data.surgeEnabled;
            VitalEdgeConfig.biomeBlendEnabled = data.biomeBlendEnabled;
            VitalEdgeConfig.biomeBlendStrength = data.biomeBlendStrength;

            VitalEdgeClient.LOGGER.info("VitalEdge | Config loaded.");
        } catch (IOException e) {
            VitalEdgeClient.LOGGER.error("VitalEdge | Failed to load config: {}", e.getMessage());
        }
    }

    public static void save() {
        ConfigData data = new ConfigData();
        data.enabled = VitalEdgeConfig.enabled;
        data.edgeThickness = VitalEdgeConfig.edgeThickness;
        data.stepCount = VitalEdgeConfig.stepCount;
        data.smoothness = VitalEdgeConfig.smoothness;
        data.opacity = VitalEdgeConfig.opacity;
        data.surgeEnabled = VitalEdgeConfig.surgeEnabled;
        data.biomeBlendEnabled = VitalEdgeConfig.biomeBlendEnabled;
        data.biomeBlendStrength = VitalEdgeConfig.biomeBlendStrength;

        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(data));
            VitalEdgeClient.LOGGER.info("VitalEdge | Config saved.");
        } catch (IOException e) {
            VitalEdgeClient.LOGGER.error("VitalEdge | Failed to save config: {}", e.getMessage());
        }
    }
}
