package dev.anoopkrishnarao.vitaledge;

import net.minecraft.client.gui.GuiGraphics;

public class EdgeGradientRenderer {

    public static void render(GuiGraphics graphics, int screenWidth, int screenHeight, int argbColor) {
        if (!VitalEdgeConfig.enabled) return;
        if (!ArmorDurabilityTracker.hasArmor()) return;

        int shorter = Math.min(screenWidth, screenHeight);
        int thickness = Math.round(shorter * VitalEdgeConfig.edgeThickness);
        float opacity = VitalEdgeConfig.opacity;

        float pulse = DamageSurgeTracker.getPulseMultiplier(ArmorDurabilityTracker.getDurabilityPercent());
        opacity = Math.clamp(opacity * pulse, 0f, 1f);

        int r = (argbColor >> 16) & 0xFF;
        int g = (argbColor >> 8)  & 0xFF;
        int b = argbColor         & 0xFF;

        if (VitalEdgeConfig.steppedMode) {
            renderStepped(graphics, screenWidth, screenHeight, thickness, opacity, r, g, b);
        } else {
            renderSmooth(graphics, screenWidth, screenHeight, thickness, opacity, r, g, b);
        }
    }

    private static void renderStepped(GuiGraphics graphics, int screenWidth, int screenHeight,
                                       int thickness, float opacity, int r, int g, int b) {
        int steps = VitalEdgeConfig.stepCount;
        int stepSize = Math.max(1, thickness / steps);

        for (int i = 0; i < steps; i++) {
            float t = (float) i / (steps - 1);
            float bandAlpha = Math.clamp((1.0f - t) * opacity, 0f, 1f);
            int a = Math.round(bandAlpha * 255);
            int color = (a << 24) | (r << 16) | (g << 8) | b;
            int offset = i * stepSize;
            fillEdges(graphics, screenWidth, screenHeight, offset, stepSize, color);
        }
    }

    private static void renderSmooth(GuiGraphics graphics, int screenWidth, int screenHeight,
                                      int thickness, float opacity, int r, int g, int b) {
        // Render pixel by pixel for true smooth exponential decay
        for (int i = 0; i < thickness; i++) {
            float t = (float) i / thickness;
            float bandAlpha = Math.clamp((float) Math.pow(1.0f - t, 2.5f) * opacity, 0f, 1f);
            int a = Math.round(bandAlpha * 255);
            int color = (a << 24) | (r << 16) | (g << 8) | b;
            fillEdges(graphics, screenWidth, screenHeight, i, 1, color);
        }
    }

    private static void fillEdges(GuiGraphics graphics, int screenWidth, int screenHeight,
                                   int offset, int size, int color) {
        graphics.fill(0, offset, screenWidth, offset + size, color);
        graphics.fill(0, screenHeight - offset - size, screenWidth, screenHeight - offset, color);
        graphics.fill(offset, 0, offset + size, screenHeight, color);
        graphics.fill(screenWidth - offset - size, 0, screenWidth - offset, screenHeight, color);
    }
}
