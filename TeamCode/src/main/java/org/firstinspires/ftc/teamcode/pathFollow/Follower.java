package org.firstinspires.ftc.teamcode.pathFollow;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GivesPosition;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.pathgen.ImportPath;
import org.firstinspires.ftc.teamcode.pathgen.Path;
import org.firstinspires.ftc.teamcode.pathgen.PathPoint;
import org.firstinspires.ftc.teamcode.utility.RotationUtil;
import org.firstinspires.ftc.teamcode.utility.point;
import org.firstinspires.ftc.teamcode.utility.pose;

public class Follower {
    public PathPoint targetPose;
    volatile boolean running = true;

    // constructor stuff
    Mecanum drivetrain;
    Telemetry telemetry;
    GivesPosition odometry;
    String pathFile;
    Thread loop = new Thread(() -> {

        Path path = ImportPath.getPath(pathFile);

        //index of current target point
        int i = 0;
        while (i < path.size() && running) {

            // get target position
            PathPoint target = path.get(i);

            // move to target
            boolean reached = goTowards(target);

            //advance point
            if (reached)
                i++;

            telemetry.addData("Destination", String.format("%.1f %.1f %.1f", target.x, target.y, target.dir));
            telemetry.addData("Odometry Position", odometry.getPosition());
            telemetry.update();
        }

        drivetrain.drive(0, 0, 0);

        telemetry.addData("Odometry Position", odometry.getPosition());
        telemetry.addData("Done with path", "done");
        telemetry.update();
    });

    //return true if dest reached
    boolean goTowards(PathPoint dest){
        pose position = odometry.getPosition();

        pose diff = new pose(dest.x-position.x, dest.y-position.y,
                RotationUtil.turnLeftOrRight(position.r, dest.dir, Math.PI * 2));

        // to intrinsic
        point intrinsic = new point(diff.x, diff.y).rotate(-diff.r);
        diff.x = intrinsic.x;
        diff.y = intrinsic.y;

        // To consider:
        // 1) speeds below 0.1 cannot overcome static friction of drivetrain
        //    Therefore, all speeds below 0.1 will be rounded up to 0.1
        // 2) because of (1), robot will jerk when it gets near a point
        //    So stop moving when close enough

        double xVel = (Math.abs(diff.x)>1)?     (diff.x/20 + 0.1 * Math.signum(diff.x)):0;
        double yVel = (Math.abs(diff.y)>1)?     (diff.y/20 + 0.1 * Math.signum(diff.y)):0;
        double rVel = (Math.abs(diff.r)>0.05)?  (diff.r    + 0.1 * Math.signum(diff.r)):0;

        drivetrain.drive(xVel, yVel, rVel);

        telemetry.addData("intention", diff);

        // return true if reached point
        return (xVel == 0 && yVel == 0 && rVel == 0);
    }

    /**
     * Follows the pathFile found in local directory
     * @param drivetrain a mecanum drivetrain
     * @param odometry an activated, running odometry to be used for positioning
     */
    public Follower(Mecanum drivetrain, Odometry odometry, Telemetry telemetry){
        this(drivetrain, odometry, "paths.txt", telemetry);
    }
    /**
     * Follows a path specified by the fileName
     * @param drivetrain a mecanum drivetrain
     * @param odometry an activated, running odometry to be used for positioning
     */
    public Follower(Mecanum drivetrain, GivesPosition odometry, String pathFile, Telemetry telemetry) {
        this.drivetrain = drivetrain;
        this.odometry = odometry;
        this.telemetry = telemetry;
        this.pathFile = pathFile;
    }

    public void start(){
        running = true;
        loop.start();
    }

    public void stop(){
        running = false;
    }

}



