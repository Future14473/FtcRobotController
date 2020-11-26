
package org.firstinspires.ftc.teamcode.odometry.dev;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GivesPosition;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.utility.pose;


public class Odometry2DRot_complicated implements GivesPosition {
    Telemetry telemetry;
    OdoWheel2DRot[]  wheels;
    IMU imu;

    pose previousPosition = new pose(0,0,0);
    volatile pose currentPosition = new pose(0,0,0); //Todo check if this needs to be volatile

    pose previousDelta = new pose(0,0,0);
    pose currentDelta = new pose(0,0,0); // delta during the time between odometry updates
    pose totalDelta = new pose (0,0,0); // only used for getting currentDelta, otherwise ignore

    OdoWheel2DRot horo;
    OdoWheel2DRot vert;


    double vertical = -Math.PI/2; // direction that the wheel faces
    double horizontal =  Math.PI;

    volatile boolean running = false;


    private final Thread loop = new Thread(() -> {
        while (running){
            loop();
        }
    });

    public void start(){ running = true; loop.start();}
    public void stop(){ running = false; }

    void loop(){
        updateTotalDelta();
        updateCurrentDelta();
        Log.e("Current Delta", currentDelta.toString());

        Log.e("Absolute Angle ", String.valueOf(totalDelta.r));
        Log.e("Previous Angle ", String.valueOf(previousDelta.r));
        Log.e("Change in R", String.valueOf(currentDelta.r));

//       setExtrinsic(currentDelta);
        pose intrinsicPosition = getCurrentIntrinsic();
       Log.e("intrinsic delta total", intrinsicPosition.toString());


           setExtrinsic(getCurrentIntrinsic());
//           telemetry.addData("Current Position", currentPosition);
        telemetry.addData("Previous Position, ", previousPosition.toString());
        telemetry.addData("Current Position, ", currentPosition.toString());
           previousPosition = currentPosition;
        telemetry.update();
    }

    public Odometry2DRot_complicated(OdoWheel2DRot[] wheels, pose initial, IMU imu, Telemetry telemetry){
        this.imu = imu;
        this.wheels = wheels;
        previousPosition = initial;
        this.telemetry = telemetry;
        for (OdoWheel2DRot wheel : wheels) { //TODO take into account of all mounting positions
            if (wheel.mountAngle == horizontal) {
                horo = wheel;
            } else if (wheel.mountAngle == vertical) {
                vert = wheel;
            }

        }
    }

    double ticksToDistance(OdoWheel2DRot wheel){
        return (wheel.getWheelTicks() / wheel.ticksPerRev())
                * 2 * Math.PI * wheel.radius() * -1;

    }

    public void setPosition(pose aNewPosition){ currentPosition = aNewPosition; }

    void updateTotalDelta() {
        totalDelta.x = ticksToDistance(horo);
        totalDelta.y = ticksToDistance(vert);
        totalDelta.r = imu.getHeading();
    }

    void updateCurrentDelta(){

        // gyroscope is special, everything is absolute not a sum, but we keep the same poses as before
        // to make things easier to calculate
        currentDelta.r = Math.abs(totalDelta.r) - previousDelta.r;
        previousDelta.r = currentDelta.r; // not adding just re-setting value

        // delta distance = delta of axis + any rotation interference
        // derived formula --> delta of axis =  delta distance - rotation interference
//        currentDelta.x = totalDelta.x - previousDelta.x - (horo.rotationInterferance * currentDelta.r)
        currentDelta.x = totalDelta.x - previousDelta.x;
        previousDelta.x += currentDelta.x;

//        currentDelta.y = totalDelta.y - previousDelta.y - (vert.rotationInterferance * currentDelta.r);
        currentDelta.y = totalDelta.y - previousDelta.y;
        previousDelta.y += currentDelta.y;


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
