package com.catface.cameramod.command;

import com.catface.cameramod.camera.CameraCapture;
import com.catface.cameramod.camera.CameraKeyframe;
import com.catface.cameramod.camera.CameraManager;
import com.catface.cameramod.camera.CameraPath;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.literal;

/** This class handles all the users commands for the cam.
 * A Brigadier command convention is being utilized here as the commands execution handler.
 * positive numbers indicate a successs and a 0 indicates a fail.
 */

public final class CameraCommand {

    private CameraCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("camera")
                        .then(literal("path")
                                .then(literal("new")
                                        .executes(context -> createPath(context.getSource()))))
                        .then(literal("keyframe")
                                .then(literal("add")
                                        .executes(context -> addKeyframe(context.getSource())))
                                .then(literal("clear")
                                        .executes(context -> clearKeyframes(context.getSource()))))
                        .then(literal("play")
                                .executes(context -> play(context.getSource())))
                        .then(literal("stop")
                                .executes(context -> stop(context.getSource())))
        );
    }

    private static int createPath(CommandSourceStack source) {
        CameraManager manager = CameraManager.getInstance();

        manager.createNewPath("default");

        source.sendSuccess(
                () -> Component.literal("Created a new camera path."),
                false
        );

        return 1;
    }

    private static int addKeyframe(CommandSourceStack source) {
        CameraManager manager = CameraManager.getInstance();
        CameraPath path = manager.getActivePath();

        if (path == null) {
            source.sendFailure(
                    Component.literal(
                            "No camera path exists. Use /camera path new first."
                    )
            );
            return 0;
        }

        int tick = path.size() == 0
                ? 0
                : path.getDurationTicks() + 20;

        CameraKeyframe keyframe = CameraCapture.capture(tick);
        path.addKeyframe(keyframe);

        source.sendSuccess(
                () -> Component.literal(
                        "Added camera keyframe at tick " + tick + "."
                ),
                false
        );

        return 1;
    }

    private static int clearKeyframes(CommandSourceStack source) {
        CameraManager manager = CameraManager.getInstance();
        CameraPath path = manager.getActivePath();

        if (path == null) {
            source.sendFailure(
                    Component.literal("No camera path exists.")
            );
            return 0;
        }

        path.clear();

        source.sendSuccess(
                () -> Component.literal("Cleared all camera keyframes."),
                false
        );

        return 1;
    }

    private static int play(CommandSourceStack source) {
        CameraManager manager = CameraManager.getInstance();

        if (manager.getActivePath() == null) {
            source.sendFailure(
                    Component.literal(
                            "No camera path exists. Use /camera path new first."
                    )
            );
            return 0;
        }

        if (manager.getActivePath().size() < 2) {
            source.sendFailure(
                    Component.literal(
                            "At least two keyframes are required for playback."
                    )
            );
            return 0;
        }

        if (!manager.play()) {
            source.sendFailure(
                    Component.literal("Unable to start camera playback.")
            );
            return 0;
        }

        source.sendSuccess(
                () -> Component.literal("Camera playback started."),
                false
        );

        return 1;
    }

    private static int stop(CommandSourceStack source) {
        CameraManager.getInstance().stop();

        source.sendSuccess(
                () -> Component.literal("Camera playback stopped."),
                false
        );

        return 1;
    }
}