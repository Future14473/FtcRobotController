package org.firstinspires.ftc.teamcode.odometry;

import java.util.ArrayList;

public class Odometry2DRot {
        // Declare class Instances
        double radiansAngle;
        double wheelDeltaX;
        double wheelDeltaY;
        double previousCoordinateX = 0;
        double previousCoordinateY = 0;
        double radiusWheel1 = 1;
        double radiusWheel2 = 1;

        public ArrayList getPositionReset(){
            // Get local coordinates
            double deltaX = getDeltaX();
            double localCoordinateX = getLocalCoordinate(previousCoordinateX, deltaX);

            double deltaY = getDeltaY();
            double localCoordinateY = getLocalCoordinate(previousCoordinateY, deltaY);

            // Add global coordinates to a list
            double globalX = getGlobalX(localCoordinateX, localCoordinateY);
            double globalY = getGlobalY(localCoordinateX, localCoordinateY);

            // Reset the previousCoordinates to now be the current globalCoordiantes
            getNewPreviousCoordinateX(globalX);
            getNewPreviousCoordinateY(globalY);

            ArrayList globalCoordinates = new ArrayList();
            globalCoordinates.add(globalX);
            globalCoordinates.add(globalY);
            return globalCoordinates;
        }

        // Getter functions
        public double getRadiansAngle(){
            return radiansAngle;
        }

        public double getNewRadiansAngle(double newRadiansAngle){
            radiansAngle = newRadiansAngle;
            return radiansAngle;
        }

        public double getWheelDeltaX(){
            return wheelDeltaX;
        }

        public double getNewWheelDeltaX(double newWheelDeltaX){
            wheelDeltaX = newWheelDeltaX;
            return wheelDeltaX;
        }

        public double getWheelDeltaY(){
            return wheelDeltaY;
        }

        public double getNewWheelDeltaY(double newWheelDeltaY) {
            wheelDeltaY = newWheelDeltaY;
            return wheelDeltaY;
        }

        public double getPreviousCoordinateX(){
            return previousCoordinateX;
        }

        public double getNewPreviousCoordinateX(double newPreviousCoordinateX) {
            previousCoordinateX = newPreviousCoordinateX;
            return previousCoordinateX;
        }

        public double getPreviousCoordinateY(){
            return previousCoordinateY;
        }

        public double getNewPreviousCoordinateY(double newPreviousCoordinateY) {
            previousCoordinateY = newPreviousCoordinateY;
            return previousCoordinateY;
        }


        // Local(intrinsic) Wheel Displacement
        public double getDeltaX(){
            double arcDelta = Math.toRadians(radiansAngle) * radiusWheel1;
            double deltaX = wheelDeltaX - arcDelta;
            return deltaX;
        }

        public double getDeltaY(){
            double arcDelta = Math.toRadians(radiansAngle) * radiusWheel2;
            double deltaY = wheelDeltaY - arcDelta;
            return deltaY;
        }

        // Add local wheel displacement to previous coordinate to get local coordinate
        public double getLocalCoordinate(double previousCoordinate, double delta){
            double localCoordinate = previousCoordinate + delta;
            return localCoordinate;
        }

        // Change the localCoordinate into a global Coordinate by rotating the 2D point by theta
        // Formulas derived from rotating a 2D point formula about arbitrary point
        public double getGlobalX(double localCoordinateX, double localCoordinateY){
            double globalX = ((localCoordinateX - previousCoordinateX) * Math.cos(radiansAngle)) -
                    ((localCoordinateY - previousCoordinateY) * Math.sin(radiansAngle)) + previousCoordinateX;
            return globalX;
        }

        public double getGlobalY(double localCoordinateX, double localCoordinateY) {
            double globalY = ((localCoordinateX - previousCoordinateX) * Math.sin(radiansAngle)) +
                    ((localCoordinateY - previousCoordinateY) * Math.cos(radiansAngle)) + previousCoordinateY;
            return globalY;
        }





    }


