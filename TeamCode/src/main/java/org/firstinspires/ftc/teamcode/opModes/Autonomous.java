package org.firstinspires.ftc.teamcode.opModes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.GiveTheDefaultConfiguration;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.pathFollow.Follower;
import org.firstinspires.ftc.teamcode.pathgen.ImportPath;
import org.firstinspires.ftc.teamcode.utility.pose;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="Autonomous", group="Autonomous")
public class Autonomous extends LinearOpMode {
    Mecanum mecanum;
    IMU imu;
    Odometry odometry;
    DcMotor intake;


    @Override
    public void runOpMode() throws InterruptedException {
        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);
        intake = hardwareMap.get(DcMotor.class, "intake");
        odometry = GiveTheDefaultConfiguration.odoOnlyConfig(hardwareMap,imu,telemetry);
        Follower follower = new Follower(mecanum, odometry, imu, telemetry);


        waitForStart();


        odometry.start();
        follower.start();

    // TODO use rotation odometry and check to see that localization works
        while (opModeIsActive()){
            while (follower.goTowards(ImportPath.ringStack)){}
            telemetry.update();
        }

        follower.stop();
        odometry.end();
    }
}
