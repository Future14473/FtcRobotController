package org.firstinspires.ftc.teamcode.opModes.unusedOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.odometry.dev.GiveTheDefaultConfiguration;
import org.firstinspires.ftc.teamcode.odometry.dev.Odometry2DRot_complicated;
import org.firstinspires.ftc.teamcode.utility.pose;


// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="2D Rotation Odometry Localization", group="Autonomous")
//@Disabled
public class Odo2DRotLocalization extends LinearOpMode {
//    Mecanum mecanum;
    IMU imu;
    Odometry2DRot_complicated odometry;


    @Override
    public void runOpMode() throws InterruptedException {
//        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);

        odometry = GiveTheDefaultConfiguration.Odo2DRotConfiguration(hardwareMap,imu, telemetry);
        waitForStart();

//        odometry.setPosition(new pose(0,0,0));
        odometry.start();
        odometry.setPosition(new pose(0,0,0));
        while (opModeIsActive()){
//            telemetry.addData("IMU Position", imu.getHeading());
//            telemetry.addData("Odometry Position", odometry.getPosition());

//            telemetry.update();

//
//            double targetDir = -(Math.atan2(gamepad1.left_stick_y,gamepad1.left_stick_x) + Math.PI/2);
//            double magnitude = Math.hypot(gamepad1.left_stick_y,gamepad1.left_stick_x);
//            double turnPwr = RotationUtil.turnLeftOrRight(odometry.getPosition().r, targetDir, Math.PI * 2);
//
//
//            mecanum.drive(gamepad1.right_stick_x/3, -gamepad1.right_stick_y/3,
//                    (magnitude > 0.5 && magnitude > 0.08)? turnPwr:0);
        }

        odometry.stop();
    }
}
