package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.GivesPosition;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.DriveWheelOdometryWheel;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.odometry.OdometryThatusesIMUforHeading;
import org.firstinspires.ftc.teamcode.odometry.OdometryWheel;
import org.firstinspires.ftc.teamcode.pathFollow.Follower;
import org.firstinspires.ftc.teamcode.utility.RotationUtil;
import org.firstinspires.ftc.teamcode.utility.point;
import org.firstinspires.ftc.teamcode.utility.pose;

import java.util.ArrayList;
import java.util.List;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="Working DriveWheel + IMU Path following", group="Autonomous")
public class DriveWheelIMUFollowing extends LinearOpMode {
    Mecanum mecanum;
    IMU imu;
    Odometry odometry;
    volatile boolean running = false;

    @Override
    public void runOpMode() throws InterruptedException {
        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);
        odometry = defaultConfiguration();
        Follower f = null;
        waitForStart();

        odometry.start();

//        new Thread(() -> {
//            telemetry.addData("running", "yes");
//            telemetry.update();
//            Follower f = new Follower(mecanum, odometry, telemetry);
//        }).start();

//        f.start();
        while (opModeIsActive()){
            f = new Follower(mecanum, odometry, telemetry);
            telemetry.addData("IMU Position", imu.getPosition());
            telemetry.addData("Odometry Position", odometry.getPosition());
            telemetry.update();

            //mecanum.drive(gamepad1.right_stick_x/3, gamepad1.right_stick_y/3, -gamepad1.left_stick_x/3);
        }

//        f.stop();
        odometry.end();
        assert f != null;
        f.stop();
    }

    Odometry defaultConfiguration(){
        //physical wheels
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        //odometry wheels
        OdometryWheel frontRightOdo = new DriveWheelOdometryWheel(new pose(178.5,168,Math.PI/2), frontRight);
        OdometryWheel frontLeftOdo = new DriveWheelOdometryWheel(new pose(-178.5,168,Math.PI/2), frontLeft);
        OdometryWheel backRightOdo = new DriveWheelOdometryWheel(new pose(178.5,-168,Math.PI/2), backRight);
        OdometryWheel backLeftOdo = new DriveWheelOdometryWheel(new pose(-178.5,-168,Math.PI/2), backLeft);

        List<OdometryWheel> odometryWheels = new ArrayList<>();
        odometryWheels.add(frontLeftOdo);
        odometryWheels.add(frontRightOdo);
        odometryWheels.add(backLeftOdo);
        odometryWheels.add(backRightOdo);

        // odometry system
        pose initial = new pose(0,0,Math.PI/2);
        return new OdometryThatusesIMUforHeading(imu, initial, odometryWheels);
    }
}
