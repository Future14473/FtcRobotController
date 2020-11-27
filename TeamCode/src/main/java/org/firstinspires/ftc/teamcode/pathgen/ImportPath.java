package org.firstinspires.ftc.teamcode.pathgen;

import org.firstinspires.ftc.teamcode.pathFollow.PathTXTs.Paths;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ImportPath {
    public static Path getPath (String path){

        String stringPath = Paths.getDefaultPath("default");
        Path ret = new Path();
        String[] lines = stringPath.split("\n");
        for (String line : lines){

            int count = 0;
            double x = 0;
            double y = 0;
            double speed = 0;
            double dir = 0;

            String[] pathValues = line.split(" ");
            for (String pathValue : pathValues){
                if (count == 0){
                     x = Integer.parseInt( pathValue);
                } else if (count == 1){
                     y = Integer.parseInt(pathValue);
                } else if ((count == 2) && (pathValue != null)){
                     dir = Integer.parseInt(pathValue); //todo check with xuyang to see if order is correct
                } else if ((count == 3) && (pathValue != null)) {
                     speed = Integer.parseInt(pathValue);
                }
                count++;
            }
            PathPoint toAdd = new PathPoint(x, y);
                toAdd.speed = speed;
                toAdd.dir = dir;
                ret.add(toAdd);
        }

        return ret;
//
//        PathPoint point0 = new PathPoint(0,0);
//        point0.speed = 0.3;
//        point0.dir = Math.PI/2;
//
//        ret.add(point0);
//        PathPoint point = new PathPoint(0,10);
//        point.speed = 0.3;
//        point.dir = 0;
//
//        PathPoint point2 = new PathPoint(20,10);
//        point2.speed = 0.3;
//        point2.dir = 0;
//
//        PathPoint point3 = new PathPoint(20,10);
//        point3.speed = 0.3;
//        point3.dir = Math.PI/2;
//
//        PathPoint point4 = new PathPoint(-10,-30);
//        point4.speed = 0.3;
//        point4.dir = Math.PI/2;

//        ret.add(point);
//        ret.add(point2);
//        ret.add(point3);
//        ret.add(point4);

//        return ret;


//        Path ret = new Path();
//        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                StringTokenizer t = new StringTokenizer(line, ",");
//                double x = Integer.parseInt(t.nextToken());
//                double y = Integer.parseInt(t.nextToken());
//                double speed = Integer.parseInt(t.nextToken());
//                double dir = Integer.parseInt(t.nextToken());
//
//                PathPoint toAdd = new PathPoint(x, y);
//                toAdd.speed = speed;
//                toAdd.dir = dir;
//
//                ret.add(toAdd);
//                return ret;
//            }
//        }catch (IOException e) {
//            System.out.printf("File %s not found%n", path);
//        }
//
//        return null;
    }
}
