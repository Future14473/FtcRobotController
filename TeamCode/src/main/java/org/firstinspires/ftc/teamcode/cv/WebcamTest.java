package org.firstinspires.ftc.teamcode.cv;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.DriveWheelOdometryWheel;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.odometry.OdometryWheel;
import org.firstinspires.ftc.teamcode.pathFollow.Follower;
import org.firstinspires.ftc.teamcode.pathgen.PathPoint;
import org.firstinspires.ftc.teamcode.utility.pose;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "WebcamTest", group = "Auto")
public class WebcamTest extends LinearOpMode {
    Mecanum MecanumDrive;
    Odometry odometry;
    Detection detector;
    IMU imu;
    int cameraMonitorViewId;
    OpenCvCamera webcam;
    Follower follower;

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive = new Mecanum(hardwareMap);
        odometry = defaultConfiguration();
        imu = new IMU(hardwareMap, telemetry);
        follower = new Follower(MecanumDrive, odometry, imu, telemetry);


        //Setting up Camera
        cameraMonitorViewId = hardwareMap.appContext.getResources().
                getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().
                createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        detector = new Detection(telemetry, odometry, MecanumDrive);


        webcam.setPipeline(detector);
        webcam.openCameraDeviceAsync(() -> {
            webcam.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT);
        });

        waitForStart();
        odometry.start();

        while(opModeIsActive()){
            double angle = detector.jankAngle(detector.bestWobble());

            telemetry.addData("Angle", angle);
            telemetry.update();
//            follower.goTo(new PathPoint(0, 0, angle)); testing the auto so had to comment this out sorry


        }


        wait(10000);
        webcam.stopStreaming();
        odometry.end();
    }


    Odometry defaultConfiguration(){
        //physical wheels
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
//        DcMotor vertical = hardwareMap.get(DcMotor.class, "vertical");
//        DcMotor horizontal = hardwareMap.get(DcMotor.class, "horizontal");

        //odometry wheels
        OdometryWheel frontRightOdo = new DriveWheelOdometryWheel(new pose(178.5,168,Math.PI/2), frontRight);
        OdometryWheel frontLeftOdo = new DriveWheelOdometryWheel(new pose(-178.5,168,Math.PI/2), frontLeft);
        OdometryWheel backRightOdo = new DriveWheelOdometryWheel(new pose(178.5,-168,Math.PI/2), backRight);
        OdometryWheel backLeftOdo = new DriveWheelOdometryWheel(new pose(-178.5,-168,Math.PI/2), backLeft);
//        OdometryWheel verticalOdo = new FreeSpinOdoWheel(new pose(-180,91,Math.PI/2), vertical);
//        OdometryWheel horizontalOdo = new FreeSpinOdoWheel(new pose(170,-190,0), horizontal);

        List<OdometryWheel> odometryWheels = new ArrayList<>();
        odometryWheels.add(frontLeftOdo);
        odometryWheels.add(frontRightOdo);
        odometryWheels.add(backLeftOdo);
        odometryWheels.add(backRightOdo);
//        odometryWheels.add(verticalOdo);
//        odometryWheels.add(horizontalOdo);

        // odometry system
        pose initial = new pose(0,0,Math.PI/2);
        return new Odometry(initial, odometryWheels);
    }

}
