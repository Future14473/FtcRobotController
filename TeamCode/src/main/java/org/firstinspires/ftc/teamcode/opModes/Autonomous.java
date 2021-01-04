package org.firstinspires.ftc.teamcode.opModes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.GiveTheDefaultConfiguration;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.pathFollow.Follower;
import org.firstinspires.ftc.teamcode.pathgen.ImportPath;
import org.firstinspires.ftc.teamcode.pathgen.PathPoint;
import org.firstinspires.ftc.teamcode.utility.Timing;
import org.firstinspires.ftc.teamcode.utility.pose;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="Autonomous", group="Autonomous")
public class Autonomous extends LinearOpMode {
    Mecanum mecanum;
    IMU imu;
    Odometry odometry; //Todo make this rotation odometry
    DcMotor intake;
    DcMotor taco;
    DcMotor shooter_adjuster;
    DcMotor shooter;
    CRServo shooter_roller;
    Timing timer;


    @Override
    public void runOpMode() throws InterruptedException {
        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);

        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter = hardwareMap.get(DcMotor.class, "shooter");

        shooter_adjuster = hardwareMap.get(DcMotor.class, "shooter_adjuster");
        taco = hardwareMap.get(DcMotor.class, "taco");
        shooter_roller = hardwareMap.get(CRServo.class, "shooter_roller");

        odometry = GiveTheDefaultConfiguration.odoOnlyConfig(hardwareMap,imu,telemetry);
        Follower follower = new Follower(mecanum, odometry, imu, telemetry, Autonomous.this);
        timer = new Timing(Autonomous.this);

        waitForStart();
//        shooter.setPower(-0.5);
        odometry.start();
        odometry.setPosition(new pose(ImportPath.origin.x, ImportPath.origin.y, ImportPath.origin.dir));
        boolean finished = false;
//        follower.start();

    // TODO use rotation odometry and check to see that localization works
        while (opModeIsActive() && !finished){

            follower.goTo(ImportPath.powerShotRight0);
            follower.goTo(ImportPath.powerShotRight);

//            shooter_roller.setPower(1);
//            taco.setPower(0.5);
            timer.safeDelay(5000);
//            shooter_roller.setPower(0);

            telemetry.addData("done with auto" , ":)");
            telemetry.update();
            finished = true;
//            shooter.setPower(-0.5);
//            taco.setPower(0.5);
//            shooter_roller.setPower(1);
//            sleep(2000);
//            intake.setPower(0.5);



//            intake.setPower(1.0); // build needs to fix the intake

//            follower.goTo(ImportPath.test);
//            follower.goTo(ImportPath.ringStack01);
//            follower.goTo(ImportPath.ringStack02);
//            follower.goTo(ImportPath.ringStack1);
//            intake.setPower(0);
//            follower.goTo(ImportPath.targetA0);
//            follower.goTo(ImportPath.targetA1);
            //follower.goTo(ImportPath.targetA);



        }

        follower.stop();
        odometry.end();
    }
}
