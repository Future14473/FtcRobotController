package org.firstinspires.ftc.teamcode.opModes;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.utility.RotationUtil;


@TeleOp(name="BaseDrive", group="Teleop")
//@Disabled
//use DriveWheelIMULocalization for the same functionality instead
public class Teleop extends LinearOpMode
{
    // Declare OpMode members.
    Mecanum MecanumDrive;
    DcMotor intake;
    DcMotor taco;
    DcMotor shooter_adjuster;
    CRServo gate;
    IMU imu;




    public void runOpMode() throws InterruptedException {

        imu = new IMU(hardwareMap, telemetry);

        MecanumDrive = new Mecanum(hardwareMap);

        intake = hardwareMap.get(DcMotor.class, "intake");
        taco = hardwareMap.get(DcMotor.class, "taco");
        gate = hardwareMap.get(CRServo.class, "gate");

        shooter_adjuster = hardwareMap.get(DcMotor.class, "shooter_adjuster");
        shooter_adjuster.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter_adjuster.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        telemetry.addData("Status", "Initialized");

        waitForStart();
        while (opModeIsActive()){


        // stop when no one is touching anything

        double y = -gamepad1.right_stick_y;
        double x = gamepad1.right_stick_x;

        // absolute turning
        double targetDir = -Math.atan2(gamepad1.left_stick_y,gamepad1.left_stick_x) - Math.PI/2;
        double magnitude = Math.hypot(gamepad1.left_stick_y,gamepad1.left_stick_x);
        double turnPwr = RotationUtil.turnLeftOrRight(imu.getHeading(), targetDir, Math.PI * 2);

        double intakeOut = gamepad1.right_trigger;
        double intakeIn = -gamepad1.left_trigger;

        // make the intake do the correct trigger, + is outward, - is inward
        intake.setPower(-(intakeIn + intakeOut));
        taco.setPower(intakeIn + intakeOut);


        // shooter adjuster
        if (gamepad1.dpad_up){ // moves the target position 10 up
            shooter_adjuster.setTargetPosition(
                    shooter_adjuster.getCurrentPosition() + 10
            );
        } else if (gamepad1.dpad_down){
            shooter_adjuster.setTargetPosition(
                    shooter_adjuster.getTargetPosition() - 10
            );
        }

        // shooter gate, 0 to 1, 0.5 is stationary
        if (gamepad1.x){
            gate.setPower(1);
        } else if (gamepad1.y){
            gate.setPower(0);
        }
        gate.setPower(0.5);


        MecanumDrive.drive(x/3, y/3,
                (magnitude > 0.5 && Math.abs(turnPwr) > 0.08)? turnPwr:0);


        // Show the elapsed game time and wheel power.
        telemetry.addData("Shooter Adjuster: ", shooter_adjuster.getCurrentPosition());
        telemetry.addData("Trigger Right: ", intakeIn);
    }



}}



