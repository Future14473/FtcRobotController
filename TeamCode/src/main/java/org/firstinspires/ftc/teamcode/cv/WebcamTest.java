package org.firstinspires.ftc.teamcode.cv;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.GiveTheDefaultConfiguration;
import org.firstinspires.ftc.teamcode.odometry.Odometry;

import org.firstinspires.ftc.teamcode.pathFollow.Follower;
import org.firstinspires.ftc.teamcode.pathgen.PathPoint;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import static org.firstinspires.ftc.teamcode.utility.Timing.delay;

@Autonomous(name = "WebcamTest", group = "Auto")
public class WebcamTest extends LinearOpMode {
    Mecanum MecanumDrive;
    Odometry odometry;
    IMU imu;
    Follower follower;

    Detection detector;
    int cameraMonitorViewId;
    OpenCvCamera webcam;

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive = new Mecanum(hardwareMap);
        odometry = GiveTheDefaultConfiguration.rotationOdometryConfig(hardwareMap,imu,telemetry);
        imu = new IMU(hardwareMap, telemetry);
        follower = new Follower(MecanumDrive, odometry, imu, telemetry);


        //Setting up Camera
        cameraMonitorViewId = hardwareMap.appContext.getResources().
                getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().
                createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        detector = new Detection(telemetry, odometry, MecanumDrive);


        webcam.setPipeline(detector);

        //Opening and Streaming from Camera

        webcam.openCameraDeviceAsync(() -> {
            webcam.startStreaming(352, 288, OpenCvCameraRotation.UPRIGHT);
        });


        waitForStart();
        odometry.start();

        while(opModeIsActive()){
            for(int i = 0; i< 100; i++){
                telemetry.addData("ree", "reee");
                telemetry.update();
            }

        }

        webcam.stopStreaming();
        odometry.end();
    }
}
