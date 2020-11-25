package org.firstinspires.ftc.teamcode.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.utility.pose;

import java.util.ArrayList;
import java.util.List;

public class GiveTheDefaultConfiguration {
    public static Odometry defaultConfiguration(HardwareMap hardwareMap, IMU imu, Telemetry telemetry){
        DcMotor horizontal = hardwareMap.get(DcMotor.class, "shooter");
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        DcMotor vertical = hardwareMap.get(DcMotor.class, "shooter_adjuster");
        vertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vertical.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


//        OdometryWheel horizontalOdo = new FreeSpinOdoWheel(new pose(17.0,-19.0,Math.PI/2), horizontal);
//        OdometryWheel verticalOdo = new FreeSpinOdoWheel(new pose(-18.0,9.1, Math.PI), vertical);

        OdometryWheel horizontalOdo = new FreeSpinOdoWheel(new pose(0,0,Math.PI/2), horizontal);
        OdometryWheel verticalOdo = new FreeSpinOdoWheel(new pose(0,0, Math.PI), vertical);

        List<OdometryWheel> odometryWheels = new ArrayList<>();

        odometryWheels.add(verticalOdo);
        odometryWheels.add(horizontalOdo);

        // odometry system
        pose initial = new pose(0,0,Math.PI/2);
        return new OdometryThatusesIMUforHeading(imu, initial, odometryWheels, telemetry);
    }
}
