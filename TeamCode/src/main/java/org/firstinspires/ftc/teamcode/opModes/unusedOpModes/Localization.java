package org.firstinspires.ftc.teamcode.opModes.unusedOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.working.Odometry;
import org.firstinspires.ftc.teamcode.odometry.dev.GiveTheDefaultConfiguration;
import org.firstinspires.ftc.teamcode.utility.RotationUtil;
import org.firstinspires.ftc.teamcode.utility.pose;

// localization with drive wheel encoders and IMU heading
// no following; use controller to move
@TeleOp(name="Full Odo + IMU Localization", group="Autonomous")
@Disabled
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
        odometry = GiveTheDefaultConfiguration.defaultConfiguration(hardwareMap,imu,telemetry);

        waitForStart();

        odometry.start();
        odometry.setPosition(new pose(0,0,0));
        while (opModeIsActive()){
            telemetry.addData("Odometry Position", odometry.getPosition());

//            telemetry.addData("frontLeft", frontLeft.getCurrentPosition());
//            telemetry.addData("frontRight", frontRight.getCurrentPosition());
//            telemetry.addData("backRight", backRight.getCurrentPosition());
//            telemetry.addData("backLeft", backLeft.getCurrentPosition());

            telemetry.update();

            double targetDir = -Math.atan2(gamepad1.left_stick_y,gamepad1.left_stick_x) - Math.PI/2;
            double magnitude = Math.hypot(gamepad1.left_stick_y,gamepad1.left_stick_x);
            double turnPwr = RotationUtil.turnLeftOrRight(odometry.getPosition().r, targetDir, Math.PI * 2);

            telemetry.addData("Intended dir", targetDir);
            telemetry.addData("pwr", turnPwr);

            mecanum.drive(gamepad1.right_stick_x/3, -gamepad1.right_stick_y/3,
                    (magnitude > 0.5 && Math.abs(turnPwr) > 0.08)? turnPwr:0);
        }

        odometry.end();
    }

}
