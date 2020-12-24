package org.firstinspires.ftc.teamcode.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.utility.pose;

public class HoroOdoWheel extends OdometryWheel {
    DcMotor wheel;

    // Ticks per rev values
//    3911, 3966, 4005, 3936, 4046, 4003, 4009, 4034,
    double ticksPerRev() {return 3989;}

    // Distance values aka circumfrence
    // Circumfrence = 12cm, Radius = 1.9cm
    double radius (){ return  1.9;} //Centimeters

    public HoroOdoWheel(pose offset, DcMotor wheel){
        super(offset);
        this.wheel = wheel;
    }

    @Override
    public long getWheelTicks() {
        return wheel.getCurrentPosition();
    }
}
