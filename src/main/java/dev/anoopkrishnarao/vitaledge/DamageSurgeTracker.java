package dev.anoopkrishnarao.vitaledge;

public class DamageSurgeTracker {

    private static final long SURGE_DURATION_MS = 1200L;

    private static long surgeStartTime = -1L;
    private static float surgeIntensity = 0f;

    public static void onDamage(float damageAmount) {
        if (!VitalEdgeConfig.surgeEnabled) return;
        surgeIntensity = Math.clamp(damageAmount / 10f, 0.3f, 1.0f);
        surgeStartTime = System.currentTimeMillis();
    }

    public static boolean isSurging() {
        if (surgeStartTime < 0) return false;
        return System.currentTimeMillis() - surgeStartTime < SURGE_DURATION_MS;
    }

    public static float getSurgePulse() {
        if (!isSurging()) return 0f;
        float t = (System.currentTimeMillis() - surgeStartTime) / (float) SURGE_DURATION_MS;
        // Smooth heartbeat: two humps with exponential decay
        float beat1 = (float) Math.exp(-12 * Math.pow(t - 0.2, 2));
        float beat2 = (float) Math.exp(-20 * Math.pow(t - 0.5, 2)) * 0.6f;
        float pulse = Math.clamp(beat1 + beat2, 0f, 1f);
        return pulse * surgeIntensity;
    }

    public static float getShoreWavePulse() {
        float t = (System.currentTimeMillis() % 2000L) / 2000f;
        return (float) (0.5f + 0.5f * Math.sin(t * 2.0 * Math.PI));
    }

    public static float getPulseMultiplier(float durabilityPercent) {
        if (isSurging()) {
            return 0.6f + 0.4f * getSurgePulse();
        }
        if (durabilityPercent >= 0f && durabilityPercent < 0.05f) {
            return 0.6f + 0.4f * getShoreWavePulse();
        }
        return 1.0f;
    }
}
