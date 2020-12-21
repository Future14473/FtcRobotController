package org.firstinspires.ftc.teamcode.odometry;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.utility.*;

import java.util.List;
import java.util.function.Function;

public class RotationOdometry extends Odometry {
    IMU imu;
    double prevAngle = 0;

    public RotationOdometry(pose initial, IMU imu, List<OdometryWheel> wheels) {
        super(initial, wheels);
        this.imu = imu;
    }

    protected void loop() {
        wheels.forEach(OdometryWheel::updateDelta);

        pose delta = getDeltaPose();

        pose curved = curvedTrajectoryTranslation(new pose(delta.x, delta.y, delta.r));
        // rotate the delta (x,y,r) by -90 degs
        point itrans = point.rotate(curved.x, curved.y, -Math.PI/2);
        curved.x = -itrans.x;
        curved.y = -itrans.y;

        position.translateRelative(curved);
    }

    public pose getDeltaPose(){
        double imuHeading = imu.getHeading();
        double rotDelta = RotationUtil.turnLeftOrRight(prevAngle, imuHeading, Math.PI/2);
        prevAngle = imuHeading;

        Average average = new Average();

        //average vertical translation
        double vertTrans = average.ofAll(wheels, (Function<OdometryWheel, Double>) wheel ->
                // distance travelled vertically (w/o taking in account rotation)
                wheel.distanceTraveledTowardsAngle(
                        // subtract the amount spun due to rotation
                        wheel.getDeltaDistance() - wheel.dotProduct(
                                // length of sector travelled
                                wheel.distanceToCenter() * rotDelta,
                                // direction of travel
                                wheel.ccTangentDir(xCenterOfRotation, yCenterOfRotation)
                        ),
                        facingForward
                )
        );


        //average horizontal translation
        double horoTrans = average.ofAll(wheels, (Function<OdometryWheel, Double>) wheel ->
                // distance travelled vertically
                wheel.distanceTraveledTowardsAngle(
                        // subtract the amount spun due to rotation
                        wheel.getDeltaDistance() - wheel.dotProduct(
                                // length of sector travelled
                                wheel.distanceToCenter() * rotDelta,
                                // direction of travel
                                wheel.ccTangentDir(xCenterOfRotation, yCenterOfRotation)
                        ),
                        facingRight
                )
        );

        return new pose(horoTrans, vertTrans, rotDelta);
    }


}
