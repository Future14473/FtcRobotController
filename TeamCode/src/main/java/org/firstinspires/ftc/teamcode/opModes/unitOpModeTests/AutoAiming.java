package org.firstinspires.ftc.teamcode.opModes.unitOpModeTests;

import android.content.Context;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Auto Aiming", group="Auto")
@Disabled
public class AutoAiming extends LinearOpMode {
    // List of available sound resources
    String[] sounds = {"TrimmedFutureSound"};
    boolean soundPlaying = false;

    @Override
    public void runOpMode() {

        // Variables for choosing from the available sounds
        int soundIndex = 0;
        int soundID = -1;
        boolean was_dpad_up = false;
        boolean was_dpad_down = false;

        Context myApp = hardwareMap.appContext;

        // create a sound parameter that holds the desired player parameters.
        SoundPlayer.PlaySoundParams params = new SoundPlayer.PlaySoundParams();
        params.loopControl = 0;
        params.waitForNonLoopingSoundsToFinish = true;

        // In this sample, we will skip waiting for the user to press play, and start displaying sound choices right away
        while (!isStopRequested()) {

            // Look for DPAD presses to change the selection
            if (gamepad1.dpad_down && !was_dpad_down) {
                // Go to next sound (with list wrap) and display it
                soundIndex = (soundIndex + 1) % sounds.length;
            }

            if (gamepad1.dpad_up && !was_dpad_up) {
                // Go to previous sound (with list wrap) and display it
                soundIndex = (soundIndex + sounds.length - 1) % sounds.length;
            }

            // Look for trigger to see if we should play sound
            // Only start a new sound if we are currently not playing one.
            if (gamepad1.right_bumper && !soundPlaying) {

                // Determine Resource IDs for the sounds you want to play, and make sure it's valid.
                if ((soundID = myApp.getResources().getIdentifier(sounds[soundIndex], "raw", myApp.getPackageName())) != 0) {

                    // Signal that the sound is now playing.
                    soundPlaying = true;

                    // Start playing, and also Create a callback that will clear the playing flag when the sound is complete.
                    SoundPlayer.getInstance().startPlaying(myApp, soundID, params, null,
                            new Runnable() {
                                public void run() {
                                    soundPlaying = false;
                                }
                            });
                    // Remember the last state of the dpad to detect changes.
                    was_dpad_up     = gamepad1.dpad_up;
                    was_dpad_down   = gamepad1.dpad_down;

                    // Display the current sound choice, and the playing status.
                    telemetry.addData("", "Use DPAD up/down to move to position.");
                    telemetry.addData("", "Press Right Bumper to activate.");
                    telemetry.addData("", "");
//                    telemetry.addData("Sound >", sounds[soundIndex]);
//                    telemetry.addData("Status >", soundPlaying ? "Playing" : "Stopped");
                    telemetry.update();
                }
            }
        }
    }
}