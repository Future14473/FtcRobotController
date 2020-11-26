package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.odometry.dev.GiveTheDefaultConfiguration;
import org.firstinspires.ftc.teamcode.pathFollow.Follower;
import org.firstinspires.ftc.teamcode.utility.pose;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="2D Odometry + IMU Following", group="Autonomous")
public class Odo2DIMUFollowing extends LinearOpMode {
    Mecanum mecanum;
    IMU imu;
    Odometry odometry;

    @Override
    public void runOpMode() throws InterruptedException {
        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);
        odometry = GiveTheDefaultConfiguration.defaultConfiguration(hardwareMap,imu,telemetry);
        Follower follower = new Follower(mecanum, odometry, telemetry);


        waitForStart();

        odometry.setPosition(new pose(0,0,0));
        odometry.start();
        follower.start();

        while (opModeIsActive()){
            mecanum.drive(gamepad1.right_stick_x/3, -gamepad1.right_stick_y/3, -gamepad1.left_stick_x/3);
            //telemetry.update();
        }

        follower.stop();
        odometry.end();
    }
}
