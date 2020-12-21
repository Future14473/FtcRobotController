package org.firstinspires.ftc.teamcode.opModes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.GiveTheDefaultConfiguration;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.pathFollow.Follower;
import org.firstinspires.ftc.teamcode.pathgen.ImportPath;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="Autonomous", group="Autonomous")
public class Autonomous extends LinearOpMode {
    Mecanum mecanum;
    IMU imu;
    Odometry odometry; //Todo make this rotation odometry
    DcMotor intake;


    @Override
    public void runOpMode() throws InterruptedException {
        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);

        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        odometry = GiveTheDefaultConfiguration.odoOnlyConfig(hardwareMap,imu,telemetry);
        Follower follower = new Follower(mecanum, odometry, imu, telemetry, Autonomous.this);


        waitForStart();


        odometry.start();
//        follower.start();

    // TODO use rotation odometry and check to see that localization works
        while (opModeIsActive()){
//            intake.setPower(1.0); // build needs to fix the intake

//            follower.goTo(ImportPath.test);
//            follower.goTo(ImportPath.ringStack01);
            follower.goTo(ImportPath.ringStack02);
            follower.goTo(ImportPath.ringStack1);
            intake.setPower(0);
            follower.goTo(ImportPath.targetA0);
            follower.goTo(ImportPath.targetA1);
            //follower.goTo(ImportPath.targetA);


            telemetry.update();
        }

        follower.stop();
        odometry.end();
    }
}
