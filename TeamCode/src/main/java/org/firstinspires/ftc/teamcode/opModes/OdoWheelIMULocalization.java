package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.DriveWheelOdometryWheel;
import org.firstinspires.ftc.teamcode.odometry.FreeSpinOdoWheel;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.odometry.OdometryThatusesIMUforHeading;
import org.firstinspires.ftc.teamcode.odometry.OdometryWheel;
import org.firstinspires.ftc.teamcode.utility.pose;

import java.util.ArrayList;
import java.util.List;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="Odo Wheel + IMU Localization", group="Autonomous")
public class OdoWheelIMULocalization extends LinearOpMode {
    Mecanum mecanum;
    IMU imu;
    Odometry odometry;
    DcMotor horizontal;


    @Override
    public void runOpMode() throws InterruptedException {
        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);
        odometry = defaultConfiguration();

        waitForStart();

        odometry.start();
        odometry.overridePosition(new pose(0,0,0));
        while (opModeIsActive()){
            telemetry.addData("IMU Position", imu.getHeading());
            telemetry.addData("Odometry Position", odometry.getPosition());
            telemetry.addData("Odometry Wheel Tick: ", horizontal.getCurrentPosition());



            telemetry.update();

            mecanum.drive(gamepad1.right_stick_x/3, -gamepad1.right_stick_y/3, -gamepad1.left_stick_x/3);
        }

        odometry.end();
    }

    Odometry defaultConfiguration(){
        horizontal = hardwareMap.get(DcMotor.class, "shooter");
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        OdometryWheel verticalOdo = new FreeSpinOdoWheel(new pose(-180,91,Math.PI/2), vertical);
        OdometryWheel horizontalOdo = new FreeSpinOdoWheel(new pose(170,-190,Math.PI/2), horizontal);

        List<OdometryWheel> odometryWheels = new ArrayList<>();

//        odometryWheels.add(verticalOdo);
        odometryWheels.add(horizontalOdo);

        // odometry system
        pose initial = new pose(0,0,Math.PI/2);
        return new OdometryThatusesIMUforHeading(imu, initial, odometryWheels, telemetry);
    }
}
