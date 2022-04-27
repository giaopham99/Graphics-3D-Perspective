package com.csci4810;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Class to perform 3-Dimensional Transformations
 */
public class CustomGraphic3D extends CustomGraphic{
    protected List<double[]> eyeCoordinateSystem = new LinkedList<>();
    protected List<double[]> dataListProjected = new LinkedList<>(); //list containing points after perspective projection
    protected double distance;
    protected double[] viewpointCoordinate;
    protected double screenWidth;
    protected double screenHeight;
    protected double viewportWidth;
    protected double viewportHeight;

    /**
     * Default Constructor extends from CustomGraphic class and adds the 3-Dimensional Perspective
     */
    CustomGraphic3D(){
        super();
        //in centimeters
        distance = 60;
        viewpointCoordinate = new double[]{6, 8, 7.5};
        screenWidth = 30;
        screenHeight = 30;
        viewportWidth = WIDTH;
        viewportHeight = HEIGHT;
    }

    /**
     * Helper method to multiply 1x34 matrix with 4x4 matrix
     * @param matrix1 1x4 matrix containing the point (x,y,z,1)
     * @param matrix2 4x4 matrix containing the transformation matrix
     * @return resulting coordinate point
     */
    protected double[] multiplyMatrix3D(double[] matrix1, double[][] matrix2){
        double[] result = new double[4];
        double sum = 0;
        //Column for matrix2
        for(int col=0; col<matrix2.length;col++){ //in terms of matrix2
            sum = 0;
            //Row for matrix2
            for (int row=0; row<matrix2.length;row++){ //in terms of matrix2
                sum += matrix1[row]*matrix2[col][row];
            }
            result[col] = sum;
        }
        return result;
    }

    /**
     * Helper method to multiply 4x4 matrix with 4x4 matrix
     * @param matrix1 first 4x4 matrix
     * @param matrix2 second 4x4 matrix
     * @return resulting 4x4 matrix
     */
    protected double[][] multiplyMatrix3D(double[][] matrix1, double[][] matrix2){
        double[][] result = new double[4][4];
        //Column for matrix2
        for(int col=0; col<matrix2.length;col++){ //in terms of matrix2
            //Row for matrix2
            for (int row=0; row<matrix2.length;row++){ //in terms of matrix2
                double sum = 0;
                //Matrix multiplication
                sum+= matrix1[0][row] * matrix2[col][0];
                sum+= matrix1[1][row] * matrix2[col][1];
                sum+= matrix1[2][row] * matrix2[col][2];
                sum+= matrix1[3][row] * matrix2[col][3];
                result[col][row] = sum;
            }
        }
        return result;
    }

    /**
     * Set Method to determine the 3D viewing angles and parameters of the image.
     * @param distance distance from the viewpoint to the screen in centimeters
     * @param viewpoint (x,y,z) coordinate of the point the image is observed from
     */
    public void set3DPositions(double distance, String[] viewpoint){
        double[] values = new double[4];
        for(int i=0; i<viewpoint.length; i++)
            values[i] = Double.valueOf(viewpoint[i]); //format strings into ints

        this.distance = distance;
        this.viewpointCoordinate = values;
    }

    /**
     * Method to read coordinates of a line in an external file to the LinkedList of data.
     * @param fileName name of the external file
     * @return number of lines read
     */
    public int inputLines3D(String fileName){
        String filePath = "./DataFiles/" + fileName;
        dataList.clear();
        //Starter code given by https://www.w3schools.com/java/java_files_read.asp
        try{
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String[]  data = reader.nextLine().split(" ",6); //Splits each line into 4 parts
                double[] lines = new double[6];
                for(int i=0; i<data.length; i++)
                    lines[i] = Double.valueOf(data[i]); //format strings into ints
                dataList.add(lines);
            }
        }catch(FileNotFoundException e){
            System.out.println("Error: File was not found.");
            e.printStackTrace();
        }
        return dataList.size();
    }

    /**
     * Method to apply a transformation matrix to the dataList
     * @param matrix 4x4 transformation matrix
     */
    public void applyTransformation3D(double[][] matrix){
        double x0,y0,z0,x1,y1,z1;
        //Loop through list of lines
        for(int i=0; i< dataList.size();i++){
            double[] result;
            double[] newLine = new double[6];
            //First point
            x0 = dataList.get(i)[0];
            y0 = dataList.get(i)[1];
            z0 = dataList.get(i)[2];
            result = multiplyMatrix3D(new double[]{x0,y0,z0,1}, matrix); //apply overall transformation
            newLine[0] = result[0]; //new x0
            newLine[1] = result[1]; //new y0
            newLine[2] = result[2]; //new z0
            //Second point
            x1 = dataList.get(i)[3];
            y1 = dataList.get(i)[4];
            z1 = dataList.get(i)[5];
            result = multiplyMatrix3D(new double[]{x1,y1,z1,1}, matrix); //apply overall transformation
            newLine[3] = result[0]; //new x1
            newLine[4] = result[1]; //new y1
            newLine[5] = result[2]; //new z1
            dataList.set(i,newLine); //update the list of lines
        }
    }

    /**
     * Method to convert the World Coordinate System into the Eye Coordinate System.
     */
    protected void convertToEye(){
        double x0,y0,z0,x1,y1,z1;
        double[][] vMatrix = calculateV();
        double[][] nMatrix = clipLines();
        double[][] overallMatrix = multiplyMatrix3D(vMatrix,nMatrix);
        for(int i = 0; i<dataList.size(); i++) {
            double[] newPoint;
            double[] newLine = new double[6];
            //First point
            x0 = dataList.get(i)[0];
            y0 = dataList.get(i)[1];
            z0 = dataList.get(i)[2];
            newPoint = multiplyMatrix3D(new double[]{x0,y0,z0,1},overallMatrix);
            newLine[0] = newPoint[0]; //new x0
            newLine[1] = newPoint[1]; //new y0
            newLine[2] = newPoint[2]; //new z0
            //Second point
            x1 = dataList.get(i)[3];
            y1 = dataList.get(i)[4];
            z1 = dataList.get(i)[5];
            newPoint = multiplyMatrix3D(new double[]{x1,y1,z1,1},overallMatrix);
            newLine[3] = newPoint[0]; //new x1
            newLine[4] = newPoint[1]; //new y1
            newLine[5] = newPoint[2]; //new z1
            eyeCoordinateSystem.add(newLine); //update the list of lines
        }
    }

    /**
     * Helper Method to calculate the Viewing Transformation Matrix.
     * @return 4x4 Viewing Transformation Matrix
     */
    protected double[][] calculateV(){
        double[][] overall;
        double[][] t1 = basicTranslate(-viewpointCoordinate[0],-viewpointCoordinate[1],-viewpointCoordinate[2]);
        double[][] t2 = t2();
        double[][] t3 = t3(viewpointCoordinate[0],viewpointCoordinate[1]);
        double[][] t4 = t4(viewpointCoordinate[0],viewpointCoordinate[1],viewpointCoordinate[2]);
        double[][] t5 = t5();

        //Concatenation
        overall = multiplyMatrix3D(t1,t2);
        overall = multiplyMatrix3D(overall,t3);
        overall = multiplyMatrix3D(overall,t4);
        overall = multiplyMatrix3D(overall,t5);

        return overall;
    }

    /**
     * Helper Method to create the T2 matrix that will rotate the World Coordinate System
     * by 90 degrees about the x-axis.
     * @return 4x4 T2 Matrix
     */
    protected double[][] t2(){
        double[][] t2 = new double[4][4];
        t2[0][0] = 1;
        t2[1][2] = 1;
        t2[2][1] = -1;
        t2[3][3] = 1;

        return t2;
    }

    /**
     * Helper Method to create the T3 matrix that will rotate the World Coordinate System
     * about the y-axis.
     * @param xe X-coordinate of the viewpoint
     * @param ye Y-coordinate of the viewpoint
     * @return 4x4 T3 Matrix
     */
    protected double[][] t3(double xe, double ye){
        double[][] t3 = new double[4][4];
        double denominator = Math.sqrt(Math.pow(xe,2) + Math.pow(ye,2));
        t3[0][0] = -ye/denominator;
        t3[2][0] = xe/denominator;
        t3[1][1] = 1;
        t3[0][2] = -xe/denominator;
        t3[2][2] = -ye/denominator;
        t3[3][3] = 1;
        return t3;
    }

    /**
     * Helper Method to create the T4 matrix that will rotate the World Coordinate System
     * about the x-axis.
     * @param xe X-coordinate of the viewpoint
     * @param ye Y-coordinate of the viewpoint
     * @param ze Z-coordinate of the viewpoint
     * @return 4x4 T4 Matrix
     */
    protected double[][] t4(double xe, double ye, double ze){
        double[][] t4 = new double[4][4];
        double numerator = Math.sqrt(Math.pow(xe,2) + Math.pow(ye,2));
        double a = Math.pow(ze,2);
        double b = Math.pow(numerator,2);
        double denominator = Math.sqrt(a + b);

        t4[0][0] = 1;
        t4[1][1] = numerator/denominator;
        t4[2][1] = ze/denominator;
        t4[1][2] = -ze/denominator;
        t4[2][2] = numerator/denominator;
        t4[3][3] = 1;

        return t4;
    }

    /**
     * Helper Method to create the T5 Matrix that will invert the Z-axis.
     * @return 4x4 T5 Matrix
     */
    protected double[][] t5(){
        double[][] t5 = new double[4][4];

        t5[0][0] = 1;
        t5[1][1] = 1;
        t5[2][2] = -1;
        t5[3][3] = 1;

        return t5;
    }

    /**
     * Method to create the N Matrix for 3D clipping.
     * @return 4x4 N Matrix
     */
    protected double[][] clipLines(){
        double[][] nMatrix = new double[4][4];
        nMatrix[0][0] = distance/(screenHeight/2);
        nMatrix[1][1] = distance/(screenHeight/2);
        nMatrix[2][2] = 1;
        nMatrix[3][3] = 1;

        return nMatrix;
    }

    /**
     * Method to convert 3D coordinates into 2D coordinates.
     */
    public void perspectiveProjection(){
        double x0,y0,z0,x1,y1,z1;
        double vsx = viewportWidth/2;
        double vcx = vsx;
        double vsy = viewportHeight/2;
        double vcy = vsy;

        //Loop through list of lines
        for(int i=0; i< eyeCoordinateSystem.size();i++){
            double[] newLine = new double[4];
            //First point
            x0 = eyeCoordinateSystem.get(i)[0];
            y0 = eyeCoordinateSystem.get(i)[1];
            z0 = eyeCoordinateSystem.get(i)[2];
            newLine[0] = (x0/z0)*vsx + vcx; //new x0
            newLine[1] = (y0/z0)*vsy + vcy; //new y0
            //Second point
            x1 = eyeCoordinateSystem.get(i)[3];
            y1 = eyeCoordinateSystem.get(i)[4];
            z1 = eyeCoordinateSystem.get(i)[5];
            newLine[2] = (x1/z1)*vsx + vcx; //new x1
            newLine[3] = (y1/z1)*vsy + vcy; //new y1
            dataListProjected.add(newLine); //update the list of lines
        }
    }

    /**
     * Method to project pixels onto a window.
     */
    public void displayPixels3D(){
        panel.clear();
        eyeCoordinateSystem.clear();
        dataListProjected.clear();

        convertToEye();
        perspectiveProjection();
        //Send each row of data to the basic line drawing algorithm
        for(int i=0; i< dataListProjected.size();i++)
            panel.basicAlgo(dataListProjected.get(i)[0], dataListProjected.get(i)[1],
                    dataListProjected.get(i)[2],dataListProjected.get(i)[3]);
    }

    /**
     * Method to create a new file and save the modified coordinate points.
     * @param fileName name of the file to be created
     */
    public void outputLines3D(String fileName){
        //Starter code given by https://www.w3schools.com/java/java_files_create.asp
        try{
            File file = new File("./DataFiles/" + fileName);
            if(file.createNewFile()){
                System.out.println("The following file was created: " + fileName);
                FileWriter writer = new FileWriter("./DataFiles/" + fileName);
                for(double[] line : dataList){
                    writer.write(String.valueOf(line[0]) + " " + String.valueOf(line[1])
                            + " " + String.valueOf(line[2]) + " " + String.valueOf(line[3])
                            + " " + String.valueOf(line[4]) + " " + String.valueOf(line[5]) + "\n");
                }
                writer.close();
                System.out.println(String.valueOf(dataList.size()) +
                        " lines were successfully written to " + fileName);
            }else
                System.out.println("Sorry. This file already exists.");

        } catch(IOException e){
            System.out.println("Error: File cannot be created.");
            e.printStackTrace();
        }
    }

    /**
     * Method to create a translation matrix to translate a coordinate point.
     * @param tx displacement in x direction
     * @param ty displacement in y direction
     * @param tz displacement in z direction
     * @return 4x4 translation matrix
     */
    public double[][] basicTranslate(double tx, double ty, double tz){
        double[][] matrix = new double[4][4];
        // matrix[col][row]
        matrix[0][0] = 1;
        matrix[1][1] = 1;
        matrix[2][2] = 1;
        matrix[0][3] = tx;
        matrix[1][3] = ty;
        matrix[2][3] = tz;
        matrix[3][3] = 1;

        return matrix;
    }

    /**
     * Method to create a transformation matrix to scale a coordinate point about the origin.
     * @param sx scale in the x direction
     * @param sy scale in the y direction
     * @param sz scale in the z direction
     * @return 4x4 transformation matrix
     */
    public double[][] basicScale(double sx, double sy, double sz){
        double[][] matrix = new double[4][4];
        // matrix[col][row]
        matrix[0][0] = sx;
        matrix[1][1] = sy;
        matrix[2][2] = sz;
        matrix[3][3] = 1;

        return matrix;
    }

    /**
     * Method to create a transformation matrix to scale a coordinate point about an arbitrary center.
     * @param sx scale in the x direction
     * @param sy scale in the y direction
     * @param sz scale in the z direction
     * @param cx x value of arbitrary center
     * @param cy y value of arbitrary center
     * @param cz z value of arbitrary center
     * @return 4x4 overall transformation matrix
     */
    public double[][] scale(double sx, double sy, double sz, double cx, double cy, double cz){
        double[][] matrix1 = basicTranslate(-cx,-cy,-cz); //Translate to origin
        double[][] matrix2 = basicScale(sx,sy,sz); //Scale
        double[][] matrix3 = basicTranslate(cx,cy,cz); //Translate back to arbitrary origin

        matrix1 = multiplyMatrix3D(matrix1,matrix2); //First matrix concatenation
        matrix1 = multiplyMatrix3D(matrix1,matrix3); //Second matrix concatenation

        return matrix1;
    }

    /**
     * Method to create a transformation matrix to rotate a coordinate point about an axis.
     * @param angle degree to rotate the coordinate points.
     * @return 4x4 transformation matrix
     */
    public double[][] rotate(double angle, String axis){
        double[][] matrix = new double[4][4];
        double cos = Math.cos(Math.toRadians(angle));
        double sin = Math.sin(Math.toRadians(angle));

        // matrix[col][row]
        switch(axis){
            case "x":
                matrix[0][0] = 1;
                matrix[1][1] = cos;
                matrix[2][1] = sin;
                matrix[1][2] = -sin;
                matrix[2][2] = cos;
                break;
            case "y":
                matrix[0][0] = cos;
                matrix[2][0] = -sin;
                matrix[1][1] = 1;
                matrix[0][2] = sin;
                matrix[2][2] = cos;
                break;
            case "z":
                matrix[0][0] = cos;
                matrix[1][0] = sin;
                matrix[0][1] = -sin;
                matrix[1][1] = cos;
                matrix[2][2] = 1;
                break;
            default:
                matrix[0][0] = 1;
                matrix[1][1] = 1;
                matrix[2][2] = 1;
        }

        matrix[3][3] = 1;

        return matrix;
    }
}
