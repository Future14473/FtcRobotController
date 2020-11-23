package org.firstinspires.ftc.teamcode.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.utility.pose;

public class FrictionFreeSpinOdoWheel extends OdometryWheel{
    DcMotor wheel;

    // Ticks per rev values

    double ticksPerRev() {return 4005;}

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
