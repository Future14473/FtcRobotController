
package org.firstinspires.ftc.teamcode.odometry;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GivesPosition;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.odometry.OdoWheel2DRot;
import org.firstinspires.ftc.teamcode.utility.pose;


public class Odometry2DRot_complicated implements GivesPosition {
    Telemetry telemetry;
    OdoWheel2DRot[]  wheels;
    IMU imu;

    pose previousPosition;
    volatile pose currentPosition; //Todo check if this needs to be volatile

    pose sumDeltaPosition = new pose (0,0,0);
    pose actualDeltaPosition = new pose(0,0,0);

//    private pose[] previousDeltaPositions;


    double vertical = -Math.PI/2; // direction that the wheel faces
    double horizontal =  Math.PI;

    volatile boolean running = false;


    private final Thread loop = new Thread(() -> {
        while (running){
           updateDelta(wheels);
        }
    });

    public void start(){ running = true; loop.start();}
    public void stop(){ running = false; }

    void loop(){

//        getDeltaPosition();
//        setExtrinsic(getCurrentIntrinsic());
//        previousPosition = currentPosition; //update the loop
    }

    public Odometry2DRot_complicated(OdoWheel2DRot[] wheels, pose initial, IMU imu, Telemetry telemetry){
        this.imu = imu;
        this.wheels = wheels;
        previousPosition = initial;
        this.telemetry = telemetry;
    }

    double ticksToDistance(OdoWheel2DRot wheel){

        return (wheel.getWheelTicks() / wheel.ticksPerRev())
                * 2 * Math.PI * wheel.radius() * -1; //values are negative because of the way it is mounted
    }

    void updateDelta(OdoWheel2DRot[] wheels) { //TODO take into account of all mounting positions
        for (OdoWheel2DRot wheel : wheels) {
            if (wheel.mountAngle == horizontal) {
                sumDeltaPosition.x = ticksToDistance(wheel);
            } else if (wheel.mountAngle == vertical) {
                sumDeltaPosition.y = ticksToDistance(wheel);
            }
            telemetry.addData("Total Wheel Displacement", sumDeltaPosition);
            telemetry.addData("Interval Wheel Displacement", actualDeltaPosition);

        }
    }


//    pose getCurrentIntrinsic(){
//        // add the deltas to the previous position
//        pose p = previousPosition; //make a copy of previous position so we can use it later to find extrinsic
//        p.x += currentDeltaPosition.x;
//        p.y += currentDeltaPosition.y;
//        p.r = currentDeltaPosition.r;
//        return p; //note that this is intrinsic
//    }

//    void resetDeltaPosition(){
//        sumDeltaPosition = currentDeltaPosition;
//    }

//    void setExtrinsic(pose intrinsic){
//        intrinsic = getCurrentIntrinsic();
//        // previousPosition is currently intrinsic, so we need to rotate it to make it extrinsic
//        currentPosition.x  = ((intrinsic.x - previousPosition.x) * Math.cos(currentDeltaPosition.r))
//                - ((intrinsic.y - previousPosition.y) * Math.sin(currentDeltaPosition.r))
//                + previousPosition.x;
//
//        currentPosition.y = ((intrinsic.x - previousPosition.x) * Math.sin(currentDeltaPosition.r))
//                - ((intrinsic.y - previousPosition.y) * Math.cos(currentDeltaPosition.r))
//                + previousPosition.y;
//    }


    @Override
    public pose getPosition() {
        return currentPosition;
    }
}
