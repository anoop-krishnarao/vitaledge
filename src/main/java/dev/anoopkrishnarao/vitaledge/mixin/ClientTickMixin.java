package dev.anoopkrishnarao.vitaledge.mixin;

import dev.anoopkrishnarao.vitaledge.ArmorDurabilityTracker;
import dev.anoopkrishnarao.vitaledge.VitalEdgeClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class ClientTickMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return;

        ArmorDurabilityTracker.update(player);

        // Temporary: log every 20 ticks (once per second)
        if (mc.level != null && mc.level.getGameTime() % 20 == 0) {
            if (ArmorDurabilityTracker.hasArmor()) {
                VitalEdgeClient.LOGGER.info(
                    "VitalEdge | Armor durability: {}%",
                    Math.round(ArmorDurabilityTracker.getDurabilityPercent() * 100)
                );
            } else {
                VitalEdgeClient.LOGGER.info("VitalEdge | No armor equipped.");
            }
        }
    }
}
