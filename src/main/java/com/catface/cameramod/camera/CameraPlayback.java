package com.catface.cameramod.camera;

import java.util.List;

/**
 * Plays a camera path one tick at a time.
 *
 * This class owns playback state and interpolation. It does not know how
 * commands work, how keyframes are captured, or how Minecraft stores files.
 */
public final class CameraPlayback {

    private final CameraController controller;
    private final EasingFunction easingFunction;

    private CameraPath activePath;
    private int currentTick;
    private boolean playing;

    public CameraPlayback(CameraController controller) {
        this(controller, EasingFunction.LINEAR);
    }

    public CameraPlayback(
            CameraController controller,
            EasingFunction easingFunction
    ) {
        if (controller == null) {
            throw new IllegalArgumentException("Camera controller cannot be null.");
        }

        if (easingFunction == null) {
            throw new IllegalArgumentException("Easing function cannot be null.");
        }

        this.controller = controller;
        this.easingFunction = easingFunction;
    }

    /**
     * Starts playback from the beginning.
     *
     * @return true if playback started; false when the path cannot be played
     */
    public boolean play(CameraPath path) {
        if (path == null || path.size() < 2) {
            stop();
            return false;
        }

        activePath = path;
        currentTick = path.getKeyframe(0).getTick();
        playing = true;

        controller.apply(path.getKeyframe(0).toState());
        return true;
    }

    public void stop() {
        playing = false;
        activePath = null;
        currentTick = 0;
    }

    public boolean isPlaying() {
        return playing;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    /**
     * Advances playback by one Minecraft tick.
     */
    public void tick() {
        if (!playing || activePath == null) {
            return;
        }

        if (currentTick >= activePath.getDurationTicks()) {
            CameraKeyframe last =
                    activePath.getKeyframe(activePath.size() - 1);
            controller.apply(last.toState());
            stop();
            return;
        }

        currentTick++;

        CameraState state = calculateState(activePath, currentTick);
        controller.apply(state);
    }

    CameraState calculateState(CameraPath path, int tick) {
        List<CameraKeyframe> keyframes = path.getKeyframes();

        CameraKeyframe first = keyframes.get(0);

        if (tick <= first.getTick()) {
            return first.toState();
        }

        CameraKeyframe last = keyframes.get(keyframes.size() - 1);

        if (tick >= last.getTick()) {
            return last.toState();
        }

        for (int index = 0; index < keyframes.size() - 1; index++) {
            CameraKeyframe start = keyframes.get(index);
            CameraKeyframe end = keyframes.get(index + 1);

            if (tick >= start.getTick() && tick <= end.getTick()) {
                return interpolate(start, end, tick);
            }
        }

        // Defensive fallback. Ordered keyframes should make this unreachable.
        return last.toState();
    }

    private CameraState interpolate(
            CameraKeyframe start,
            CameraKeyframe end,
            int tick
    ) {
        int segmentDuration = end.getTick() - start.getTick();

        if (segmentDuration <= 0) {
            return end.toState();
        }

        double rawProgress =
                (double) (tick - start.getTick()) / segmentDuration;

        double progress = clamp(easingFunction.apply(rawProgress), 0.0, 1.0);

        double x = lerp(start.getX(), end.getX(), progress);
        double y = lerp(start.getY(), end.getY(), progress);
        double z = lerp(start.getZ(), end.getZ(), progress);

        float yaw = lerpAngle(start.getYaw(), end.getYaw(), progress);
        float pitch = (float) lerp(
                start.getPitch(),
                end.getPitch(),
                progress
        );

        return new CameraState(x, y, z, yaw, pitch);
    }

    private static double lerp(double start, double end, double progress) {
        return start + (end - start) * progress;
    }

    /**
     * Interpolates through the shortest angular path, including across
     * the -180/180 degree boundary.
     */
    private static float lerpAngle(float start, float end, double progress) {
        float difference = wrapDegrees(end - start);
        return start + difference * (float) progress;
    }

    private static float wrapDegrees(float degrees) {
        float wrapped = degrees % 360.0F;

        if (wrapped >= 180.0F) {
            wrapped -= 360.0F;
        }

        if (wrapped < -180.0F) {
            wrapped += 360.0F;
        }

        return wrapped;
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
