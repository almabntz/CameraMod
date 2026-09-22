package com.catface.cameramod.camera;

/**
 * Coordinates the active camera path and playback state.
 *
 *  This class provides a single client-side entry point for camera playback.
 * Commands can interact with this manager without needing to know how
 * interpolation or Minecraft camera manipulation works.
 */

public final class CameraManager {

    private static final CameraManager INSTANCE = new CameraManager();

    private final CameraController controller;
    private final CameraPlayback playback;

    private CameraPath activePath;

    private CameraManager() {
        controller = new MinecraftCameraController();
        playback = new CameraPlayback(controller);
    }

 // Setters and Getters
    public static CameraManager getInstance() { return INSTANCE; }
    public CameraPath getActivePath() { return activePath; }
    public void setActivePath(CameraPath path) { this.activePath = path;}

    public boolean play() {
        if (activePath == null) {
            return false;
        }

        return playback.play(activePath);
    }

    public void stop() {
        playback.stop();
    }

    public boolean isPlaying() {
        return playback.isPlaying();
    }

    public void tick() {
        if (!isPlaying()) {
            return;
        }

        playback.tick();
    }
}