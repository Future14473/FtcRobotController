package org.firstinspires.ftc.teamcode.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.utility.pose;

public class VertOdoWheel extends OdometryWheel {
    DcMotor wheel;

    // Ticks per rev values
    //4019, 3996, 4067, 3927, 3970, 3991, 4039, 3946
    double ticksPerRev() {return 3994;}

    // Distance values aka circumfrence
    // Circumfrence = 12cm, Radius = 1.9cm
    double radius (){ return  1.9;} //Centimeters

    public VertOdoWheel(pose offset, DcMotor wheel){
        super(offset);
        this.wheel = wheel;
    }

    @Override
    public long getWheelTicks() {
        return wheel.getCurrentPosition();
    }
}
