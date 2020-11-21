package org.firstinspires.ftc.teamcode.opModes;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.movement.Mecanum;



@TeleOp(name="BaseDrive", group="Iterative Opmode")
//@Disabled
//use DriveWheelIMULocalization for the same functionality instead
public class BaseDrive extends OpMode
{
    // Declare OpMode members.
    Mecanum MecanumDrive;
    DcMotor intake;
    DcMotor taco;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        MecanumDrive = new Mecanum(hardwareMap);
        intake = hardwareMap.get(DcMotor.class, "intake");
        taco = hardwareMap.get(DcMotor.class, "taco");

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // stop when no one is touching anything

        double y = gamepad1.right_stick_y;
        double x = -gamepad1.right_stick_x;
        double turn = gamepad1.left_stick_x;
        double intakeOut = gamepad1.right_trigger;
        double intakeIn = -gamepad1.left_trigger;

        // make the intake do the correct trigger, + is outward, - is inward
        intake.setPower(-(intakeIn + intakeOut));
        taco.setPower(intakeIn + intakeOut);
//        if (intakeIn != 0){
//            intake.setPower(intakeIn);
//        } else if(intakeOut != 0){
//            intake.setPower(intakeOut);
//        }

        MecanumDrive.drive(x,y,turn);


        // Show the elapsed game time and wheel power.
        telemetry.addData("Trigger Left: ", intakeOut);
        telemetry.addData("Trigger Right: ", intakeIn);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}



