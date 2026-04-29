package dev.anoopkrishnarao.vitaledge;

public class DamageSurgeTracker {

    private static final long SURGE_DURATION_MS = 300L;

    private static long surgeStartTime = -1L;
    private static float surgeIntensity = 0f;

    /**
     * Called when the local player takes damage.
     * intensity: 0.0 to 1.0, scaled by damage magnitude.
     */
    public static void onDamage(float damageAmount) {
        if (!VitalEdgeConfig.surgeEnabled) return;
        // Map damage to intensity: clamp between 0.3 and 1.0
        surgeIntensity = Math.clamp(damageAmount / 10f, 0.3f, 1.0f);
        surgeStartTime = System.currentTimeMillis();
    }

    public static boolean isSurging() {
        if (surgeStartTime < 0) return false;
        return System.currentTimeMillis() - surgeStartTime < SURGE_DURATION_MS;
    }

    /**
     * Returns heartbeat pulse value (0.0 to 1.0) based on elapsed time.
     * Double-pulse pattern: thump-thump then decay.
     */
    public static float getSurgePulse() {
        if (!isSurging()) return 0f;
        float t = (System.currentTimeMillis() - surgeStartTime) / (float) SURGE_DURATION_MS;
        // Heartbeat: double sine bump then decay
        float pulse = (float) (Math.sin(t * Math.PI * 2) + 0.5f * Math.sin(t * Math.PI * 4));
        pulse = Math.clamp(pulse, 0f, 1f);
        return pulse * surgeIntensity;
    }

    /**
     * Returns shore wave pulse value (0.0 to 1.0) based on current time.
     * Used when durability < 5%.
     */
    public static float getShoreWavePulse() {
        float t = (System.currentTimeMillis() % 2000L) / 2000f;
        return (float) (0.5f + 0.5f * Math.sin(t * 2.0 * Math.PI));
    }

    /**
     * Returns the current pulse multiplier to apply to overlay opacity.
     * Surge takes priority over shore wave.
     */
    public static float getPulseMultiplier(float durabilityPercent) {
        if (isSurging()) {
            return 0.6f + 0.4f * getSurgePulse();
        }
        if (durabilityPercent >= 0f && durabilityPercent < 0.05f) {
            return 0.6f + 0.4f * getShoreWavePulse();
        }
        return 1.0f; // static, no pulse
    }
}
