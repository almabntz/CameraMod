package com.catface.cameramod.camera;

/**
 * Immutable camera position and rotation at a single instant.
 *
 * This deliberately does not contain playback timing. It can therefore be
 * produced by capture, interpolation, or another camera source.
 */
public record CameraState(
        double x,
        double y,
        double z,
        float yaw,
        float pitch
) {
}
