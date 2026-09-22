package com.catface.cameramod.camera;

import com.catface.cameramod.CameraMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;

/** This Class is responsible for handling the ticks
 * the game communicates to the camera
 */

// Register event subscriber automatically on the physical client only.
@EventBusSubscriber(
        modid = CameraMod.MODID,
        value = Dist.CLIENT
)

public final class CameraTickHandler {

    private CameraTickHandler() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        CameraManager.getInstance().tick();
    }
}