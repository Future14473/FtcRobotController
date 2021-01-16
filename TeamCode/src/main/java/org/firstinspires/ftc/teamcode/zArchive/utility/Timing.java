package org.firstinspires.ftc.teamcode.zArchive.utility;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Timing {
    LinearOpMode opMode;

    public Timing (LinearOpMode opMode){
        this.opMode = opMode;
    }

    public void safeDelay(long delay){
        long start = System.currentTimeMillis();
        while((System.currentTimeMillis() - start < delay) && opMode.opModeIsActive()){
            //wait
        }
    }

    public static void delay(long delay){
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < delay) {
            //wait
        }
    }
}