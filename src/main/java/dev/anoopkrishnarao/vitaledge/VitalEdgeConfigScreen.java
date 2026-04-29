package dev.anoopkrishnarao.vitaledge;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public class VitalEdgeConfigScreen extends Screen {

    private final Screen parent;

    private float edgeThickness = VitalEdgeConfig.edgeThickness;
    private int stepCount = VitalEdgeConfig.stepCount;
    private float smoothness = VitalEdgeConfig.smoothness;
    private float opacity = VitalEdgeConfig.opacity;
    private float biomeBlendStrength = VitalEdgeConfig.biomeBlendStrength;

    private final Map<AbstractWidget, String> descriptions = new HashMap<>();
    private String hoveredDescription = "";

    public VitalEdgeConfigScreen(Screen parent) {
        super(Component.literal("Vital Edge Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 40;
        int sliderWidth = 200;
        int rowHeight = 28;

        // Enabled toggle
        var overlayBtn = this.addRenderableWidget(Button.builder(
            Component.literal("Overlay: " + (VitalEdgeConfig.enabled ? "ON" : "OFF")),
            btn -> {
                VitalEdgeConfig.enabled = !VitalEdgeConfig.enabled;
                btn.setMessage(Component.literal("Overlay: " + (VitalEdgeConfig.enabled ? "ON" : "OFF")));
            })
            .bounds(centerX - sliderWidth / 2, startY, sliderWidth, 20)
            .build()
        );
        descriptions.put(overlayBtn, "Toggles the entire overlay on or off.");

        // Edge thickness slider
        var thicknessSlider = this.addRenderableWidget(new AbstractSliderButton(
            centerX - sliderWidth / 2, startY + rowHeight, sliderWidth, 20,
            Component.literal("Edge Thickness: " + Math.round(edgeThickness * 100) + "%"),
            (edgeThickness - 0.03f) / 0.17f
        ) {
            @Override
            protected void updateMessage() {
                edgeThickness = 0.03f + (float) this.value * 0.17f;
                this.setMessage(Component.literal("Edge Thickness: " + Math.round(edgeThickness * 100) + "%"));
            }
            @Override
            protected void applyValue() {
                edgeThickness = 0.03f + (float) this.value * 0.17f;
            }
        });
        descriptions.put(thicknessSlider, "How far the gradient bleeds inward from each edge.");

        // Step count slider
        var stepsSlider = this.addRenderableWidget(new AbstractSliderButton(
            centerX - sliderWidth / 2, startY + rowHeight * 2, sliderWidth, 20,
            Component.literal("Steps: " + stepCount),
            (stepCount - 2) / 18.0
        ) {
            @Override
            protected void updateMessage() {
                stepCount = 2 + (int) Math.round(this.value * 18);
                this.setMessage(Component.literal("Steps: " + stepCount));
            }
            @Override
            protected void applyValue() {
                stepCount = 2 + (int) Math.round(this.value * 18);
            }
        });
        descriptions.put(stepsSlider, "Number of bands in the gradient. Low = blocky, high = smooth.");

        // Smoothness slider
        var smoothnessSlider = this.addRenderableWidget(new AbstractSliderButton(
            centerX - sliderWidth / 2, startY + rowHeight * 3, sliderWidth, 20,
            Component.literal("Smoothness: " + Math.round(smoothness * 100) + "%"),
            smoothness
        ) {
            @Override
            protected void updateMessage() {
                smoothness = (float) this.value;
                this.setMessage(Component.literal("Smoothness: " + Math.round(smoothness * 100) + "%"));
            }
            @Override
            protected void applyValue() {
                smoothness = (float) this.value;
            }
        });
        descriptions.put(smoothnessSlider, "How softly each band blends into the next.");

        // Opacity slider
        var opacitySlider = this.addRenderableWidget(new AbstractSliderButton(
            centerX - sliderWidth / 2, startY + rowHeight * 4, sliderWidth, 20,
            Component.literal("Opacity: " + Math.round(opacity * 100) + "%"),
            (opacity - 0.1f) / 0.9f
        ) {
            @Override
            protected void updateMessage() {
                opacity = 0.1f + (float) this.value * 0.9f;
                this.setMessage(Component.literal("Opacity: " + Math.round(opacity * 100) + "%"));
            }
            @Override
            protected void applyValue() {
                opacity = 0.1f + (float) this.value * 0.9f;
            }
        });
        descriptions.put(opacitySlider, "Master transparency of the overlay. Lower = more subtle.");

        // Damage surge toggle
        var surgeBtn = this.addRenderableWidget(Button.builder(
            Component.literal("Damage Surge: " + (VitalEdgeConfig.surgeEnabled ? "ON" : "OFF")),
            btn -> {
                VitalEdgeConfig.surgeEnabled = !VitalEdgeConfig.surgeEnabled;
                btn.setMessage(Component.literal("Damage Surge: " + (VitalEdgeConfig.surgeEnabled ? "ON" : "OFF")));
            })
            .bounds(centerX - sliderWidth / 2, startY + rowHeight * 5, sliderWidth, 20)
            .build()
        );
        descriptions.put(surgeBtn, "Pulses the overlay when you take damage.");

        // Biome blend toggle
        var biomeBtn = this.addRenderableWidget(Button.builder(
            Component.literal("Biome Blend: " + (VitalEdgeConfig.biomeBlendEnabled ? "ON" : "OFF")),
            btn -> {
                VitalEdgeConfig.biomeBlendEnabled = !VitalEdgeConfig.biomeBlendEnabled;
                btn.setMessage(Component.literal("Biome Blend: " + (VitalEdgeConfig.biomeBlendEnabled ? "ON" : "OFF")));
            })
            .bounds(centerX - sliderWidth / 2, startY + rowHeight * 6, sliderWidth, 20)
            .build()
        );
        descriptions.put(biomeBtn, "Tints the overlay based on your current biome.");

        // Biome blend strength slider
        var blendSlider = this.addRenderableWidget(new AbstractSliderButton(
            centerX - sliderWidth / 2, startY + rowHeight * 7, sliderWidth, 20,
            Component.literal("Blend Strength: " + Math.round(biomeBlendStrength * 100) + "%"),
            biomeBlendStrength
        ) {
            @Override
            protected void updateMessage() {
                biomeBlendStrength = (float) this.value;
                this.setMessage(Component.literal("Blend Strength: " + Math.round(biomeBlendStrength * 100) + "%"));
            }
            @Override
            protected void applyValue() {
                biomeBlendStrength = (float) this.value;
            }
        });
        descriptions.put(blendSlider, "How strongly the biome tint affects the overlay color.");

        // Done button
        this.addRenderableWidget(Button.builder(
            Component.literal("Done"),
            btn -> this.onClose())
            .bounds(centerX - sliderWidth / 2, startY + rowHeight * 8 + 10, sliderWidth, 20)
            .build()
        );
    }

    @Override
    public void onClose() {
        VitalEdgeConfig.edgeThickness = edgeThickness;
        VitalEdgeConfig.stepCount = stepCount;
        VitalEdgeConfig.smoothness = smoothness;
        VitalEdgeConfig.opacity = opacity;
        VitalEdgeConfig.biomeBlendStrength = biomeBlendStrength;
        VitalEdgeConfigManager.save();
        this.minecraft.setScreen(parent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, delta);

        // Update hovered description
        hoveredDescription = "";
        for (var entry : descriptions.entrySet()) {
            AbstractWidget widget = entry.getKey();
            if (widget.isHovered()) {
                hoveredDescription = entry.getValue();
                break;
            }
        }

        // Render description below Done button
        if (!hoveredDescription.isEmpty()) {
            int descY = 40 + 28 * 8 + 10 + 28;
            graphics.drawCenteredString(this.font, hoveredDescription, this.width / 2, descY, 0xAAAAAA);
        }
    }
}
