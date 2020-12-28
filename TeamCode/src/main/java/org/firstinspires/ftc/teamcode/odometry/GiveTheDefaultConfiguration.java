package org.firstinspires.ftc.teamcode.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.odometry.resources.DriveWheelOdometryWheel;
import org.firstinspires.ftc.teamcode.odometry.resources.OdometryThatusesIMUforHeading;
import org.firstinspires.ftc.teamcode.pathgen.ImportPath;
import org.firstinspires.ftc.teamcode.utility.pose;

import java.util.ArrayList;
import java.util.List;

public class GiveTheDefaultConfiguration {
    public static Odometry defaultConfiguration(HardwareMap hardwareMap, IMU imu, Telemetry telemetry){
        DcMotor horizontal = hardwareMap.get(DcMotor.class, "taco");
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        OdometryWheel frontRightOdo = new DriveWheelOdometryWheel(new pose(178.5,168,Math.PI), frontRight);
        OdometryWheel frontLeftOdo = new DriveWheelOdometryWheel(new pose(-178.5,168,Math.PI), frontLeft);
        OdometryWheel backRightOdo = new DriveWheelOdometryWheel(new pose(178.5,-168,Math.PI), backRight);
        OdometryWheel backLeftOdo = new DriveWheelOdometryWheel(new pose(-178.5,-168,Math.PI), backLeft);

        OdometryWheel horizontalOdo = new FreeSpinOdoWheel(new pose(17.0,-19.0,Math.PI/2), horizontal);

        List<OdometryWheel> odometryWheels = new ArrayList<>();


        odometryWheels.add(horizontalOdo);
        odometryWheels.add(frontLeftOdo);
        odometryWheels.add(frontRightOdo);
        odometryWheels.add(backLeftOdo);
        odometryWheels.add(backRightOdo);
        telemetry.addData("Phys odom", odometryWheels);

        // odometry system
        pose initial = new pose(ImportPath.getOrigin().x, ImportPath.getOrigin().y,
                ImportPath.getOrigin().dir);
        return new OdometryThatusesIMUforHeading(imu, initial, odometryWheels, telemetry);
    }

    public static Odometry odoOnlyConfig(HardwareMap hardwareMap, IMU imu, Telemetry telemetry){
        DcMotor horizontal = hardwareMap.get(DcMotor.class, "taco");
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        DcMotor vertical = hardwareMap.get(DcMotor.class, "shooter");
        vertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vertical.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        OdometryWheel horizontalOdo = new FreeSpinOdoWheel(new pose(17.0,-19.0,Math.PI/2), horizontal);
        OdometryWheel verticalOdo = new FreeSpinOdoWheel(new pose(-18.0,9.1, Math.PI), vertical);

//        OdometryWheel horizontalOdo = new FreeSpinOdoWheel(new pose(10000000,10000000,Math.PI/2), horizontal);
//        OdometryWheel verticalOdo = new FreeSpinOdoWheel(new pose(10000000,10000000, Math.PI), vertical);

        List<OdometryWheel> odometryWheels = new ArrayList<>();

        odometryWheels.add(verticalOdo);
        odometryWheels.add(horizontalOdo);

        // odometry system
        pose initial = new pose(ImportPath.origin.x,ImportPath.origin.y,ImportPath.origin.dir);
        return new OdometryThatusesIMUforHeading(imu, initial, odometryWheels, telemetry);
    }

    public static RotationOdometry rotationOdometryConfig(HardwareMap hardwareMap, IMU imu, Telemetry telemetry){
        DcMotor horizontal = hardwareMap.get(DcMotor.class, "taco");
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        DcMotor vertical = hardwareMap.get(DcMotor.class, "shooter");
        vertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vertical.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        OdometryWheel horizontalOdo = new HoroOdoWheel(new pose(17.0,-19.0,Math.PI/2), horizontal);
        OdometryWheel verticalOdo = new VertOdoWheel(new pose(-18.0,9.1, Math.PI), vertical);

//        OdometryWheel horizontalOdo = new FreeSpinOdoWheel(new pose(10000000,10000000,Math.PI/2), horizontal);
//        OdometryWheel verticalOdo = new FreeSpinOdoWheel(new pose(10000000,10000000, Math.PI), vertical);

        List<OdometryWheel> odometryWheels = new ArrayList<>();

        odometryWheels.add(verticalOdo);
        odometryWheels.add(horizontalOdo);

        // odometry system
        pose initial = new pose(ImportPath.origin.x,ImportPath.origin.y,ImportPath.origin.dir);
        return new RotationOdometry(initial, imu, odometryWheels);
    }
}
