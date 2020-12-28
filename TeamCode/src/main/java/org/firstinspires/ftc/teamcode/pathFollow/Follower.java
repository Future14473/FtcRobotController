package org.firstinspires.ftc.teamcode.pathFollow;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GivesPosition;
import org.firstinspires.ftc.teamcode.imu.IMU;
import org.firstinspires.ftc.teamcode.movement.Mecanum;

import org.firstinspires.ftc.teamcode.pathgen.ImportPath;
import org.firstinspires.ftc.teamcode.pathgen.Path;
import org.firstinspires.ftc.teamcode.pathgen.PathPoint;
import org.firstinspires.ftc.teamcode.utility.RotationUtil;
import org.firstinspires.ftc.teamcode.utility.point;
import org.firstinspires.ftc.teamcode.utility.pose;

public class Follower {
    public PathPoint target = new PathPoint(0,0,0);
    volatile boolean running = true;

    // constructor stuff
    LinearOpMode opmode;
    Mecanum drivetrain;
    Telemetry telemetry;
    GivesPosition odometry;
    String pathFile;
    IMU followerIMU;
    Thread loop = new Thread(() -> {

        Path path = ImportPath.getPath();

        //index of current target point
        int i = 0;
        while (i < path.size() && running) {

            // get target position
            target = path.get(i);


            // move to target
            boolean reached = isArrived(target);

            //advance point
            if (reached)
                i++;

            Log.d("Destination", String.format("%.1f %.1f %.1f", target.x, target.y, target.dir));
            Log.d("Odometry Position", odometry.getPosition().toString());
            telemetry.addData("Destination", String.format("%.1f %.1f %.1f", target.x, target.y, target.dir));
            telemetry.addData("Odometry Position", odometry.getPosition());
            telemetry.update();
        }

        drivetrain.drive(0, 0, 0);

        telemetry.addData("Odometry Position", odometry.getPosition());
        telemetry.addData("Done with path", "done");
        stop();// keeps doing random things after done with path
        telemetry.update();
    });

    public void goTo(PathPoint dest){
        while (!isArrived(dest) && opmode.opModeIsActive()){
            Log.e("Odometry Position: ", odometry.getPosition().toString());
            Log.e("Destiny: ", String.format("%.1f %.1f %.1f", dest.x, dest.y, dest.dir));
            // keep going to the point
        }
        Log.e("Done with PAth: ", "true");
        Log.e("______________________________________________________ ", "______________________________________________________");
        telemetry.addData("Done with PAth: ", "true");
    }
    //return true if dest reached
    boolean isArrived(PathPoint dest){

        pose position = odometry.getPosition();

        pose diff = new pose(dest.x-position.x, dest.y-position.y,
                RotationUtil.turnLeftOrRight(position.r, dest.dir, Math.PI * 2));

        // to intrinsic
        point intrinsic = new point(diff.x, diff.y).rotate(-position.r);
        diff.x = intrinsic.x;
        diff.y = intrinsic.y;

        Log.e("diff (intrinsic): ", String.format("%.1f %.1f %.1f",diff.x,diff.y,diff.r));

        // To consider:
        // 1) speeds below 0.1 cannot overcome static friction of drivetrain
        //    Therefore, all speeds below 0.1 will be rounded up to 0.1
        // 2) because of (1), robot will jerk when it gets near a point
        //    So stop moving when close enough

        double xVel = Math.abs(diff.x)<2    ? 0 : Math.max(Math.abs(diff.x)/200, 0.1) * Math.signum(diff.x);
        double yVel = Math.abs(diff.y)<2    ? 0 : Math.max(Math.abs(diff.y)/200, 0.1) * Math.signum(diff.y);
        double rVel = Math.abs(diff.r)<0.05 ? 0 : Math.max(Math.abs(diff.r), 0.1) * Math.signum(diff.r);

        // if turning only use the gyro to increase accuracy
//        if (dest.x == 0 && dest.y == 0){
//            double rDirection = RotationUtil.turnLeftOrRight(followerIMU.getHeading(), dest.dir, Math.PI * 2) / 6;
//            drivetrain.drive(0,0,(rVel > 0.5 && Math.abs(rDirection) > 0.08)? rDirection:0);
//            return (rVel == 0);
//        }

        // because we're doing big motion, the robot tends to overshoot
        drivetrain.drive(xVel, yVel, rVel);

        telemetry.addData("To Point Amount", diff);

        // return true if reached point
        return (xVel == 0 && yVel == 0 && rVel == 0);
    }

    /**
     * Follows the pathFile found in local directory
     * @param drivetrain a mecanum drivetrain
     * @param odometry an activated, running odometry to be used for positioning
     */
    // unused
//    public Follower(Mecanum drivetrain, org.firstinspires.ftc.teamcode.odometry.Odometry odometry, Telemetry telemetry){
//        this(drivetrain, odometry, "paths.txt", telemetry);
//    }
    /**
     * Follows a path specified by the fileName
     * @param drivetrain a mecanum drivetrain
     * @param odometry an activated, running odometry to be used for positioning
     */
    public Follower(Mecanum drivetrain, GivesPosition odometry, IMU imu, Telemetry telemetry) {
        this.drivetrain = drivetrain;
        this.odometry = odometry;
        this.telemetry = telemetry;
        followerIMU = imu;
    }

    public Follower(Mecanum drivetrain, GivesPosition odometry, IMU imu, Telemetry telemetry, LinearOpMode opMode) {
        this.drivetrain = drivetrain;
        this.odometry = odometry;
        this.telemetry = telemetry;
        this.opmode = opMode;
        followerIMU = imu;
    }

    public void start(){
        running = true;
        loop.start();
    }

    public void stop(){
        running = false;
    }

}



