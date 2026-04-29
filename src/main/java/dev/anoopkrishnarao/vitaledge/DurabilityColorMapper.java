package dev.anoopkrishnarao.vitaledge;

public class DurabilityColorMapper {

    // Pulse: 1.5s period, Value oscillates between 0.55 and 1.0
    private static final float PULSE_PERIOD_MS = 1500f;
    private static final float PULSE_MIN_V = 0.55f;
    private static final float PULSE_MAX_V = 1.0f;

    /**
     * Returns a packed ARGB int for the given durability percent.
     * durabilityPercent: 0.0 (broken) to 1.0 (full)
     */
    public static int getColor(float durabilityPercent) {
        float hue;
        float saturation = 1.0f;
        float value;

        if (durabilityPercent >= 0.70f) {
            // Green
            hue = 120f;
            value = 1.0f;
        } else if (durabilityPercent >= 0.40f) {
            // Amber
            hue = 45f;
            value = 1.0f;
        } else if (durabilityPercent >= 0.15f) {
            // Orange
            hue = 25f;
            value = 1.0f;
        } else if (durabilityPercent >= 0.05f) {
            // Red
            hue = 0f;
            value = 1.0f;
        } else {
            // Pulsing red — sine wave on Value
            hue = 0f;
            float pulse = (float) Math.sin(
                (System.currentTimeMillis() % (long) PULSE_PERIOD_MS)
                / PULSE_PERIOD_MS * 2.0 * Math.PI
            );
            // Map sine (-1..1) to (PULSE_MIN_V..PULSE_MAX_V)
            value = PULSE_MIN_V + (pulse + 1f) / 2f * (PULSE_MAX_V - PULSE_MIN_V);
        }

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
