package org.firstinspires.ftc.teamcode.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.utility.pose;

public class HoroOdoWheel extends OdometryWheel {
    DcMotor wheel;

    // Recalibration
    // 4050, 3919, 4093, 3961, 4033, 4017 = 4012
    double ticksPerRev() {return 4012;}

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
