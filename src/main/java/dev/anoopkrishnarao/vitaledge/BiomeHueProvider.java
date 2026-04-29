package dev.anoopkrishnarao.vitaledge;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class BiomeHueProvider {

    /**
     * Returns a hue shift in degrees based on the current biome group.
     * Returns 0 if biome blend is disabled or biome is temperate/nether/end.
     */
    public static float getHueShift() {
        if (!VitalEdgeConfig.biomeBlendEnabled) return 0f;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return 0f;

        BlockPos pos = mc.player.blockPosition();
        ResourceLocation biome = mc.level.getBiome(pos).unwrapKey()
            .map(key -> key.location())
            .orElse(null);

        if (biome == null) return 0f;

        String path = biome.getPath();

        return switch (getBiomeGroup(path)) {
            case LUSH       -> 15f;
            case COLD       -> -20f;
            case ARID       -> 20f;
            case DARK       -> -40f;
            case OCEAN      -> -10f;
            case MUSHROOM   -> 30f;
            default         -> 0f; // TEMPERATE, NETHER, END — no shift
        };
    }

    private enum BiomeGroup {
        TEMPERATE, LUSH, COLD, ARID, DARK, OCEAN, MUSHROOM, NETHER, END
    }

    private static BiomeGroup getBiomeGroup(String path) {
        return switch (path) {
            // Lush/Jungle
            case "jungle", "sparse_jungle", "bamboo_jungle",
                 "mangrove_swamp", "cherry_grove", "lush_caves"
                -> BiomeGroup.LUSH;

            // Cold/Snowy
            case "snowy_plains", "ice_spikes", "taiga", "snowy_taiga",
                 "old_growth_pine_taiga", "old_growth_spruce_taiga",
                 "snowy_slopes", "frozen_peaks", "grove",
                 "frozen_river", "snowy_beach", "frozen_ocean",
                 "deep_frozen_ocean"
                -> BiomeGroup.COLD;

            // Arid/Desert
            case "desert", "savanna", "savanna_plateau", "windswept_savanna",
                 "badlands", "eroded_badlands", "wooded_badlands",
                 "stony_peaks", "jagged_peaks", "warm_ocean"
                -> BiomeGroup.ARID;

            // Dark/Spooky
            case "dark_forest", "pale_garden", "swamp", "deep_dark",
                 "dripstone_caves"
                -> BiomeGroup.DARK;

            // Ocean
            case "ocean", "deep_ocean", "lukewarm_ocean",
                 "deep_lukewarm_ocean", "cold_ocean", "deep_cold_ocean"
                -> BiomeGroup.OCEAN;

            // Mushroom
            case "mushroom_fields" -> BiomeGroup.MUSHROOM;

            // Nether
            case "nether_wastes", "warped_forest", "crimson_forest",
                 "soul_sand_valley", "basalt_deltas"
                -> BiomeGroup.NETHER;

            // End
            case "the_end", "end_highlands", "end_midlands",
                 "small_end_islands", "end_barrens"
                -> BiomeGroup.END;

            // Temperate (default)
            default -> BiomeGroup.TEMPERATE;
        };
    }
}
