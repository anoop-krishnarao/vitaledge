package dev.anoopkrishnarao.vitaledge.mixin;

import dev.anoopkrishnarao.vitaledge.ArmorDurabilityTracker;
import dev.anoopkrishnarao.vitaledge.EdgeGradientRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class HudRenderMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderHud(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        // Don't render overlay when any screen is open (pause menu, inventory, etc.)
        if (mc.screen != null) return;
        if (mc.player == null) return;

        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        int color = ArmorDurabilityTracker.getColor((float) mc.player.getY());
        EdgeGradientRenderer.render(graphics, screenWidth, screenHeight, color);
    }
}
