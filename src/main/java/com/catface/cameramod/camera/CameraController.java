package com.catface.cameramod.camera;

/**
 * Boundary between playback math and the game engine.
 *
 * Playback decides what the camera state should be. A concrete controller
 * decides how that state is applied to Minecraft.
 */
public interface CameraController {

    void apply(CameraState state);
}
