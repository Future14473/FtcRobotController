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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp", group="Linear Opmode")
//@Disabled
public class TeleOp extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx intake;
    private DcMotorEx rightDrive;
    Mecanum_InProgress Motors = new Mecanum_InProgress();
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        Mecanum_InProgress Motors = new Mecanum_InProgress(hardwareMap);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            //DrivetrainBaseController
            double forward = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn  =  gamepad1.right_stick_x;
            Motors.move(forward, strafe, turn);
            //FastModeBaseController
            if(gamepad1.right_trigger == 2){
                Motors.frontRight.setVelocity(Motors.frontRightPower*10000);
                Motors.frontLeft.setVelocity(Motors.frontLeftPower*10000);
                Motors.backRight.setVelocity(Motors.backRightPower*10000);
                Motors.backLeft.setVelocity(Motors.backLeftPower*10000);
            }
            //SlowModeBaseController
            if(gamepad1.left_trigger == 2){
                Motors.frontRight.setVelocity(Motors.frontRightPower/25);
                Motors.frontLeft.setVelocity(Motors.frontLeftPower/25);
                Motors.backRight.setVelocity(Motors.backRightPower/25);
                Motors.backLeft.setVelocity(Motors.backLeftPower/25);
            }
            //IntakeAttachmentsController
            if(gamepad2.a){
                intake.setVelocity(100);
            }else{
                intake.setVelocity(0);
            }
            //

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)");
            telemetry.update();
        }
    }
}
