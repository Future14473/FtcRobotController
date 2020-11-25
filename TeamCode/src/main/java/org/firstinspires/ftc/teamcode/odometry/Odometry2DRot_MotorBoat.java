package org.firstinspires.ftc.teamcode.odometry;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GivesPosition;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.utility.pose;


public class Odometry2DRot_MotorBoat implements GivesPosition {
    Telemetry telemetry;
    IMU imu;

    OdoWheel2DRot horo;
    OdoWheel2DRot vert;

    volatile public pose totalDeltaPosition;


    volatile pose currentPosition; //Todo check if this needs to be volatile



    volatile boolean running = false;


    private final Thread loop = new Thread(() -> {
        while (running){
            totalDeltaPosition.x = ticksToDistance(horo);
            totalDeltaPosition.y = ticksToDistance(vert);

        }
    });

    public void start(){ running = true; loop.start();}
    public void stop(){ running = false; }

    void loop(){

//        getDeltaPosition();
//        setExtrinsic(getCurrentIntrinsic());
//        previousPosition = currentPosition; //update the loop
    }

    public Odometry2DRot_MotorBoat(OdoWheel2DRot horo, OdoWheel2DRot vert, pose initial, IMU imu, Telemetry telemetry){
        this.imu = imu;
        this.horo = horo;
        this.vert = vert;
        this.telemetry = telemetry;
    }

    void updateDelta(){
        totalDeltaPosition.x = ticksToDistance(horo);
        totalDeltaPosition.y = ticksToDistance(vert);
    }

    double ticksToDistance(OdoWheel2DRot wheel){

        return (wheel.getWheelTicks() / wheel.ticksPerRev())
                * 2 * Math.PI * wheel.radius() * -1; //values are negative because of the way it is mounted
    }

    @Override
    public pose getPosition() {
        return currentPosition;
    }
}
