package org.firstinspires.ftc.teamcode.cv;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name = "PhoneCamTest", group = "Auto")
public class PhoneCamTest extends LinearOpMode {
    Detection detector;
    int cameraMonitorViewId;
    OpenCvCamera phoneCam;

    @Override
    public void runOpMode() throws InterruptedException {

        //Setting up Camera

        cameraMonitorViewId = hardwareMap.appContext.getResources().
                getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().
                createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);


        detector = new Detection(telemetry);
        phoneCam.setPipeline(detector);
        phoneCam.openCameraDeviceAsync(
                () -> phoneCam.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT)
        );

        // webcam.openCameraDeviceAsync(() -> webcam.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT));

        waitForStart();

        phoneCam.stopStreaming();
    }

}