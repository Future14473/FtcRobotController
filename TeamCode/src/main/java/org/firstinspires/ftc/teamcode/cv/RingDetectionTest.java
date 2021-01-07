package org.firstinspires.ftc.teamcode.cv;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.GiveTheDefaultConfiguration;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.odometry.OdometryWheel;
import org.firstinspires.ftc.teamcode.utility.pose;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "Ring Detection", group = "Auto")
public class RingDetectionTest extends LinearOpMode {
    Odometry odometry;
    Detection detector;
    int cameraMonitorViewIdP;
    int cameraMonitorViewIdW;
    OpenCvCamera phoneCam;
    OpenCvCamera webcam;
    Mecanum MecanumDrive;
    IMU imu;
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive = new Mecanum(hardwareMap);
        imu = new IMU(hardwareMap, telemetry);

        odometry = GiveTheDefaultConfiguration.odoOnlyConfig(hardwareMap,imu,telemetry);

        //Setting up Camera

        cameraMonitorViewIdP = hardwareMap.appContext.getResources().
                getIdentifier("cameraMonitorViewIdP", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().
                createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewIdP);

        cameraMonitorViewIdW = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewIdW", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewIdW);

        detector = new Detection(telemetry, odometry, MecanumDrive);
        phoneCam.setPipeline(detector);
        phoneCam.openCameraDeviceAsync(
                () -> phoneCam.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT)
        );

        webcam.setPipeline(detector);
       // webcam.openCameraDeviceAsync(() -> webcam.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT));

        waitForStart();

        odometry.start();

        while(opModeIsActive()){
            telemetry.addData("Relative Position", detector.bestWobble());
        }

        phoneCam.stopStreaming();
        odometry.end();
    }

}
