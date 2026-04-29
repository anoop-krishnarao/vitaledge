package dev.anoopkrishnarao.vitaledge;

import net.minecraft.client.gui.GuiGraphics;

public class EdgeGradientRenderer {

    public static void render(GuiGraphics graphics, int screenWidth, int screenHeight, int argbColor) {
        if (!VitalEdgeConfig.enabled) return;
        if (!ArmorDurabilityTracker.hasArmor()) return;

        int shorter = Math.min(screenWidth, screenHeight);
        int thickness = Math.round(shorter * VitalEdgeConfig.edgeThickness);
        int steps = VitalEdgeConfig.stepCount;
        float smoothness = VitalEdgeConfig.smoothness;
        float opacity = VitalEdgeConfig.opacity;

        // Apply pulse multiplier
        float pulse = DamageSurgeTracker.getPulseMultiplier(ArmorDurabilityTracker.getDurabilityPercent());
        opacity = Math.clamp(opacity * pulse, 0f, 1f);

        int r = (argbColor >> 16) & 0xFF;
        int g = (argbColor >> 8)  & 0xFF;
        int b = argbColor         & 0xFF;

        int stepSize = Math.max(1, thickness / steps);

        for (int i = 0; i < steps; i++) {
            float t = (float) i / (steps - 1);

            float sharpAlpha = (i == 0) ? 1.0f : (1.0f - (float) i / steps);
            float smoothAlpha = (float) Math.pow(1.0f - t, 2.0f);
            float bandAlpha = sharpAlpha + smoothness * (smoothAlpha - sharpAlpha);
            bandAlpha = Math.clamp(bandAlpha * opacity, 0f, 1f);

            int a = Math.round(bandAlpha * 255);
            int color = (a << 24) | (r << 16) | (g << 8) | b;

            int offset = i * stepSize;

            // Top edge
            graphics.fill(0, offset, screenWidth, offset + stepSize, color);
            // Bottom edge
            graphics.fill(0, screenHeight - offset - stepSize, screenWidth, screenHeight - offset, color);
            // Left edge
            graphics.fill(offset, offset + stepSize, offset + stepSize, screenHeight - offset - stepSize, color);
            // Right edge
            graphics.fill(screenWidth - offset - stepSize, offset + stepSize, screenWidth - offset, screenHeight - offset - stepSize, color);
        }
    }
}
