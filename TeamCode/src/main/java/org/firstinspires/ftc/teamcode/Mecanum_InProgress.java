package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Mecanum_InProgress {
    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;
    double f;
    double s;
    DcMotorEx frontRight;
    DcMotorEx frontLeft;
    DcMotorEx backRight;
    DcMotorEx backLeft;


    public Mecanum_InProgress(){

    }

    public Mecanum_InProgress(HardwareMap hardwareMap ){
        frontLeft = hardwareMap.get(DcMotorEx.class, "top_left_drive");
        frontRight = hardwareMap.get(DcMotorEx.class, "top_right_drive");
        backLeft = hardwareMap.get(DcMotorEx.class, "bottom_left_drive");
        backRight = hardwareMap.get(DcMotorEx.class, "bottom_right_drive");

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
    }


    public void set_motor_values(double F, double S, double turn){
        frontRightPower = F - S + turn;
        backRightPower = F + S + turn;
        frontLeftPower = F + S - turn;
        backLeftPower = F - S - turn;
    }

    public void set_forward_strafe(double angle){
        angle = Math.toRadians(angle);
        f = Math.sin(angle);
        s = Math.cos(angle);
    }

    public void move(double f, double s, double turn){
//        set_forward_strafe(angle);
        set_motor_values(f, s, turn);
//        topRightDrive.setVelocity(topRightPower);
//        topLeftDrive.setVelocity(topLeftPower);
//        bottomLeftDrive.setVelocity(bottomLeftPower);
//        bottomRightDrive.setVelocity(bottomRightPower);
        frontLeftDrive.setVelocity(  (int)(maxTicksPerSec * (ySpeed + xSpeed + turnSpeed)));
        frontRight.setVelocity( (int)(maxTicksPerSec * (ySpeed - xSpeed - turnSpeed)));
        backLeft.setVelocity(   (int)(maxTicksPerSec * (ySpeed - xSpeed + turnSpeed)));
        backRight.setVelocity(  (int)(maxTicksPerSec * (ySpeed + xSpeed - turnSpeed)));

    }
}
