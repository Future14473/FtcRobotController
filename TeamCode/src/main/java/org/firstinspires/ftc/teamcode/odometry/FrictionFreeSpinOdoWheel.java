package org.firstinspires.ftc.teamcode.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.utility.pose;
// doesn't seem to be needed anymore, DO NOT USE
public class FrictionFreeSpinOdoWheel extends OdometryWheel{
    DcMotor wheel;

    // Ticks per rev values
    //2408, 2450,2483
    double ticksPerRev() {return 2447;}

    // Distance values aka circumfrence
    // Circumfrence = 12cm, Radius = 1.9cm
    double radius (){ return  1.9;} //Centimeters

    public FrictionFreeSpinOdoWheel(pose offset, DcMotor wheel){
        super(offset);
        this.wheel = wheel;
    }

    @Override
    public long getWheelPosition() {
        return wheel.getCurrentPosition();
    }
}
// good one Y black blue o
// 1234 --> 1423