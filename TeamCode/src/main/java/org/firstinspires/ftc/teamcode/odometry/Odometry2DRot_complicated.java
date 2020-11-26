
package org.firstinspires.ftc.teamcode.odometry;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GivesPosition;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.utility.pose;

import java.util.ArrayList;


public class Odometry2DRot_complicated implements GivesPosition {
    Telemetry telemetry;
    OdoWheel2DRot[]  wheels;
    IMU imu;

    pose previousPosition = new pose(0,0,0);
    volatile pose currentPosition = new pose(0,0,0); //Todo check if this needs to be volatile

    pose currentDelta = new pose(0,0,0);
    pose totalTicks = new pose (0,0,0);
    pose sumPrevious = new pose(0,0,0);

//    private pose[] previousDeltaPositions;


    double vertical = -Math.PI/2; // direction that the wheel faces
    double horizontal =  Math.PI;

    volatile boolean running = false;


    private final Thread loop = new Thread(() -> {
        while (running){
           updateTotalDelta(wheels);
           updateCurentDelta();
//            telemetry.addData("Total Wheel Displacement", totalTicks);
//           telemetry.addData("current delta ", currentDelta);
           setExtrinsic(currentDelta);
           setExtrinsic(getCurrentIntrinsic());
           telemetry.addData("Current Position", currentPosition);
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

    void updateTotalDelta(OdoWheel2DRot[] wheels) { //TODO take into account of all mounting positions
        for (OdoWheel2DRot wheel : wheels) {
            if (wheel.mountAngle == horizontal) {
                totalTicks.x = ticksToDistance(wheel);
            } else if (wheel.mountAngle == vertical) {
                totalTicks.y = ticksToDistance(wheel);
            }

        }
        totalTicks.r = imu.getHeading();
    }
    // for debugging
//    ArrayList<Double> listOfXDelta = new ArrayList<Double>();

    void updateCurentDelta(){
        currentDelta.x = totalTicks.x - sumPrevious.x;
//        listOfXDelta.add(currentDelta.x);

//        Log.e("Interval Wheel Displacement", listOfXDelta.toString());
        sumPrevious.x += currentDelta.x;

        currentDelta.y = totalTicks.y - sumPrevious.y;
        sumPrevious.y += currentDelta.y;

        // gyroscope is special, everything is absolute not a sum, but we keep the same poses as before
        // to make things easier to calculate
        currentDelta.r = totalTicks.r - sumPrevious.r;
        sumPrevious.r = currentDelta.r; // not adding just re-setting value
    }

    pose getCurrentIntrinsic(){
        // add the deltas to the previous position
        pose p = previousPosition;
        p.x += currentDelta.x;
        p.y += currentDelta.y;
        p.r = currentDelta.r;
        return p; //note that this is intrinsic
    }

//    void resetDeltaPosition(){
//        sumDeltaPosition = currentDeltaPosition;
//    }

    void setExtrinsic(pose intrinsic){
        // previousPosition is currently intrinsic, so we need to rotate it to make it extrinsic
        currentPosition.x  = ((intrinsic.x - previousPosition.x) * Math.cos(currentDelta.r))
                - ((intrinsic.y - previousPosition.y) * Math.sin(currentDelta.r))
                + previousPosition.x;

        currentPosition.y = ((intrinsic.x - previousPosition.x) * Math.sin(currentDelta.r))
                - ((intrinsic.y - previousPosition.y) * Math.cos(currentDelta.r))
                + previousPosition.y;

        // the delta angle has been used above for calculations, now turn it back to absolute angle
        currentPosition.r = imu.getHeading();
    }


    @Override
    public pose getPosition() {
        return currentPosition;
    }
}
