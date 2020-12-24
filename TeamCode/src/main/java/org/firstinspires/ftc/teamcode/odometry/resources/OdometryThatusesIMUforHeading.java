package org.firstinspires.ftc.teamcode.odometry.resources;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.odometry.OdometryWheel;
import org.firstinspires.ftc.teamcode.utility.RotationUtil;
import org.firstinspires.ftc.teamcode.utility.point;
import org.firstinspires.ftc.teamcode.utility.pose;

import java.util.List;

public class OdometryThatusesIMUforHeading extends Odometry {

    Telemetry telemetry;

    // keep public so we can turn using only gyro
    public IMU imu;
    double prevAngle = 0;

    protected void loop() {
        wheels.forEach(OdometryWheel::updateDelta);

        pose delta = getDeltaPose();

        double imuHeading = imu.getHeading();
        delta.r = RotationUtil.turnLeftOrRight(prevAngle, imuHeading, Math.PI/2);
        prevAngle = imuHeading;

        pose curved = curvedTrajectoryTranslation(new pose(delta.x, delta.y, delta.r));
        point itrans = point.rotate(curved.x, curved.y, -Math.PI/2);
        curved.x = -itrans.x;
        curved.y = -itrans.y;

        position.translateRelative(curved);
    }

    public OdometryThatusesIMUforHeading(IMU imu, pose initial, List<OdometryWheel> wheels, Telemetry telemetry) {
        super(initial, wheels);
        this.imu = imu;
        this.telemetry = telemetry;
    }
}