package com.catface.cameramod.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

/**
 * Applies calculated camera states to Minecraft's current camera entity.
 *
 * This keeps Minecraft-specific manipulation outside the playback engine.
 * The MVP can therefore evolve to a dedicated detached camera entity later
 * without changing interpolation or path storage.
 */
public final class MinecraftCameraController implements CameraController {

    @Override
    public void apply(CameraState state) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity cameraEntity = minecraft.getCameraEntity();

        if (cameraEntity == null) {
            return;
        }

        cameraEntity.setPos(state.x(), state.y(), state.z());
        cameraEntity.setYRot(state.yaw());
        cameraEntity.setXRot(state.pitch());
    }
}
