package org.firstinspires.ftc.teamcode.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.utility.pose;

public class FreeSpinOdoWheel extends OdometryWheel{
    DcMotor wheel;

    // Ticks per rev values
    //3983, 3990, 4054, 4041, 4014, 3965, 3991 Avg = 4,005
    double ticksPerRev() {return 4005;}

    // Distance values aka circumfrence
    // Circumfrence = 12cm, Radius = 1.9cm
    double radius (){ return  1.9;} //Centimeters

    public FreeSpinOdoWheel(pose offset, DcMotor wheel){
        super(offset);
        this.wheel = wheel;
    }

    @Override
    public long getWheelTicks() {
        return wheel.getCurrentPosition();
    }
}
