package dev.anoopkrishnarao.vitaledge;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class VitalEdgeConfigScreen extends Screen {

    private final Screen parent;

    // Slider state
    private float edgeThickness = VitalEdgeConfig.edgeThickness;
    private int stepCount = VitalEdgeConfig.stepCount;
    private float smoothness = VitalEdgeConfig.smoothness;
    private float opacity = VitalEdgeConfig.opacity;

    public VitalEdgeConfigScreen(Screen parent) {
        super(Component.literal("VitalEdge Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 40;
        int sliderWidth = 200;
        int rowHeight = 28;

        // Enabled toggle
        this.addRenderableWidget(Button.builder(
            Component.literal("Overlay: " + (VitalEdgeConfig.enabled ? "ON" : "OFF")),
            btn -> {
                VitalEdgeConfig.enabled = !VitalEdgeConfig.enabled;
                btn.setMessage(Component.literal("Overlay: " + (VitalEdgeConfig.enabled ? "ON" : "OFF")));
            })
            .bounds(centerX - sliderWidth / 2, startY, sliderWidth, 20)
            .build()
        );

        // Edge thickness slider
        this.addRenderableWidget(new AbstractSliderButton(
            centerX - sliderWidth / 2, startY + rowHeight, sliderWidth, 20,
            Component.literal("Edge Thickness: " + Math.round(edgeThickness * 100) + "%"),
            // value: 0.0 = 5%, 1.0 = 40%
            (edgeThickness - 0.05f) / 0.35f
        ) {
            @Override
            protected void updateMessage() {
                edgeThickness = 0.05f + (float) this.value * 0.35f;
                this.setMessage(Component.literal("Edge Thickness: " + Math.round(edgeThickness * 100) + "%"));
            }
            @Override
            protected void applyValue() {
                edgeThickness = 0.05f + (float) this.value * 0.35f;
            }
        });

        // Step count slider
        this.addRenderableWidget(new AbstractSliderButton(
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

        // Smoothness slider
        this.addRenderableWidget(new AbstractSliderButton(
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

        // Opacity slider
        this.addRenderableWidget(new AbstractSliderButton(
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

        // Damage surge toggle
        this.addRenderableWidget(Button.builder(
            Component.literal("Damage Surge: " + (VitalEdgeConfig.surgeEnabled ? "ON" : "OFF")),
            btn -> {
                VitalEdgeConfig.surgeEnabled = !VitalEdgeConfig.surgeEnabled;
                btn.setMessage(Component.literal("Damage Surge: " + (VitalEdgeConfig.surgeEnabled ? "ON" : "OFF")));
            })
            .bounds(centerX - sliderWidth / 2, startY + rowHeight * 5, sliderWidth, 20)
            .build()
        );

        // Done button
        this.addRenderableWidget(Button.builder(
            Component.literal("Done"),
            btn -> this.onClose())
            .bounds(centerX - sliderWidth / 2, startY + rowHeight * 6 + 10, sliderWidth, 20)
            .build()
        );
    }

    @Override
    public void onClose() {
        // Write local state to config
        VitalEdgeConfig.edgeThickness = edgeThickness;
        VitalEdgeConfig.stepCount = stepCount;
        VitalEdgeConfig.smoothness = smoothness;
        VitalEdgeConfig.opacity = opacity;
        this.minecraft.setScreen(parent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, delta);
    }
}
