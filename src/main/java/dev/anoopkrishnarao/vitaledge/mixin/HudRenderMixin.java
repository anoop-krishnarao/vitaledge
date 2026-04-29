package dev.anoopkrishnarao.vitaledge.mixin;

import dev.anoopkrishnarao.vitaledge.ArmorDurabilityTracker;
import dev.anoopkrishnarao.vitaledge.EdgeGradientRenderer;
import net.minecraft.client.DeltaTracker;
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
        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        // Get the current player Y from the client tick tracker
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player == null) return;

        int color = ArmorDurabilityTracker.getColor((float) mc.player.getY());
        EdgeGradientRenderer.render(graphics, screenWidth, screenHeight, color);
    }
}
