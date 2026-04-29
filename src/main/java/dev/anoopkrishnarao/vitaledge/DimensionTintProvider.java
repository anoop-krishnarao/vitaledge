package dev.anoopkrishnarao.vitaledge;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class DimensionTintProvider {

    /**
     * Returns a hue shift in degrees to apply to the base durability color.
     * Overworld: 0 (no shift)
     * Nether: +15 (warm red shift)
     * End: -30 (violet shift)
     */
    public static float getHueShift() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return 0f;

        ResourceKey<Level> dimension = mc.level.dimension();

        if (dimension.equals(Level.NETHER)) {
            return 15f;
        } else if (dimension.equals(Level.END)) {
            return -30f;
        }
        return 0f;
    }
}
