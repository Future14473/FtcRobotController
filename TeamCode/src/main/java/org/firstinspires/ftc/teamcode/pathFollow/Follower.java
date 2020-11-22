package org.firstinspires.ftc.teamcode.pathFollow;

import android.telecom.TelecomManager;

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
import org.firstinspires.ftc.teamcode.vuforia.VuMarkNav;

public class Follower {

    double closeEnoughDistance = 10;
    double closeEnoughAngle = Math.PI/6;
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
            pose position = odometry.getPosition();

            //move robot towards i
            PathPoint target = path.get(i);



            //diff in rotation
            double rotDiff = RotationUtil.turnLeftOrRight(position.r, target.dir, Math.PI * 2);

            //diff in translation
            point transDiff = new point(target.x - position.x, target.y - position.y);
            transDiff.scale(target.speed);

            //convert from field angles to robot intrinsic angles
            point transDiffIntrinsic = transDiff.rotate(position.r - Math.PI / 2);

            //actually move
            drivetrain.drive(transDiffIntrinsic.x / 90, transDiffIntrinsic.y / 90, rotDiff / 20);


            //advance
            double dist = target.distTo(new point(position.x, position.y));
            double angle = Math.abs(RotationUtil.turnLeftOrRight(position.r, target.dir, Math.PI / 2));

            if (dist < closeEnoughDistance && angle < closeEnoughAngle) {
                i++;
            }

//                telemetry.addData("driving velocity",
//                        transDiffIntrinsic.x + " " + transDiffIntrinsic.y + " " + rotDiff);
            telemetry.addData("Destination", String.format("%.1f %.1f %.1f", target.x, target.y, target.dir));
            telemetry.addData("Odometry Position", odometry.getPosition());
            telemetry.update();
        }

        drivetrain.drive(0, 0, 0);
        telemetry.addData("Done with path", "done");
        telemetry.update();
    });

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



