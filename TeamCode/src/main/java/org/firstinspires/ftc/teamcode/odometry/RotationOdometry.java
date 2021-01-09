package org.firstinspires.ftc.teamcode.odometry;


import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.utility.*;

import java.util.List;
import java.util.function.Function;

public class RotationOdometry extends Odometry {
    IMU imu;
    double prevAngle = 0;
    List<OdometryWheel> wheels;
    public OdometryWheel horoWheel;
    public OdometryWheel vertWheel;
    double horoRotConst =  -9.828 +  0.67945;
    double vertRotConst =  35.889 + 5.180;
    public double rotDelta; //TODO make this not public later
    public double horoTrans; //TODO make this not public later
    public double vertTrans; //TODO make this not public later

    public RotationOdometry(pose initial, IMU imu, List<OdometryWheel> wheels) {
        super(initial, wheels);
        this.imu = imu;
        this.wheels = wheels;

    }

    protected void loop() {
        wheels.forEach(OdometryWheel::updateDelta);

        pose delta = getDeltaPose();

        pose curved = curvedTrajectoryTranslation(new pose(delta.x, delta.y, delta.r));
        // rotate the delta (x,y,r) by -90 degs
        point itrans = point.rotate(curved.x, curved.y, -Math.PI/2);
        curved.x = -itrans.x;
        curved.y = -itrans.y;

        //position.translateRelative(curved);
        position.translateRelative(delta);

    }

    public pose getDeltaPose(){
        double imuHeading = imu.getHeading();
        rotDelta = RotationUtil.turnLeftOrRight(prevAngle, imuHeading, Math.PI/2);
        prevAngle = imuHeading;

        horoTrans = horoWheel.dotProduct(horoWheel.getDeltaDistance(), 0) - horoWheel.dotProduct(rotDelta * horoWheel.distanceToCenter(), horoWheel.ccTangentDir());
        vertTrans = vertWheel.dotProduct(horoWheel.getDeltaDistance(), Math.PI/2) - vertWheel.dotProduct(rotDelta * vertWheel.distanceToCenter(), vertWheel.ccTangentDir()) ;

        /*
        Average average = new Average();

        //average vertical translation
         vertTrans = average.ofAll(wheels, (Function<OdometryWheel, Double>) wheel ->
                // distance travelled vertically (w/o taking in account rotation)
                wheel.distanceTraveledTowardsAngle(
                        // subtract the amount spun due to rotation
                        wheel.getDeltaDistance()
                                - rotDelta * vertRotConst
//                                - rotDelta * GiveTheDefaultConfiguration.vertOffset.y,
                        ,facingForward
                )
        );


        //average horizontal translation
         horoTrans = average.ofAll(wheels, (Function<OdometryWheel, Double>) wheel ->
                // distance travelled vertically
                wheel.distanceTraveledTowardsAngle(
                        // subtract the amount spun due to rotation
                        wheel.getDeltaDistance()
                                - rotDelta * horoRotConst
//                                - rotDelta * GiveTheDefaultConfiguration.horoOffset.y
                        ,
                        facingRight
                )
        );

//        double horoTrans = average.ofAll(wheels, (Function<OdometryWheel, Double>) wheel ->
//                // distance travelled vertically
//                wheel.distanceTraveledTowardsAngle(
//                        // subtract the amount spun due to rotation
//                        wheel.getDeltaDistance()
//                                - wheel.dotProduct(
//                                // length of sector travelled
//                                wheel.distanceToCenter() * rotDelta,
//                                // direction of travel
//                                wheel.ccTangentDir(xCenterOfRotation, yCenterOfRotation)
//                        ),
//                        facingRight
//                )
//        );
            */
        return new pose(horoTrans, vertTrans, rotDelta);
    }


}
