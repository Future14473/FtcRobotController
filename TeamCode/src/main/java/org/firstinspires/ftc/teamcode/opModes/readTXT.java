package org.firstinspires.ftc.teamcode.opModes;

import android.os.Environment;
import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.pathgen.ImportPath;
import org.firstinspires.ftc.teamcode.pathgen.Path;
import org.firstinspires.ftc.teamcode.utility.ReadFromFile;

import java.io.File;
import java.io.IOException;

// trying to get it to work, actually makes more sense to do it in java, read from file
// is very finicky with getting the path
@TeleOp(name="Read TXT", group="Autonomous")
//@Disabled
public class readTXT extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        /* how to write a file, but can be done manually using MTP(media transfer protocol)
        String filename = "KyleIsTired.txt";
        File file = AppUtil.getInstance().getSettingsFile(filename);
        String absolutePath = file.getPath();
        ReadWriteFile.writeFile(file, "Kyle Is very tired today, I am also eating cake");

        String filename = "paths2.txt";
        File file = AppUtil.getInstance().getSettingsFile(filename);


        String output = ReadWriteFile.readFile(file);
        Log.d("Output value", output);
        */

        Path path = ImportPath.getPath("default");
        Log.d("path", String.valueOf(path));
        waitForStart();
        while (opModeIsActive()){

        }
    }
}
