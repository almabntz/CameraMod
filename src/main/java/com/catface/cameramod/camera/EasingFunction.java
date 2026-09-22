package com.catface.cameramod.camera;

/**
 * Converts a normalized playback progress value into an eased progress value.
 *
 * The input and output are normally in the range 0.0 to 1.0. Keeping
 * easing behind an interface lets playback support additional easing styles
 * later without changing its core logic.
 */
@FunctionalInterface
public interface EasingFunction {

    double apply(double progress);

    EasingFunction LINEAR = progress -> progress;
}
