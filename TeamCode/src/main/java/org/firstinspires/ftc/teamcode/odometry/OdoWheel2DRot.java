package org.firstinspires.ftc.teamcode.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

public class OdoWheel2DRot {
    DcMotor wheel;
    double mountAngle;
    double rotationInterferance;


    // Ticks per rev values
    //3983, 3990, 4054, 4041, 4014, 3965, 3991 Avg = 4,005
    double ticksPerRev() {return 4005;}

    // Distance values aka circumfrence
    // Circumfrence = 12cm, Radius = 1.9cm
    double radius (){ return  1.9;} //Centimeters

    public OdoWheel2DRot(double mountAngle, double rotationInterferance, DcMotor wheel){
        this.mountAngle = mountAngle;
        this.rotationInterferance = rotationInterferance;
        this.wheel = wheel;
    }

    public long getWheelTicks() {
        return wheel.getCurrentPosition();
    }
}
