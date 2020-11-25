package org.firstinspires.ftc.teamcode.odometry;

import org.firstinspires.ftc.teamcode.GivesPosition;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.utility.pose;

public class Odometry2DRot implements GivesPosition {
    OdoWheel2DRot[]  wheels;
    IMU imu;

    pose previousPosition;
    pose deltaPosition;
    volatile pose currentPosition; //Todo check if this needs to be volatile

    double vertical = Math.PI/2; // direction that the wheel faces
    double horizontal = Math.PI;

    volatile boolean running = false;


    Thread loop = new Thread(() -> {
        while (running){
            loop();
        }
    });

    public void start(){ running = true; }
    public void stop(){ running = false; }

    void loop(){
        getDeltaPosition();
        setExtrinsic(getCurrentIntrinsic());
        previousPosition = currentPosition; //update the loop
    }

    public Odometry2DRot(OdoWheel2DRot[] wheels, pose initial, IMU imu){
        this.imu = imu;
        this.wheels = wheels;
        previousPosition = initial;
    }

    void getDeltaPosition(){ //TODO take into account of all mounting positions

        for (OdoWheel2DRot wheel : wheels){
            deltaPosition.r = imu.getHeading();

            // find the delta position and takes into account for turn drift using IMU
            if (wheel.mountAngle == horizontal){
                deltaPosition.x = wheel.getWheelTicks() - wheel.rotationInterferance * deltaPosition.r;
            }
            else if (wheel.mountAngle == vertical) {
                deltaPosition.y = wheel.getWheelTicks() - wheel.rotationInterferance * deltaPosition.r;
            }
        }
    }

    pose getCurrentIntrinsic(){
        // add the deltas to the previous position
        pose p = previousPosition; //make a copy of previous position so we can use it later to find extrinsic
        p.x += deltaPosition.x;
        p.y += deltaPosition.y;
        p.r += deltaPosition.r;
        return p; //note that this is intrinsic
    }

    void setExtrinsic(pose intrinsic){
        intrinsic = getCurrentIntrinsic();
        // previousPosition is currently intrinsic, so we need to rotate it to make it extrinsic
        currentPosition.x  = ((intrinsic.x - previousPosition.x) * Math.cos(deltaPosition.r))
                - ((intrinsic.y - previousPosition.y) * Math.sin(deltaPosition.r))
                + previousPosition.x;

        currentPosition.y = ((intrinsic.x - previousPosition.x) * Math.sin(deltaPosition.r))
                - ((intrinsic.y - previousPosition.y) * Math.cos(deltaPosition.r))
                + previousPosition.y;
    }


    @Override
    public pose getPosition() {
        return currentPosition;
    }
}
