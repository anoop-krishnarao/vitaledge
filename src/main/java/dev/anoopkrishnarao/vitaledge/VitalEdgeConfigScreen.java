package dev.anoopkrishnarao.vitaledge;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VitalEdgeConfigScreen extends Screen {

    private final Screen parent;

    private float edgeThickness = VitalEdgeConfig.edgeThickness;
    private int stepCount = VitalEdgeConfig.stepCount;
    private float opacity = VitalEdgeConfig.opacity;
    private float biomeBlendStrength = VitalEdgeConfig.biomeBlendStrength;

    private final Map<AbstractWidget, String> descriptions = new HashMap<>();
    private final List<AbstractWidget> scrollableWidgets = new ArrayList<>();
    private Button doneButton;
    private String hoveredDescription = "";

    private int scrollOffset = 0;
    private static final int ROW_HEIGHT = 28;
    private static final int SLIDER_WIDTH = 200;
    private static final int HEADER_HEIGHT = 40;
    private static final int FOOTER_HEIGHT = 50;
    private static final int PADDING = 10;
    private static final int SCROLLBAR_WIDTH = 6;
    private static final int SCROLLBAR_MARGIN = 4;

    public VitalEdgeConfigScreen(Screen parent) {
        super(Component.literal("Vital Edge Settings"));
        this.parent = parent;
    }

    private int getTotalContentHeight() {
        return ROW_HEIGHT * 8 + PADDING * 2;
    }

    private int getVisibleHeight() {
        return this.height - HEADER_HEIGHT - FOOTER_HEIGHT;
    }

    private int getMaxScroll() {
        return Math.max(0, getTotalContentHeight() - getVisibleHeight());
    }

    @Override
    public void resize(net.minecraft.client.Minecraft minecraft, int width, int height) {
        scrollOffset = 0;
        super.resize(minecraft, width, height);
    }

    @Override
    protected void init() {
        scrollableWidgets.clear();
        descriptions.clear();

        int centerX = this.width / 2;
        int baseY = HEADER_HEIGHT + PADDING;

        var overlayBtn = Button.builder(
            Component.literal("Overlay: " + (VitalEdgeConfig.enabled ? "ON" : "OFF")),
            btn -> {
                VitalEdgeConfig.enabled = !VitalEdgeConfig.enabled;
                btn.setMessage(Component.literal("Overlay: " + (VitalEdgeConfig.enabled ? "ON" : "OFF")));
            })
            .bounds(centerX - SLIDER_WIDTH / 2, baseY, SLIDER_WIDTH, 20)
            .build();
        descriptions.put(overlayBtn, "Toggles the entire overlay on or off.");
        scrollableWidgets.add(overlayBtn);

        var thicknessSlider = new AbstractSliderButton(
            centerX - SLIDER_WIDTH / 2, baseY + ROW_HEIGHT, SLIDER_WIDTH, 20,
            Component.literal("Edge Thickness: " + String.format("%.1f", edgeThickness * 100) + "%"),
            (edgeThickness - 0.03f) / 0.05f) {
            @Override protected void updateMessage() {
                edgeThickness = 0.03f + (float) this.value * 0.05f;
                this.setMessage(Component.literal("Edge Thickness: " + String.format("%.1f", edgeThickness * 100) + "%"));
            }
            @Override protected void applyValue() {
                edgeThickness = 0.03f + (float) this.value * 0.05f;
            }
        };
        descriptions.put(thicknessSlider, "How far the gradient bleeds inward from each edge.");
        scrollableWidgets.add(thicknessSlider);

        var stepsSlider = new AbstractSliderButton(
            centerX - SLIDER_WIDTH / 2, baseY + ROW_HEIGHT * 2, SLIDER_WIDTH, 20,
            Component.literal("Steps: " + stepCount),
            (stepCount - 8) / 12.0) {
            @Override protected void updateMessage() {
                stepCount = 8 + (int) Math.round(this.value * 12);
                this.setMessage(Component.literal("Steps: " + stepCount));
            }
            @Override protected void applyValue() {
                stepCount = 8 + (int) Math.round(this.value * 12);
            }
        };
        descriptions.put(stepsSlider, "Number of bands in the gradient. Only applies in Stepped mode.");
        scrollableWidgets.add(stepsSlider);

        var styleBtn = Button.builder(
            Component.literal("Gradient: " + (VitalEdgeConfig.steppedMode ? "Stepped" : "Smooth")),
            btn -> {
                VitalEdgeConfig.steppedMode = !VitalEdgeConfig.steppedMode;
                btn.setMessage(Component.literal("Gradient: " + (VitalEdgeConfig.steppedMode ? "Stepped" : "Smooth")));
            })
            .bounds(centerX - SLIDER_WIDTH / 2, baseY + ROW_HEIGHT * 3, SLIDER_WIDTH, 20)
            .build();
        descriptions.put(styleBtn, "Stepped: Discrete bands for a blocky look. | Smooth: Soft exponential fade.");
        scrollableWidgets.add(styleBtn);

        var opacitySlider = new AbstractSliderButton(
            centerX - SLIDER_WIDTH / 2, baseY + ROW_HEIGHT * 4, SLIDER_WIDTH, 20,
            Component.literal("Opacity: " + Math.round(opacity * 100) + "%"),
            opacity / 0.5f) {
            @Override protected void updateMessage() {
                opacity = (float) this.value * 0.5f;
                this.setMessage(Component.literal("Opacity: " + Math.round(opacity * 100) + "%"));
            }
            @Override protected void applyValue() {
                opacity = (float) this.value * 0.5f;
            }
        };
        descriptions.put(opacitySlider, "Master transparency. Max 50% to avoid visual clutter.");
        scrollableWidgets.add(opacitySlider);

        var surgeBtn = Button.builder(
            Component.literal("Damage Surge: " + (VitalEdgeConfig.surgeEnabled ? "ON" : "OFF")),
            btn -> {
                VitalEdgeConfig.surgeEnabled = !VitalEdgeConfig.surgeEnabled;
                btn.setMessage(Component.literal("Damage Surge: " + (VitalEdgeConfig.surgeEnabled ? "ON" : "OFF")));
            })
            .bounds(centerX - SLIDER_WIDTH / 2, baseY + ROW_HEIGHT * 5, SLIDER_WIDTH, 20)
            .build();
        descriptions.put(surgeBtn, "Pulses the overlay when you take damage.");
        scrollableWidgets.add(surgeBtn);

        var biomeBtn = Button.builder(
            Component.literal("Biome Blend: " + (VitalEdgeConfig.biomeBlendEnabled ? "ON" : "OFF")),
            btn -> {
                VitalEdgeConfig.biomeBlendEnabled = !VitalEdgeConfig.biomeBlendEnabled;
                btn.setMessage(Component.literal("Biome Blend: " + (VitalEdgeConfig.biomeBlendEnabled ? "ON" : "OFF")));
            })
            .bounds(centerX - SLIDER_WIDTH / 2, baseY + ROW_HEIGHT * 6, SLIDER_WIDTH, 20)
            .build();
        descriptions.put(biomeBtn, "Tints the overlay based on your current biome.");
        scrollableWidgets.add(biomeBtn);

        var blendSlider = new AbstractSliderButton(
            centerX - SLIDER_WIDTH / 2, baseY + ROW_HEIGHT * 7, SLIDER_WIDTH, 20,
            Component.literal("Blend Strength: " + Math.round(biomeBlendStrength * 100) + "%"),
            biomeBlendStrength) {
            @Override protected void updateMessage() {
                biomeBlendStrength = (float) this.value;
                this.setMessage(Component.literal("Blend Strength: " + Math.round(biomeBlendStrength * 100) + "%"));
            }
            @Override protected void applyValue() {
                biomeBlendStrength = (float) this.value;
            }
        };
        descriptions.put(blendSlider, "How strongly the biome tint affects overlay color.");
        scrollableWidgets.add(blendSlider);

        // Done button — always fixed, never in scrollable list
        doneButton = this.addRenderableWidget(Button.builder(
            Component.literal("Done"),
            btn -> this.onClose())
            .bounds(this.width / 2 - SLIDER_WIDTH / 2, this.height - FOOTER_HEIGHT + 26, SLIDER_WIDTH, 20)
            .build());

        updateScrollableWidgetPositions();
    }

    private void updateScrollableWidgetPositions() {
        // Remove only scrollable widgets, keep done button
        for (AbstractWidget w : scrollableWidgets) {
            this.removeWidget(w);
        }
        int centerX = this.width / 2;
        int baseY = HEADER_HEIGHT + PADDING - scrollOffset;
        for (int i = 0; i < scrollableWidgets.size(); i++) {
            AbstractWidget w = scrollableWidgets.get(i);
            w.setX(centerX - SLIDER_WIDTH / 2);
            w.setY(baseY + ROW_HEIGHT * i);
            this.addRenderableWidget(w);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double hAmt, double vAmt) {
        if (mouseY > HEADER_HEIGHT && mouseY < this.height - FOOTER_HEIGHT) {
            scrollOffset = Math.clamp((int)(scrollOffset - vAmt * 10), 0, getMaxScroll());
            updateScrollableWidgetPositions();
        }
        return true;
    }

    @Override
    public void onClose() {
        VitalEdgeConfig.edgeThickness = edgeThickness;
        VitalEdgeConfig.stepCount = stepCount;
        VitalEdgeConfig.opacity = opacity;
        VitalEdgeConfig.biomeBlendStrength = biomeBlendStrength;
        VitalEdgeConfigManager.save();
        this.minecraft.setScreen(parent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);

        // Scissor clip — only scrollable content area
        double scale = this.minecraft.getWindow().getGuiScale();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(
            0,
            (int)((this.height - HEADER_HEIGHT - getVisibleHeight()) * scale),
            (int)(this.width * scale),
            (int)(getVisibleHeight() * scale)
        );
        super.render(graphics, mouseX, mouseY, delta);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        // Header — always on top
        graphics.fill(0, 0, this.width, HEADER_HEIGHT, 0xFF1E1E1E);
        graphics.fill(0, HEADER_HEIGHT - 1, this.width, HEADER_HEIGHT, 0xFF666666);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, (HEADER_HEIGHT - 8) / 2, 0xFFFFFFFF);

        // Footer — always on top
        graphics.fill(0, this.height - FOOTER_HEIGHT, this.width, this.height, 0xFF1E1E1E);
        graphics.fill(0, this.height - FOOTER_HEIGHT, this.width, this.height - FOOTER_HEIGHT + 1, 0xFF666666);

        // Tooltip inside footer
        hoveredDescription = "";
        for (var entry : descriptions.entrySet()) {
            AbstractWidget w = entry.getKey();
            if (w.getY() >= HEADER_HEIGHT && w.getY() < this.height - FOOTER_HEIGHT && w.isHovered()) {
                hoveredDescription = entry.getValue();
                break;
            }
        }
        if (!hoveredDescription.isEmpty()) {
            graphics.drawCenteredString(this.font, hoveredDescription,
                this.width / 2, this.height - FOOTER_HEIGHT + 10, 0xFFAAAAAA);
        }

        // Done button rendered outside scissor, always clickable
        doneButton.render(graphics, mouseX, mouseY, delta);

        // Scrollbar — only when needed
        int maxScroll = getMaxScroll();
        if (maxScroll > 0) {
            int visibleHeight = getVisibleHeight();
            int barHeight = Math.max(20, visibleHeight * visibleHeight / getTotalContentHeight());
            int barY = HEADER_HEIGHT + (int)((float) scrollOffset / maxScroll * (visibleHeight - barHeight));
            int barX = this.width - SCROLLBAR_WIDTH - SCROLLBAR_MARGIN;
            graphics.fill(barX, HEADER_HEIGHT, barX + SCROLLBAR_WIDTH, this.height - FOOTER_HEIGHT, 0x33FFFFFF);
            graphics.fill(barX, barY, barX + SCROLLBAR_WIDTH, barY + barHeight, 0xCCFFFFFF);
        }
    }
}
