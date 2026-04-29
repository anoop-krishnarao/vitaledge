package dev.anoopkrishnarao.vitaledge;

public class YHeightModifier {

    // Default clamp values (will be configurable via Mod Menu in M7)
    public static float upperYClamp = 200f;
    public static float lowerYClamp = -40f;
    public static float lightCeiling = 0.25f;  // max +25% Value above Y=0
    public static float darkFloor = 0.25f;      // max -25% Value below Y=0

    /**
     * Takes a packed ARGB color and current Y position,
     * returns a brightness-adjusted packed ARGB color.
     */
    public static int modify(int argb, float y) {
        // Extract RGB channels
        int a = (argb >> 24) & 0xFF;
        float r = ((argb >> 16) & 0xFF) / 255f;
        float g = ((argb >> 8)  & 0xFF) / 255f;
        float b = (argb         & 0xFF) / 255f;

        // RGB → HSV
        float[] hsv = rgbToHsv(r, g, b);
        float hue = hsv[0];
        float sat = hsv[1];
        float val = hsv[2];

        // Apply Y modifier to Value only
        if (y > 0) {
            float t = Math.min(y / upperYClamp, 1f);
            val = val + t * lightCeiling;
        } else if (y < 0) {
            float t = Math.min(-y / -lowerYClamp, 1f);
            val = val - t * darkFloor;
        }

        val = Math.clamp(val, 0f, 1f);

        // HSV → RGB
        int[] rgb = hsvToRgb(hue, sat, val);
        return (a << 24) | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
    }

    private static float[] rgbToHsv(float r, float g, float b) {
        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float delta = max - min;

        float h = 0f;
        if (delta != 0) {
            if (max == r)      h = 60f * (((g - b) / delta) % 6);
            else if (max == g) h = 60f * (((b - r) / delta) + 2);
            else               h = 60f * (((r - g) / delta) + 4);
        }
        if (h < 0) h += 360f;

        float s = (max == 0) ? 0f : delta / max;
        return new float[]{ h, s, max };
    }

    private static int[] hsvToRgb(float h, float s, float v) {
        float hh = h / 60f;
        int i = (int) Math.floor(hh);
        float f = hh - i;
        float p = v * (1f - s);
        float q = v * (1f - s * f);
        float t = v * (1f - s * (1f - f));

        float r, g, b;
        switch (i % 6) {
            case 0 -> { r = v; g = t; b = p; }
            case 1 -> { r = q; g = v; b = p; }
            case 2 -> { r = p; g = v; b = t; }
            case 3 -> { r = p; g = q; b = v; }
            case 4 -> { r = t; g = p; b = v; }
            default -> { r = v; g = p; b = q; }
        }

        return new int[]{
            Math.clamp(Math.round(r * 255), 0, 255),
            Math.clamp(Math.round(g * 255), 0, 255),
            Math.clamp(Math.round(b * 255), 0, 255)
        };
    }
}
