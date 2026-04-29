package dev.anoopkrishnarao.vitaledge;

public class DurabilityColorMapper {

    private static final float PULSE_PERIOD_MS = 1500f;
    private static final float PULSE_MIN_V = 0.55f;
    private static final float PULSE_MAX_V = 1.0f;

    /**
     * Returns a packed ARGB int for the given durability percent,
     * dimension hue shift, and biome hue shift.
     */
    public static int getColor(float durabilityPercent, float dimensionHueShift, float biomeHueShift) {
        float hue;
        float saturation = 1.0f;
        float value;

        if (durabilityPercent >= 0.70f) {
            hue = 120f;
            value = 1.0f;
        } else if (durabilityPercent >= 0.40f) {
            hue = 45f;
            value = 1.0f;
        } else if (durabilityPercent >= 0.15f) {
            hue = 25f;
            value = 1.0f;
        } else if (durabilityPercent >= 0.05f) {
            hue = 0f;
            value = 1.0f;
        } else {
            // Pulsing red — biome blend overridden, pure red pulse
            hue = 0f;
            float pulse = (float) Math.sin(
                (System.currentTimeMillis() % (long) PULSE_PERIOD_MS)
                / PULSE_PERIOD_MS * 2.0 * Math.PI
            );
            value = PULSE_MIN_V + (pulse + 1f) / 2f * (PULSE_MAX_V - PULSE_MIN_V);
            // Apply dimension shift only — no biome blend when near-broken
            hue = (hue + dimensionHueShift + 360f) % 360f;
            return hsvToArgb(hue, saturation, value);
        }

        // Apply dimension hue shift
        hue = (hue + dimensionHueShift + 360f) % 360f;

        // Apply biome hue blend scaled by blend strength
        hue = (hue + biomeHueShift * VitalEdgeConfig.biomeBlendStrength + 360f) % 360f;

        return hsvToArgb(hue, saturation, value);
    }

    private static int hsvToArgb(float hue, float saturation, float value) {
        float h = hue / 60f;
        int i = (int) Math.floor(h);
        float f = h - i;
        float p = value * (1f - saturation);
        float q = value * (1f - saturation * f);
        float t = value * (1f - saturation * (1f - f));

        float r, g, b;
        switch (i % 6) {
            case 0 -> { r = value; g = t; b = p; }
            case 1 -> { r = q; g = value; b = p; }
            case 2 -> { r = p; g = value; b = t; }
            case 3 -> { r = p; g = q; b = value; }
            case 4 -> { r = t; g = p; b = value; }
            default -> { r = value; g = p; b = q; }
        }

        int ri = Math.clamp(Math.round(r * 255), 0, 255);
        int gi = Math.clamp(Math.round(g * 255), 0, 255);
        int bi = Math.clamp(Math.round(b * 255), 0, 255);

        return (0xFF << 24) | (ri << 16) | (gi << 8) | bi;
    }
}
