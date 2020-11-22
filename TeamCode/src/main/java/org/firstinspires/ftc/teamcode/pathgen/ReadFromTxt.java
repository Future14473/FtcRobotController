//package org.firstinspires.ftc.teamcode.pathgen;
//
//import android.os.Environment;
//import android.util.Log;
//import android.widget.TextView;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//
//public class ReadFromTxt {
//
//    StringBuilder text = new StringBuilder();
////    try {
//        File sdcard = Environment.getExternalStorageDirectory();
//        File file = new File(sdcard,"testFile.txt");
//
//        BufferedReader br = new BufferedReader(new FileReader(file));
//
//        while ((String line = br.readLine()) != null) {
//            text.append(line);
//            Log.i("Test", "text : "+text+" : end");
//            text.append('\n');
//        }
//
//    public ReadFromTxt() throws FileNotFoundException {
//    }
//}
////    catch (IOException e) {
////        e.printStackTrace();
////
////    }
//    finally{
//        br.close();
//    }
//    TextView tv = (TextView)findViewById(R.id.amount);
//
//    tv.setText(text.toString()); ////Set the text to text view.
//}
//
