package com.catface.cameramod.camera;

/**
 * Represents one point in a cinematic camera path.
 *
 * A keyframe stores camera position, rotation, and the playback tick at
 * which the camera should reach that state.
 */
public final class CameraKeyframe {

    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final int tick;

    public CameraKeyframe(
            double x,
            double y,
            double z,
            float yaw,
            float pitch,
            int tick
    ) {
        if (tick < 0) {
            throw new IllegalArgumentException("Keyframe tick cannot be negative.");
        }

        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.tick = tick;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public int getTick() {
        return tick;
    }

    public CameraState toState() {
        return new CameraState(x, y, z, yaw, pitch);
    }
}
