package org.firstinspires.ftc.teamcode.pathgen;

public class ImportPath {
    static double centerOffset = 22.86;
    static double yTile = 60.96;
    static double xTile = -yTile; // negative because of the half of the field we are using

    public static PathPoint origin = new PathPoint(xTile,0 + centerOffset, 0);

    public static PathPoint test = new PathPoint(xTile * 1.5, centerOffset + (yTile * 0),0);
    public static PathPoint ringStack01 = new PathPoint(xTile * 1.15, centerOffset + (yTile * 1),0);
    public static PathPoint ringStack02 = new PathPoint(xTile * 1.35, centerOffset + (yTile * 1.2),0);
    public static PathPoint ringStack1 = new PathPoint(xTile * 1.5, centerOffset + (yTile * 2.2),0);

    public static PathPoint targetA0 = new PathPoint(xTile * 1.75, centerOffset + (yTile * 2.7),-Math.PI/4);
    public static PathPoint targetA1 = new PathPoint(xTile * 1.9, centerOffset + (yTile * 3.2),-Math.PI/3);
    public static PathPoint targetA = new PathPoint(xTile * 2, centerOffset + (yTile * 3.5),-Math.PI/2);
    public static PathPoint targetB = new PathPoint(xTile * 2,  centerOffset + yTile * 4.5,0);


    static Path path;

    public static Path getPath() {
        path = new Path();

        path.add(origin);
        path.add(ringStack1);


        return path;
    }

    // 1 ft to cm = 30.48cm
    // 2ft to 60.96
    public static PathPoint getOrigin(){
        return new PathPoint(xTile,0, 0);
    }

    public static PathPoint getRingStartPos(){
        return new PathPoint(xTile * 1.5, yTile * 2,0);
    }

    public static PathPoint getTargetA(){ return new PathPoint(xTile * 3, yTile * 3.5,0); }

    public static PathPoint getTargetB(){ return new PathPoint(xTile * 2, yTile * 4.5,0); }










//        String stringPath = Paths.getDefaultPath("default");
//        Path ret = new Path();
//        String[] lines = stringPath.split("\n");
//        for (String line : lines){
//
//            int count = 0;
//            double x = 0;
//            double y = 0;
//            double speed = 0;
//            double dir = 0;
//
//            String[] pathValues = line.split(" ");
//            for (String pathValue : pathValues){
//                if (count == 0){
//                     x = Integer.parseInt( pathValue);
//                } else if (count == 1){
//                     y = Integer.parseInt(pathValue);
//                } else if ((count == 2) && (pathValue != null)){
//                     dir = Integer.parseInt(pathValue); //todo check with xuyang to see if order is correct
//                } else if ((count == 3) && (pathValue != null)) {
//                     speed = Integer.parseInt(pathValue);
//                }
//                count++;
//            }
//            PathPoint toAdd = new PathPoint(x, y);
//                toAdd.speed = speed;
//                toAdd.dir = dir;
//                ret.add(toAdd);
//        }
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
