package dev.anoopkrishnarao.vitaledge;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;

public class ArmorDurabilityTracker {

    private static float durabilityPercent = -1f;

    public static void update(LocalPlayer player) {
        EquipmentSlot[] armorSlots = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
        };

        int totalMax = 0;
        int totalDamage = 0;
        int equippedCount = 0;

        for (EquipmentSlot slot : armorSlots) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem) {
                totalMax += stack.getMaxDamage();
                totalDamage += stack.getDamageValue();
                equippedCount++;
            }
        }

        if (equippedCount == 0) {
            durabilityPercent = -1f;
        } else {
            durabilityPercent = 1f - ((float) totalDamage / (float) totalMax);
        }
    }

    public static float getDurabilityPercent() {
        return durabilityPercent;
    }

    public static boolean hasArmor() {
        return durabilityPercent >= 0f;
    }

    public static int getColor(float playerY) {
        if (!hasArmor()) return 0x00000000;
        float hueShift = DimensionTintProvider.getHueShift();
        int baseColor = DurabilityColorMapper.getColor(durabilityPercent, hueShift);
        return YHeightModifier.modify(baseColor, playerY);
    }
}
