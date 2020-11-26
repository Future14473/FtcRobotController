package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.odometry.dev.GiveTheDefaultConfiguration;
import org.firstinspires.ftc.teamcode.pathFollow.Follower;
import org.firstinspires.ftc.teamcode.utility.RotationUtil;
import org.firstinspires.ftc.teamcode.utility.pose;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="2 Odometry + IMU Following Manual", group="Autonomous")
public class OdoIMUFollowingManual extends LinearOpMode {
    Mecanum mecanum;
    IMU imu;
    Odometry odometry;
    DcMotor vertical;
    DcMotor horizontal;

    @Override
    public void runOpMode() throws InterruptedException {
        mecanum = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);
        odometry = GiveTheDefaultConfiguration.defaultConfiguration(hardwareMap,imu,telemetry);
        Follower pathFollower = new Follower(mecanum, odometry, telemetry);


        waitForStart();

        odometry.setPosition(new pose(0,0,0));
        odometry.start();
        //pathFollower.start();

        boolean up = gamepad1.y;
        boolean down = gamepad1.a;
        pose dest = new pose(0,0,0);

        while (opModeIsActive()){
            telemetry.addData("Odometry Position", odometry.getPosition());

            up = gamepad1.y;
            down = gamepad1.a;

            pose position = odometry.getPosition();
            if(up)
                dest.y = 10;
            if(down)
                dest.y=-10;

            pose diff = new pose(dest.x-position.x, dest.y-position.y,
                    RotationUtil.turnLeftOrRight(position.r, dest.r, Math.PI * 2));

            telemetry.addData("intention",diff);

            if(up || down){
                mecanum.drive(
                        (Math.abs(diff.x)>1)?  (diff.x/20 + 0.1 * Math.signum(diff.x)):0,
                        (Math.abs(diff.y)>1)?  (diff.y/20 + 0.1 * Math.signum(diff.y)):0,
                        (Math.abs(diff.r)>0.05)?(diff.r    + 0.1 * Math.signum(diff.r)):0
                );
            }

            mecanum.drive(gamepad1.right_stick_x/3, -gamepad1.right_stick_y/3, -gamepad1.left_stick_x/3);
            telemetry.update();

        }

        //pathFollower.stop();
        odometry.end();
    }
}
