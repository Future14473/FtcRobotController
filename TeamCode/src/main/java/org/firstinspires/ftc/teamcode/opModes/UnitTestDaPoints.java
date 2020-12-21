package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.GiveTheDefaultConfiguration;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.odometry.RotationOdometry;
import org.firstinspires.ftc.teamcode.pathFollow.Follower;
import org.firstinspires.ftc.teamcode.pathgen.ImportPath;
import org.firstinspires.ftc.teamcode.pathgen.PathPoint;
import org.firstinspires.ftc.teamcode.utility.pose;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="UnitTestPoints", group="Autonomous")
public class UnitTestDaPoints extends LinearOpMode {
    Mecanum mecanum;
    IMU imu;
    RotationOdometry odometry;
    DcMotor intake;


    @Override
    public void runOpMode() throws InterruptedException {
        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);

        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        odometry = GiveTheDefaultConfiguration.rotationOdometryConfig(hardwareMap,imu,telemetry);
        Follower follower = new Follower(mecanum, odometry, imu, telemetry, UnitTestDaPoints.this);


        waitForStart();

        odometry.setPosition(new pose(0,0,0));
        odometry.start();
//        follower.start();

    // TODO use rotation odometry and check to see that localization works
        while (opModeIsActive()){
            if (gamepad1.x){
                follower.goTo(new PathPoint(0,60.96,0));
            }
            if (gamepad1.y){
                follower.goTo(new PathPoint(60.96,0,0));
            }
            if (gamepad1.a){
                follower.goTo(new PathPoint(0,0, Math.PI/2));

            }

            if (gamepad1.b){
                follower.goTo(new PathPoint(0,0, 0)); //reset position

            }

            telemetry.update();
        }

        follower.stop();
        odometry.end();
    }
}
