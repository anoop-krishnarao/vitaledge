package dev.anoopkrishnarao.vitaledge;

public class VitalEdgeConfig {

    // Whether the overlay is enabled at all
    public static boolean enabled = true;

    // Edge thickness as fraction of shorter screen dimension (0.05 to 0.40)
    public static float edgeThickness = 0.15f;

    // Number of gradient bands within the edge thickness (2 to 20)
    public static int stepCount = 8;

    // Smoothness of transitions between steps (0.0 = sharp, 1.0 = fully blended)
    public static float smoothness = 0.5f;

    // Master opacity of the overlay (0.1 to 1.0)
    public static float opacity = 0.70f;

    // Whether damage surge pulse is enabled
    public static boolean surgeEnabled = true;

    // Whether biome hue blend is enabled
    public static boolean biomeBlendEnabled = true;

    // Strength of biome hue blend (0.0 = no blend, 1.0 = full shift)
    public static float biomeBlendStrength = 0.5f;
}
