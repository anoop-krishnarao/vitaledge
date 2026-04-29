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
        float edgeThickness = 0.055f;
        int stepCount = 14;
        boolean steppedMode = false;
        float opacity = 0.25f;
        boolean surgeEnabled = true;
        boolean biomeBlendEnabled = true;
        float biomeBlendStrength = 0.5f;
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }

        try {
            String json = Files.readString(CONFIG_PATH);
            ConfigData data = GSON.fromJson(json, ConfigData.class);
            if (data == null) return;

            VitalEdgeConfig.enabled = data.enabled;
            VitalEdgeConfig.edgeThickness = Math.clamp(data.edgeThickness, 0.03f, 0.08f);
            VitalEdgeConfig.stepCount = Math.clamp(data.stepCount, 8, 20);
            VitalEdgeConfig.steppedMode = data.steppedMode;
            VitalEdgeConfig.opacity = Math.clamp(data.opacity, 0f, 0.5f);
            VitalEdgeConfig.surgeEnabled = data.surgeEnabled;
            VitalEdgeConfig.biomeBlendEnabled = data.biomeBlendEnabled;
            VitalEdgeConfig.biomeBlendStrength = Math.clamp(data.biomeBlendStrength, 0f, 1f);

            VitalEdgeClient.LOGGER.info("Vital Edge | Config loaded.");
        } catch (IOException e) {
            VitalEdgeClient.LOGGER.error("Vital Edge | Config failed to load: {}", e.getMessage());
        }
    }

    public static void save() {
        ConfigData data = new ConfigData();
        data.enabled = VitalEdgeConfig.enabled;
        data.edgeThickness = VitalEdgeConfig.edgeThickness;
        data.stepCount = VitalEdgeConfig.stepCount;
        data.steppedMode = VitalEdgeConfig.steppedMode;
        data.opacity = VitalEdgeConfig.opacity;
        data.surgeEnabled = VitalEdgeConfig.surgeEnabled;
        data.biomeBlendEnabled = VitalEdgeConfig.biomeBlendEnabled;
        data.biomeBlendStrength = VitalEdgeConfig.biomeBlendStrength;

        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(data));
            VitalEdgeClient.LOGGER.info("Vital Edge | Config saved.");
        } catch (IOException e) {
            VitalEdgeClient.LOGGER.error("Vital Edge | Config failed to save: {}", e.getMessage());
        }
    }
}
