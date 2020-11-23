package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.GivesPosition;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.DriveWheelOdometryWheel;
import org.firstinspires.ftc.teamcode.odometry.FreeSpinOdoWheel;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.odometry.OdometryThatusesIMUforHeading;
import org.firstinspires.ftc.teamcode.odometry.OdometryWheel;
import org.firstinspires.ftc.teamcode.utility.RotationUtil;
import org.firstinspires.ftc.teamcode.utility.pose;

import java.util.ArrayList;
import java.util.List;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="Full Odo + IMU Localization", group="Autonomous")
public class Localization extends LinearOpMode {
    Mecanum mecanum;
    IMU imu;
    Odometry odometry;
    DcMotor horizontal;
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor backLeft;

    @Override
    public void runOpMode() throws InterruptedException {
        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);
        odometry = defaultConfiguration();

        waitForStart();

        odometry.start();
        odometry.overridePosition(new pose(0,0,0));
        while (opModeIsActive()){
            telemetry.addData("Odometry Position", odometry.getPosition());

//            telemetry.addData("frontLeft", frontLeft.getCurrentPosition());
//            telemetry.addData("frontRight", frontRight.getCurrentPosition());
//            telemetry.addData("backRight", backRight.getCurrentPosition());
//            telemetry.addData("backLeft", backLeft.getCurrentPosition());

            telemetry.update();

            double targetDir = -Math.atan2(gamepad1.left_stick_y,gamepad1.left_stick_x) - Math.PI/2;
            double magnitude = Math.hypot(gamepad1.left_stick_y,gamepad1.left_stick_x);
            double turnPwr = - RotationUtil.turnLeftOrRight(odometry.getPosition().r, targetDir, Math.PI * 2);

            telemetry.addData("Intended dir", targetDir);
            telemetry.addData("pwr", turnPwr);

            mecanum.drive(gamepad1.right_stick_x/3, -gamepad1.right_stick_y/3,
                    (magnitude > 0.5 && Math.abs(turnPwr) > 0.08)? -turnPwr:0);
        }

        odometry.end();
    }

    Odometry defaultConfiguration(){
        //physical wheels
         frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
         frontRight = hardwareMap.get(DcMotor.class, "frontRight");
         backRight = hardwareMap.get(DcMotor.class, "backRight");
         backLeft = hardwareMap.get(DcMotor.class, "backLeft");
//        DcMotor vertical = hardwareMap.get(DcMotor.class, "vertical");
        horizontal = hardwareMap.get(DcMotor.class, "shooter");
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //odometry wheels
        OdometryWheel frontRightOdo = new DriveWheelOdometryWheel(new pose(178.5,168,Math.PI), frontRight);
        OdometryWheel frontLeftOdo = new DriveWheelOdometryWheel(new pose(-178.5,168,Math.PI), frontLeft);
        OdometryWheel backRightOdo = new DriveWheelOdometryWheel(new pose(178.5,-168,Math.PI), backRight);
        OdometryWheel backLeftOdo = new DriveWheelOdometryWheel(new pose(-178.5,-168,Math.PI), backLeft);
//        OdometryWheel verticalOdo = new FreeSpinOdoWheel(new pose(-180,91,Math.PI/2), vertical);
        OdometryWheel horizontalOdo = new FreeSpinOdoWheel(new pose(170,-190,Math.PI/2), horizontal);

        List<OdometryWheel> odometryWheels = new ArrayList<>();
        odometryWheels.add(frontLeftOdo);
        odometryWheels.add(frontRightOdo);
        odometryWheels.add(backLeftOdo);
        odometryWheels.add(backRightOdo);
//        odometryWheels.add(verticalOdo);
        odometryWheels.add(horizontalOdo);

        // odometry system
        pose initial = new pose(0,0,Math.PI/2);
        return new OdometryThatusesIMUforHeading(imu, initial, odometryWheels, telemetry);
    }
}
