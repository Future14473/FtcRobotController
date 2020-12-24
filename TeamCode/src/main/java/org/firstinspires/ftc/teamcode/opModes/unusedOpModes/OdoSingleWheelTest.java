package org.firstinspires.ftc.teamcode.opModes.unusedOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.odometry.FreeSpinOdoWheel;
import org.firstinspires.ftc.teamcode.utility.pose;

import java.util.ArrayList;


@TeleOp(name="OdoSingleTest", group="Iterative Opmode")
@Disabled
public class OdoSingleWheelTest extends OpMode
{
    DcMotor wheel;
    FreeSpinOdoWheel odowheel;

    DcMotor wheel2;
    FreeSpinOdoWheel odowheel2;

    int cyclesSinceLastUpdate = 0;
    ArrayList<Integer> recordCyclesToUpdate = new ArrayList();

    @Override
    public void init() {
        wheel = hardwareMap.get(DcMotor.class, "shooter");
        wheel2 = hardwareMap.get(DcMotor.class, "shooter_adjuster");
        odowheel = new FreeSpinOdoWheel(new pose(0,0,0), wheel);
        odowheel2 = new FreeSpinOdoWheel(new pose(0,0,0), wheel2);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }


    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        wheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        wheel2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        MotorConfigurationType type = wheel.getMotorType();

        double prevDist = wheel.getCurrentPosition();

        odowheel.updateDelta();
        odowheel.getDeltaDistance();
        odowheel2.updateDelta();
        odowheel2.getDeltaDistance();

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "WheelHoriz Position: " + String.format("%d", wheel.getCurrentPosition()));
        telemetry.addData("Status", "WheelHoriz Distance: " + String.format("%.1f", odowheel.totalDistTravelled));

        telemetry.addData("Status", "WheelVert Position: " + String.format("%d", wheel2.getCurrentPosition()));
        telemetry.addData("Status", "WheelVert Distance: " + String.format("%.1f", odowheel2.totalDistTravelled));

        /*//did it update?
        if(wheel.getCurrentPosition() == prevDist){//no
            cyclesSinceLastUpdate ++;
        }else{//yes
            recordCyclesToUpdate.add(cyclesSinceLastUpdate);
            if(recordCyclesToUpdate.size()>1000){
                recordCyclesToUpdate.remove(0);
            }
            cyclesSinceLastUpdate=0;
        }

        //average
        double sum = 0;
        for(int c:recordCyclesToUpdate){
            sum+=c;
        }
        telemetry.addData("cycles taken to update", sum/recordCyclesToUpdate.size());*/
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        telemetry.addData("Status", "done! Great job!");
    }

}


