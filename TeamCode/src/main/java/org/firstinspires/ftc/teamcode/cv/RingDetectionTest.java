package org.firstinspires.ftc.teamcode.cv;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.odometry.working.DriveWheelOdometryWheel;
import org.firstinspires.ftc.teamcode.odometry.working.Odometry;
import org.firstinspires.ftc.teamcode.odometry.working.OdometryWheel;
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
    @Override
    public void runOpMode() throws InterruptedException {

        odometry = defaultConfiguration();

        //Setting up Camera

        cameraMonitorViewIdP = hardwareMap.appContext.getResources().
                getIdentifier("cameraMonitorViewIdP", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().
                createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewIdP);

        cameraMonitorViewIdW = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewIdW", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewIdW);

        detector = new Detection(telemetry, odometry);
        phoneCam.setPipeline(detector);
        phoneCam.openCameraDeviceAsync(
                () -> phoneCam.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT)
        );

        webcam.setPipeline(detector);
        webcam.openCameraDeviceAsync(() -> webcam.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT));

        waitForStart();

        odometry.start();

        while(opModeIsActive()){
            telemetry.addData("Relative Position", detector.bestWobble());
        }

        phoneCam.stopStreaming();
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
