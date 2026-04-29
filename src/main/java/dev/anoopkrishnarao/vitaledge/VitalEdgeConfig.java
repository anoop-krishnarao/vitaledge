package dev.anoopkrishnarao.vitaledge;

public class VitalEdgeConfig {

    // Whether the overlay is enabled at all
    public static boolean enabled = true;

    // Edge thickness as fraction of shorter screen dimension (0.03 to 0.08)
    public static float edgeThickness = 0.055f;

    // Number of gradient bands within the edge thickness (8 to 20)
    public static int stepCount = 14;

    // Gradient style: true = stepped (blocky), false = smooth (exponential)
    public static boolean steppedMode = false;

    // Master opacity of the overlay (0.0 to 0.50)
    public static float opacity = 0.25f;

    // Whether damage surge pulse is enabled
    public static boolean surgeEnabled = true;

    // Whether biome hue blend is enabled
    public static boolean biomeBlendEnabled = true;

    // Strength of biome hue blend (0.0 = no blend, 1.0 = full shift)
    public static float biomeBlendStrength = 0.5f;
}
