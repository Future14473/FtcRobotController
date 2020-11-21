package org.firstinspires.ftc.teamcode.cv;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name = "Ring Detection", group = "Auto")
public class RingDetectionTest extends LinearOpMode {
    Odometry odometry;
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


        telemetry.addLine("Waiting for start");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("Relative Position", detector.bestWobble());
        }

    }
}
