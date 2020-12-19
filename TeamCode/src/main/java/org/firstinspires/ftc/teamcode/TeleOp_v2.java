/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="TeleOp-v2", group="Linear Opmode")


 public class TeleOp_v2 extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx intake;
    private DcMotorEx taco;
    private DcMotorEx shooter;
    private  DcMotorEx shooter_adjust;
    private Servo wobble;
    private Servo wobble_arm;
    private CRServo shooter_roller;
    private boolean taco_rollerIsRunning = true;
    private boolean intakeIsRunning = true;
    private boolean reverseIntakeIsRunning = true;
    private boolean shooterIsRunning = true;
    private boolean wobbleIsRunning = true;
    private boolean wobbleArmIsRunning = true;

    Mecanum Motors;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        intake  = hardwareMap.get(DcMotorEx.class, "intake");
        taco = hardwareMap.get(DcMotorEx.class, "taco");
        shooter  = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter_adjust  = hardwareMap.get(DcMotorEx.class, "shooter_adjuster");
        wobble = hardwareMap.get(Servo.class, "wobble");
        wobble_arm = hardwareMap.get(Servo.class, "wobble_arm");
        shooter_roller = hardwareMap.get(CRServo.class, "shooter_roller");
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            Mecanum Motors = new Mecanum(hardwareMap);
            double forward = gamepad1.right_stick_y;
            double strafe = gamepad1.right_stick_x;
            double turn = gamepad1.left_stick_x;
            Motors.drive(strafe/2, forward/2, turn/2);

            if(gamepad1.right_bumper){
                Motors.backRight.setVelocity(Motors.backRight.getVelocity()*10000);
                Motors.frontLeft.setVelocity(Motors.frontLeft.getVelocity()*10000);
                Motors.frontRight.setVelocity(Motors.frontRight.getVelocity()*10000);
                Motors.backLeft.setVelocity(Motors.backLeft.getVelocity()*10000);
            }

            if(gamepad1.left_bumper){
                Motors.backRight.setVelocity(Motors.backRight.getVelocity()/10);
                Motors.frontLeft.setVelocity(Motors.frontLeft.getVelocity()/10);
                Motors.frontRight.setVelocity(Motors.frontRight.getVelocity()/10);
                Motors.backLeft.setVelocity(Motors.backLeft.getVelocity()/10);
            }



            if (gamepad2.right_trigger == 1&&taco_rollerIsRunning){
                    taco.setVelocity(10000);
                shooter_roller.setPower(100);
                    taco_rollerIsRunning = false;

            }
            if (gamepad2.right_trigger == 1 &&!taco_rollerIsRunning){
                    taco.setVelocity(0);
                    shooter_roller.setPower(0);
                taco_rollerIsRunning = true;
            }



            if (gamepad2.x&& shooterIsRunning){
                    shooter.setVelocity(10000);
                    shooterIsRunning = false;

            }
            if (gamepad2.x&&!shooterIsRunning){
                    shooter.setVelocity(0);
                    shooterIsRunning = true;
            }


            if (gamepad2.y&& wobbleIsRunning){
                    wobble.setPosition(1);
                    wobbleIsRunning = false;

            }
            if (gamepad2.y&&!wobbleIsRunning){
                    wobble.setPosition(0);
                    wobbleIsRunning = true;
            }


            if (gamepad2.left_trigger == 1&&intakeIsRunning){
                    intake.setVelocity(1000);
                    intakeIsRunning = false;

            }
            if (gamepad2.left_trigger == 1&&!intakeIsRunning){
                    intake.setVelocity(0);
                    intakeIsRunning = true;
            }



            if (gamepad2.left_bumper&&reverseIntakeIsRunning){
                    intake.setVelocity(-1000);
                    reverseIntakeIsRunning = false;

            }
            if (gamepad2.left_bumper&&!reverseIntakeIsRunning){
                    intake.setVelocity(0);
                    reverseIntakeIsRunning = true;
            }

            double angle = -gamepad2.left_stick_y;
            shooter_adjust.setVelocity(angle);




            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
