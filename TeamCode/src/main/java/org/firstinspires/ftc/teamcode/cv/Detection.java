package org.firstinspires.ftc.teamcode.cv;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.movement.Mecanum;
import org.firstinspires.ftc.teamcode.odometry.Odometry;
import org.firstinspires.ftc.teamcode.utility.pose;
import org.openftc.easyopencv.OpenCvPipeline;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import static org.firstinspires.ftc.teamcode.utility.Timing.delay;
import static org.opencv.imgproc.Imgproc.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Detection extends OpenCvPipeline {

    // Variables for rings
    public static final double DISKR = 0.2;
    public static final double DMAXR = DISKR * 1.7;
    public static final double DMINR = DISKR * 0.7;

    // Variables for wobble goal
    public static final double STICKR = 9;
    public static final double SMAXR = STICKR * 1.2;
    public static final double SMINR = STICKR * 0.5;

    // Variables for camera view -> Distance
    public static final double x0 = 20;
    public static final double z0 = 30;
    public static final int yP = 720;
    public static final int xP = 960;
    public static final double theta0 = Math.atan(z0/x0);
    public static final double viewAngle = Math.PI - 2* theta0;
    public static final double realX0 = 35;
    public static final double slope = 0.46;

    public static final long iterativeConst = 1;

    Point objPosition = new Point();

    ArrayList<double[]> wobbles = new ArrayList<>();
    int wobbleIterations = 10;

    Mat formatted = new Mat();
    Mat resized = new Mat();
    Mat recolored = new Mat();
    Mat threshold = new Mat();
    Mat subthreshold = new Mat();
    Mat submat = new Mat();
    Mat canvas = new Mat();
    int value = 0;
    int saturation = 0;

    Telemetry telemetry;
    Odometry odometry;
    Mecanum MecanumDrive;
    Mat output;

    public Detection(Telemetry t, Odometry odometry, Mecanum MecanumDrive){
        this.telemetry = t;
        this.odometry = odometry;
        this.MecanumDrive = MecanumDrive;
    }
    public Detection(Telemetry t, Mecanum MecanumDrive){
        this.telemetry = t;
        this.MecanumDrive = MecanumDrive;
    }
    public Detection(Telemetry t){
        this.telemetry = t;
    }

    @Override
    public Mat processFrame(Mat input){
        formatted = picSetup(input);
        if(wobbleIterations < 0){
            return input;
        }

        output = markWobble(input, find_wobble(formatted, "blue"));
//        wobbleIterations--;
        return output;
    }
//    public Mat makeSkinny(Mat input){
//        System.out.println("ree");
//    }

    // Setting up the picture to have a height of 960 pix and recoloring to BGR
    // Updates the avgValues
    public Mat picSetup(Mat input) {
        Mat recolored = new Mat();
        cvtColor(input, recolored, COLOR_RGB2BGR);
        resized = new Mat();
        double scale = 960.0/input.height();
        Imgproc.resize(recolored, resized, new Size(Math.round(input.width() * scale), Math.round(input.height() * scale)));
        recolored.release();
        avgValues(resized);
        return resized;
    }

    // Resizes image to height 960 pix
    public Mat picSetupNoFlip(Mat input) {
        resized = new Mat();
        double scale = 960.0/input.height();
        Imgproc.resize(input, resized, new Size(Math.round(input.width() * scale), Math.round(input.height() * scale)));
        recolored.release();
        return resized;
    }

    // Creates a new Mat object converted from BGR to HSV
    public Mat copyHSV(Mat input){
        cvtColor(input, recolored, COLOR_BGR2HSV);
        return recolored;
    }

    // Creates Arrays with every 15 pixels and sorts them to find the median
    // for HSV Value and Saturation
    public void avgValues(Mat input){
        Mat copy = copyHSV(input);
        int[] values = new int[(int)(Math.ceil(input.width()/15.0) * Math.ceil(input.height()/15.0))];
        int[] saturations = new int[(int)(Math.ceil(input.width()/15.0) * Math.ceil(input.height()/15.0))];
        int index = 0;
        for(int y = 0; y < input.height(); y+=15){
            for(int x = 0; x < input.width(); x+=15){
                values[index] = (int)copy.get(y, x)[2];
                saturations[index] = (int)copy.get(y, x)[1];
                index++;
            }
        }
        copy.release();
        Arrays.sort(values);
        Arrays.sort(saturations);
        int medVal = values[(int)(values.length/2)];
        int medSat = saturations[(int)(saturations.length/2)];

        // Equations derived by testing in different lighting
        // and using linear regression to go from median values to target
        value = Math.max((int)(-0.65*medVal + 165.42), 0);
        saturation = Math.max((int)(1.19*medSat + 38.78), 0);
    }

    public Mat find_yellows(Mat input){
        Mat copy = copyHSV(input);
        Core.inRange(copy, new Scalar(7, saturation, value), new Scalar(30, 255, 255), threshold);
        copy.release();
        return threshold;
    }

    public Mat find_blues(Mat input){
        Mat copy = copyHSV(input);
        Core.inRange(copy, new Scalar(110, saturation, value), new Scalar(125, 255, 255), threshold);
        copy.release();
        return threshold;
    }
    public Mat find_reds(Mat input){
        Mat copy = copyHSV(input);
        Core.inRange(copy, new Scalar(0, saturation, value), new Scalar(11, 255, 255), threshold);
        copy.release();
        return threshold;
    }

    /*
    Takes in roughly filtered contours and reprocesses
    them to sort out the rings and seperate ring stacks
    into individual rings
     */
    public ArrayList<Rect> find_subcontours(Mat input){
        subthreshold = find_yellows(input);
        Mat gray = new Mat();
        Mat grayblur = new Mat();
        Mat edgesX = new Mat();
        Mat edgesY = new Mat();
        Mat edges = new Mat();
        Mat sub = new Mat();
        Mat preFilter = input.clone();

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((int)(input.width()/15.0), 1));
        Mat kernelBig = Imgproc.getStructuringElement(CV_SHAPE_ELLIPSE, new Size(3, 3));

        ArrayList<Rect> output = new ArrayList<>();

        // Blurring a grayscale version of the input to create better edge detection results
        cvtColor(input, gray, COLOR_BGR2GRAY);
        Imgproc.blur(gray, grayblur, new Size(3,3));

        // Using different sobel derivatives and adding together
        Imgproc.Sobel(grayblur, edgesX, -1, 0, 1);
        Imgproc.Sobel(grayblur, edgesY, -1, 1, 0);
        Core.add(edgesX, edgesY, edges);

        // Dilating to seperate the stacked objects even more
        Imgproc.dilate(subthreshold, subthreshold, kernelBig);

        // Subtracting the edges from the thesholded image to make edges clearer
        Core.subtract(subthreshold, edges, sub);

        // Rethresholding to further accent the edges
        Core.inRange(sub, new Scalar(200, 200, 200), new Scalar(255, 255, 255), sub);

        // Eroding to remove small areas connecting seperated objects
        Imgproc.erode(sub, sub, kernel);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(sub, contours, new Mat(), Imgproc.CHAIN_APPROX_NONE, Imgproc.CHAIN_APPROX_SIMPLE);

        // Filtering out contours with wrong dimensions
        contours.removeIf(m -> {
            Rect rect = Imgproc.boundingRect(m);
            double r = (double) rect.height/rect.width;
            return ((r > DMAXR) || (r < DMINR) || (rect.width < input.width() * 0.7));
        });

        // Adding valid rings to the ouput
        for(int i = 0; i<contours.size(); i++){
            MatOfPoint contour = contours.get(i);
            Rect rect = Imgproc.boundingRect(contour);
            Imgproc.rectangle(input, rect.tl(), rect.br(), new Scalar(0, 255, 255), 2);
            output.add(rect);
        }

        gray.release();
        grayblur.release();
        edgesX.release();
        edgesY.release();
        edges.release();
        sub.release();
        preFilter.release();
        kernel.release();
        kernelBig.release();

        return output;
    }


    public ArrayList<Stack> find_rings(Mat input){
        threshold = find_yellows(input);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(threshold, contours, new Mat(), Imgproc.CHAIN_APPROX_NONE, Imgproc.CHAIN_APPROX_SIMPLE);

        //rough filtering
        contours.removeIf(m -> {
            Rect rect = Imgproc.boundingRect(m);
            return (rect.area() < 1500) || (rect.height > rect.width);
        });

        //Rings will be sorted into Stacks in rectsData
        ArrayList<Stack> rectsData= new ArrayList<>();

        //sorting "rings" into stacks
        for(MatOfPoint contour:contours){
            Rect rect = Imgproc.boundingRect(contour);

            // creating bounding boxes for the found contours with some breathing room
            int tempx = rect.x;
            int tempy = rect.y;
            rect.x = (int)Math.max(rect.x - (rect.width*0.1), 0);
            rect.y = (int)Math.max(rect.y - (rect.height*0.1), 0);
            rect.width = (int)Math.min((rect.width*1.1) + tempx, input.width()) - rect.x;
            rect.height = (int)Math.min((rect.height*1.1) + tempy, input.height()) - rect.y;

            submat = new Mat(input.clone(), rect);

            // Putting the submat through a more refined filtering system
            ArrayList<Rect> moreRects = find_subcontours(submat);
            for(Rect subrect: moreRects){
                double epsilonW = rect.width * 0.3;
                double epsilonH = rect.height * 0.3;
                subrect.x += rect.x;
                subrect.y += rect.y;
                int index = Stack.closeIn(rectsData, subrect, epsilonW, epsilonH);
                if(index == -2){
                    System.out.println("Overlap");
                }
                else if(index == -1){
                    rectsData.add(new Stack(subrect));
                }
                else{
                    rectsData.get(index).addExisting(subrect);
                }
            }
        }
        //labeling found stacks
        threshold.release();
        return rectsData;
    }

    public Boolean wobble_stick(Mat input, MatOfPoint contour){
        Rect rect = Imgproc.boundingRect(contour);
        int newX = rect.x;
        int newY = (int)Math.max(rect.y - (rect.height*0.1), 0);
        int newW = rect.width;
        int newH = (int)Math.min((rect.height * 0.95) + newY, input.height()) - newY;
        submat = new Mat(input.clone(), new Rect(newX, newY, newW, newH));

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(submat, contours, new Mat(), Imgproc.CHAIN_APPROX_NONE, Imgproc.CHAIN_APPROX_SIMPLE);

        //initial filtering
        contours.removeIf(m -> {
            double height2 = Math.pow(Imgproc.boundingRect(m).height, 2);
            double area = contourArea(m);
            return ((area < 1000) || (area < height2/SMAXR) || (area > height2/SMINR));
        });

        return (contours.size() > 0);
    }

    public Rect find_wobble(Mat input, String side){
        List<MatOfPoint> contours = new ArrayList<>();

        // Choosing to use red or blue thresholding
        if(side.equals("blue")){
            threshold = find_blues(input);
        }
        else if(side.equals("red")){
            threshold = find_reds(input);
        }
        else{
            System.out.println("ERROR: Not a valid side.");
        }

        Mat kernel = Imgproc.getStructuringElement(CV_SHAPE_ELLIPSE, new Size(20, 20));
        Mat kernelE = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((int)(input.width()/250.0), 1));

        // Dilating and eroding to produce smoother results with less noise
        Imgproc.dilate(threshold, threshold, kernel);
        Imgproc.erode(threshold, threshold, kernelE);
        Imgproc.findContours(threshold, contours, new Mat(), Imgproc.CHAIN_APPROX_NONE, Imgproc.CHAIN_APPROX_SIMPLE);

        // rough filtering
        contours.removeIf(m -> {
            Rect rect = Imgproc.boundingRect(m);
            double r = (double) rect.height/rect.width;
            return ((rect.area() < 1000) || (rect.width > rect.height)) || !wobble_stick(threshold, m);
        });

        MatOfPoint max = new MatOfPoint();
        double area = -1;
        for(int i = 0; i<contours.size();i++){
            MatOfPoint contour = contours.get(i);
            if(contourArea(contour) > area){
                area = contourArea(contour);
                max = contour;
            }
        }
        Rect maxRect = Imgproc.boundingRect(max);
        Rect finalRect = new Rect(maxRect.x, maxRect.y, maxRect.width, (int)Math.round(maxRect.height*(28.0/24)));
        addWobble(finalRect);
        kernel.release();
        kernelE.release();
        return finalRect;
    }

    public double find_Angle(Rect obj){
        double centerX = obj.x + (double)obj.width/2;
        double centerY = obj.y + obj.height;
        double y = pix2Y(centerY);
        double x = pix2RealX(centerX, centerY);
        double angle = Math.atan(y/x) - Math.PI/2;
        if(y/x < 0){
            angle += Math.PI;
        }
        return Math.toDegrees(angle);
    }

    public double pix2Y(double pixY){
        double angle = (yP - pixY)/yP * viewAngle;
        return z0 * Math.tan(theta0 + angle) + x0;
    }

    public double pix2RealX(double pixX, double pixY){
        double y = pix2Y(pixY);
        double fullX = realX0 + y*slope;
        return (pixX - xP/2.0)/(xP) * fullX;
    }

    public Point find_Point(Rect obj){
        double centerX = obj.x + (double)obj.width/2;
        double centerY = obj.y + obj.height;
        double y = pix2Y(centerY);
        double x = pix2RealX(centerX, centerY);
        return new Point(x, y);
    }


    public Mat markRings(Mat input, ArrayList<Stack> rectsData){
        canvas = input.clone();
        for(Stack stack: rectsData){
            if(stack.count > 0){
                double Angle = find_Angle(stack.fullStack);
                Imgproc.rectangle(canvas, stack.fullStack.tl(), stack.fullStack.br(), new Scalar(255, 255, 0), 2);
                Imgproc.putText(canvas, "" + stack.count,
                        new Point(stack.fullStack.x + 20, stack.fullStack.y - 50),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
                //Imgproc.putText(copy, "Angle: " + (int)Angle, new Point(data[1] + data[2]/2, data[4] + 100),
                // Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);

            }
        }
        return canvas;
    }

    public Point CountRingsToLocation(ArrayList<Stack> rectsData){
        int maxCount = Collections.max(rectsData).count;
        if(maxCount == 0){
            return new Point();
            // go to 0 ring location
        }
        else if(maxCount == 1){
            return new Point();
            // go to 1 ring location
        }
        else{
            return new Point();
            // go to 4 ring location
        }
    }

    public Mat markWobble(Mat input, Rect rect){
        //labeling found wobble
        canvas = input.clone();
        double Angle = find_Angle(rect);
        Imgproc.rectangle(canvas, rect.tl(), rect.br(), new Scalar(0, 255, 255), 2);
        Imgproc.putText(canvas, "Wobble Goal", new Point(rect.x, rect.y -100), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 255, 0), 2);
        Imgproc.putText(canvas, "Angle: " + (int)Angle, new Point(rect.x, rect.y -50 ), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);
        return canvas;
    }

    public Point locateObj(Rect rect){
        Point rel = find_Point(rect);
        int dx = (int)rel.x;
        int dy = (int)rel.y;
        pose curr = odometry.getPosition();
        int newX = (int)(curr.x + dx*Math.cos(curr.r + Math.PI/2));
        int newY = (int)(curr.y + dy*Math.sin(curr.r + Math.PI/2));
        return new Point(newX, newY);
    }


    public Point bestWobble(){
        if(wobbles.isEmpty()){
            return new Point(0, 0);
        }
        int maxWobblesI = 0;
        double maxWobbles = 0;
        for(int i = 0; i<wobbles.size(); i++){
            double[] wobble = wobbles.get(i);
            if(wobble[0] > maxWobbles){
                maxWobbles = wobble[0];
                maxWobblesI = i;
            }
        }

        double[] maxWobbleFinal = wobbles.get(maxWobblesI);
        wobbles.clear();
        telemetry.addData("Best Wobble: ", new Point(maxWobbleFinal[1], maxWobbleFinal[2]));
        telemetry.update();

        return new Point(maxWobbleFinal[1], maxWobbleFinal[2]);
    }

    public void addWobble(Rect rect){
        Point rel = find_Point(rect);
        boolean match = false;
        for(int i = 0; i<wobbles.size(); i++){
            double[] wobble = wobbles.get(i);
            double dist = Math.hypot(rel.x - wobble[1], rel.y - wobble[2]);
            if(dist < 150){
                wobble[1] = (wobble[0] * wobble[1] + rel.x)/(wobble[0] + 1);
                wobble[2] = (wobble[0] * wobble[2] + rel.y)/(wobble[0] + 1);
                wobble[0]++;
                match = true;
                break;
            }
        }
        if(!match){
            double[] added = {1, rel.x, rel.y};
            wobbles.add(added);
        }
    }

    public Point bestRing(ArrayList<double[]> rectsData){
        return new Point();
    }

    public int faceObjectInc(Point target){
        int dx = ((int)target.x - yP/2);
        double speed = 0;
        if(dx > 20){
            speed = 0.2;
        }
        else if(dx < -20){
            speed = -0.2;
        }
        else{
            dx = 0;
        }
        MecanumDrive.drive(0, 0, speed);
        //delay(dx * iterativeConst);
        delay(50);
        MecanumDrive.drive(0, 0, 0);
        return dx;
    }

    public double jankAngle(Point target){
        return Math.toRadians(target.x/960*70);
    }

}
