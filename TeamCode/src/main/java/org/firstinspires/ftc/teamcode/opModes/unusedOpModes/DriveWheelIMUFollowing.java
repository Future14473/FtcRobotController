package org.firstinspires.ftc.teamcode.opModes.unusedOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.working.DriveWheelOdometryWheel;
import org.firstinspires.ftc.teamcode.odometry.working.Odometry;
import org.firstinspires.ftc.teamcode.odometry.working.OdometryThatusesIMUforHeading;
import org.firstinspires.ftc.teamcode.odometry.working.OdometryWheel;
import org.firstinspires.ftc.teamcode.pathFollow.Follower;
import org.firstinspires.ftc.teamcode.utility.pose;

import java.util.ArrayList;
import java.util.List;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="Working DriveWheel + IMU Path following", group="Autonomous")
@Disabled
public class DriveWheelIMUFollowing extends LinearOpMode {
    Mecanum mecanum;
    IMU imu;
    Odometry odometry;
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor backLeft;


    @Override
    public void runOpMode() throws InterruptedException {
        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);
        odometry = defaultConfiguration();
        Follower f = new Follower(mecanum, odometry, telemetry);

        waitForStart();

        odometry.start();
        f.start();

        while (opModeIsActive()){

            //telemetry.addData("IMU Position", imu.getPosition());
            //telemetry.addData("Odometry Position", odometry.getPosition());
            //telemetry.update();

            //mecanum.drive(gamepad1.right_stick_x/3, gamepad1.right_stick_y/3, -gamepad1.left_stick_x/3);
            telemetry.addData("frontLeft", frontLeft.getCurrentPosition());
            telemetry.addData("frontRight", frontRight.getCurrentPosition());
            telemetry.addData("backRight", backRight.getCurrentPosition());
            telemetry.addData("backLeft", backLeft.getCurrentPosition());
        }

        f.stop();
        odometry.end();

    }

    Odometry defaultConfiguration(){
        //physical wheels
         frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
         frontRight = hardwareMap.get(DcMotor.class, "frontRight");
         backRight = hardwareMap.get(DcMotor.class, "backRight");
         backLeft = hardwareMap.get(DcMotor.class, "backLeft");
//        DcMotor vertical = hardwareMap.get(DcMotor.class, "vertical");
//        DcMotor horizontal = hardwareMap.get(DcMotor.class, "intake");

        //odometry wheels, kinda wierd
        OdometryWheel frontRightOdo = new DriveWheelOdometryWheel(new pose(178.5,168,Math.PI/2), frontRight);
        OdometryWheel frontLeftOdo = new DriveWheelOdometryWheel(new pose(-178.5,168,Math.PI/2), frontLeft);
        OdometryWheel backRightOdo = new DriveWheelOdometryWheel(new pose(178.5,-168,Math.PI/2), backRight);
        OdometryWheel backLeftOdo = new DriveWheelOdometryWheel(new pose(-178.5,-168,Math.PI/2), backLeft);
//        OdometryWheel verticalOdo = new FreeSpinOdoWheel(new pose(-180,91,Math.PI/2), vertical);
//        OdometryWheel horizontalOdo = new FreeSpinOdoWheel(new pose(170,-190,  Math.PI), horizontal);

        List<OdometryWheel> odometryWheels = new ArrayList<>();
        odometryWheels.add(frontLeftOdo);
        odometryWheels.add(frontRightOdo);
        odometryWheels.add(backLeftOdo);
        odometryWheels.add(backRightOdo);
//        odometryWheels.add(verticalOdo);
        //odometryWheels.add(horizontalOdo);

        // odometry system
        pose initial = new pose(0,0,Math.PI/2);
        return new OdometryThatusesIMUforHeading(imu, initial, odometryWheels, telemetry);
    }
}
