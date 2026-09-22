package com.catface.cameramod.command;

import com.catface.cameramod.CameraMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

/**
 * Registers the camera mod's client-side commands with Minecraft.
 */
@EventBusSubscriber(
        modid = CameraMod.MODID,
        value = Dist.CLIENT
)
public final class CameraCommandHandler {

    private CameraCommandHandler() {
    }

    @SubscribeEvent
    public static void onRegisterClientCommands(
            RegisterClientCommandsEvent event
    ) {
        CameraCommand.register(event.getDispatcher());
    }
}
