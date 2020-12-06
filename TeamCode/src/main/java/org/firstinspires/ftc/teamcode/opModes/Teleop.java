package org.firstinspires.ftc.teamcode.opModes;


import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.utility.RotationUtil;


@TeleOp(name="Teleop", group="Teleop")
//@Disabled
//use DriveWheelIMULocalization for the same functionality instead
public class Teleop extends LinearOpMode
{
    // Declare OpMode members.
    Mecanum MecanumDrive;
    DcMotor intake;
    DcMotor taco;
    DcMotor shooter_adjuster;
    DcMotor shooter;
//    CRServo gate;
    Servo wobArm, wobGrip;
    IMU imu;

    public void runOpMode() throws InterruptedException {

        imu = new IMU(hardwareMap, telemetry);

        MecanumDrive = new Mecanum(hardwareMap);

        intake = hardwareMap.get(DcMotor.class, "intake");
        taco = hardwareMap.get(DcMotor.class, "taco");
//        gate = hardwareMap.get(CRServo.class, "gate");
        wobArm = hardwareMap.get(Servo.class, "wobArm");
        wobGrip = hardwareMap.get(Servo.class, "wobGrip");
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        shooter_adjuster = hardwareMap.get(DcMotor.class, "shooter_adjuster");
        shooter_adjuster.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter_adjuster.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        telemetry.addData("Status", "Initialized");

        waitForStart();


        while (opModeIsActive()){

        double y = -gamepad1.right_stick_y;
        double x = gamepad1.right_stick_x;

        // absolute turning
        double targetDir = -Math.atan2(gamepad1.left_stick_y,gamepad1.left_stick_x) - Math.PI/2;
        double magnitude = Math.hypot(gamepad1.left_stick_y,gamepad1.left_stick_x);
        double turnPwr = RotationUtil.turnLeftOrRight(imu.getHeading(), targetDir, Math.PI * 2);

        // stop when no one is touching anything
        MecanumDrive.drive(x/3, y/3,
                (magnitude > 0.5 && Math.abs(turnPwr) > 0.08)? turnPwr:0);

        double intakeOut = gamepad1.right_trigger;
        double intakeIn = -gamepad1.left_trigger;

        // make the intake do the correct trigger, + is outward, - is inward
        intake.setPower(-(intakeIn + intakeOut));
        taco.setPower(intakeIn + intakeOut);


        // shooter adjuster
        shooter_adjuster.setPower( (gamepad1.dpad_up?-0.5:0.5) + (gamepad1.dpad_down?0.5:-0.5) );
        shooter.setPower( (gamepad1.dpad_left?-0.5:0.5) + (gamepad1.dpad_right?0.5:-0.5) );

        // shooter gate
//        gate.setPower(0);
//        if (gamepad1.x){
//            gate.setPower(1);
//        } else if (gamepad1.y){
//            gate.setPower(-1);
//        }

        if (gamepad1.a){
//            wobArm.setPosition(0.0);
//            wobGrip.setPosition(0.5);

        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Shooter Adjuster: ", shooter_adjuster.getCurrentPosition());
        Log.e("Shooter Adjuster: ", String.valueOf( shooter_adjuster.getCurrentPosition()));
        telemetry.addData("Trigger Right: ", intakeIn);
    }



}
}



