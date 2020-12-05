package org.firstinspires.ftc.teamcode.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.utility.pose;

public class DriveWheelOdometryWheel extends OdometryWheel{
    DcMotor wheel;


    double ticksPerRev() {return 386;}
    double radius (){ return  5;} //Centimeters

    public DriveWheelOdometryWheel(pose offset, DcMotor wheel){
        super(offset);
        this.wheel = wheel;
    }

    @Override
    public long getWheelTicks() {
        return wheel.getCurrentPosition();
    }
    // values from getWheelTicks are off by 2 for some reason
}
