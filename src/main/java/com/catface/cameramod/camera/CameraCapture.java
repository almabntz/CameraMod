package com.catface.cameramod.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

/**
 * Reads the current local player's camera position and rotation.
 *
 * This class is intentionally responsible only for capture. It does not
 * store keyframes or control playback.
 */
public final class CameraCapture {

    private CameraCapture() {
    }

    /**
     * Captures the current local player state as a keyframe.
     *
     * @param tick playback tick assigned to the captured keyframe
     * @return captured keyframe
     * @throws IllegalStateException if no local player is available
     */
    public static CameraKeyframe capture(int tick) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) {
            throw new IllegalStateException(
                    "Cannot capture a keyframe because no local player is available."
            );
        }

        return new CameraKeyframe(
                player.getX(),
                player.getY(),
                player.getZ(),
                player.getYRot(),
                player.getXRot(),
                tick
        );
    }
}
