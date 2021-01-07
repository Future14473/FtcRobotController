package org.firstinspires.ftc.teamcode.opModes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.*;
import org.firstinspires.ftc.teamcode.utility.RotationUtil;
import org.firstinspires.ftc.teamcode.utility.pose;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="2 odometry + IMU Localization", group="Autonomous")
//@Disabled
public class OdoWheelIMULocalization extends LinearOpMode {
    Mecanum mecanum;
    IMU imu;
    RotationOdometry odometry;
    DcMotor horizontal;
    DcMotor vertical;
    double totalDeltaAngle = 0;
    double currentDeltaAngle = 0;


    @Override
    public void runOpMode() throws InterruptedException {


        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);

        odometry = GiveTheDefaultConfiguration.rotationOdometryConfig(hardwareMap,imu,telemetry);
        waitForStart();



        horizontal = hardwareMap.get(DcMotor.class, "taco");
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        vertical = hardwareMap.get(DcMotor.class, "shooter");
        vertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vertical.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        odometry.setPosition(new pose(0,0,0));
        odometry.start();

        while (opModeIsActive()){
            currentDeltaAngle = odometry.rotDelta;
            totalDeltaAngle += currentDeltaAngle;

            Log.e("Rot Delta", String.valueOf(odometry.rotDelta));
            Log.e("Horo Delta", String.valueOf(odometry.horoTrans));
            Log.e("Vert Delta", String.valueOf(odometry.vertTrans));
            Log.e("_________________","______________________");

            telemetry.addData("IMU Position", imu.getHeading());
            telemetry.addData("Odometry Position", odometry.getPosition());
            telemetry.addData("Vert Wheel Ticks ", vertical.getCurrentPosition());
            telemetry.addData("Horo Wheel Ticks ", horizontal.getCurrentPosition());
            telemetry.addData("Rot Delta", odometry.rotDelta);
            telemetry.addData("Sum Rot Delta", totalDeltaAngle);


            telemetry.update();


            double targetDir = -(Math.atan2(gamepad1.left_stick_y,gamepad1.left_stick_x) + Math.PI/2);
            double magnitude = Math.hypot(gamepad1.left_stick_y,gamepad1.left_stick_x);
            double turnPwr = RotationUtil.turnLeftOrRight(odometry.getPosition().r, targetDir, Math.PI * 2);


            mecanum.drive(gamepad1.right_stick_x/3, -gamepad1.right_stick_y/3,
                    (magnitude > 0.5 && magnitude > 0.08)? turnPwr:0);
        }

        odometry.end();
    }
}
