package com.csci4810;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


/**
 * Class to perform 2-Dimensional Transformations
 */
public class CustomGraphic {
    static final int WIDTH = 1024;
    static final int HEIGHT = 1024;
    protected List<double[]> dataList = new LinkedList<>(); //list containing points read in from file
    protected Frame panel = new Frame(WIDTH,HEIGHT);
    protected JFrame frame = new JFrame("Program 4");

    /**
     * Default Constructor
     * Sets up the panel and frame to display the graphics
     */
    CustomGraphic(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.WHITE);
        frame.setSize(WIDTH,HEIGHT);
        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     * Helper method to multiply 1x3 matrix with 3x3 matrix
     * @param matrix1 1x3 matrix containing the point (x,y,1)
     * @param matrix2 3x3 matrix containing the transformation matrix
     * @return resulting coordinate point
     */
    protected double[] multiplyMatrix2D(double[] matrix1, double[][] matrix2){
        double[] result = new double[3];
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
     * Helper method to multiply 3x3 matrix with 3x3 matrix
     * @param matrix1 first 3x3 matrix
     * @param matrix2 second 3x3 matrix
     * @return resulting 3x3 matrix
     */
    protected double[][] multiplyMatrix2D(double[][] matrix1, double[][] matrix2){
        double[][] result = new double[3][3];
        //Column for matrix2
        for(int col=0; col<matrix2.length;col++){ //in terms of matrix2
            //Row for matrix2
            for (int row=0; row<matrix2.length;row++){ //in terms of matrix2
                double sum = 0;
                //Matrix multiplication
                sum+= matrix1[0][row] * matrix2[col][0];
                sum+= matrix1[1][row] * matrix2[col][1];
                sum+= matrix1[2][row] * matrix2[col][2];
                result[col][row] = sum;
            }
        }
        return result;
    }

    /**
     * Method to read coordinates of a line in an external file to the LinkedList of data.
     * @param fileName name of the external file
     * @return number of lines read
     */
    public int inputLines2D(String fileName){
        String filePath = "./DataFiles/" + fileName;
        dataList.clear();
        //Starter code given by https://www.w3schools.com/java/java_files_read.asp
        try{
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String[]  data = reader.nextLine().split(" ",4); //Splits each line into 4 parts
                double[] lines = new double[4];
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
     * @param matrix 3x3 transformation matrix
     */
    public void applyTransformation2D(double[][] matrix){
        double x0,y0,x1,y1;
        //Loop through list of lines
        for(int i=0; i< dataList.size();i++){
            double[] result;
            double[] newLine = new double[4];
            //First point
            x0 = dataList.get(i)[0];
            y0 = dataList.get(i)[1];
            result = multiplyMatrix2D(new double[]{x0,y0,1}, matrix); //apply transformation
            newLine[0] = result[0];
            newLine[1] = result[1];
            //Second point
            x1 = dataList.get(i)[2];
            y1 = dataList.get(i)[3];
            result = multiplyMatrix2D(new double[]{x1,y1,1}, matrix); //apply transformation
            newLine[2] = result[0];
            newLine[3] = result[1];
            dataList.set(i,newLine); //update the list of lines
        }
    }

    /**
     * Method to project pixels onto a window.
     */
    public void displayPixels2D(){
        panel.clear();
        //Send each row of data to the basic line drawing algorithm
        for(int i=0; i< dataList.size();i++)
            panel.basicAlgo(dataList.get(i)[0], dataList.get(i)[1],dataList.get(i)[2],dataList.get(i)[3]);
    }

    /**
     * Method to create a new file and save the modified coordinate points.
     * @param fileName name of the file to be created
     */
    public void outputLines2D(String fileName){
        //Starter code given by https://www.w3schools.com/java/java_files_create.asp
        try{
            File file = new File("./DataFiles/" + fileName);
            if(file.createNewFile()){
                System.out.println("The following file was created: " + fileName);
                FileWriter writer = new FileWriter("./DataFiles/" + fileName);
                for(double[] line : dataList){
                    writer.write(String.valueOf(line[0]) + " " + String.valueOf(line[1])
                    + " " + String.valueOf(line[2]) + " " + String.valueOf(line[3]) + "\n");
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
     * @return 3x3 translation matrix
     */
    public double[][] basicTranslate(double tx, double ty){
        double[][] matrix = new double[3][3];
        // matrix[col][row]
        matrix[0][0] = 1;
        matrix[1][1] = 1;
        matrix[0][2] = tx;
        matrix[1][2] = ty;
        matrix[2][2] = 1;

        return matrix;
    }

    /**
     * Method to create a transformation matrix to scale a coordinate point about the origin.
     * @param sx scale in the x direction
     * @param sy scale in the y direction
     * @return 3x3 transformation matrix
     */
    public double[][] basicScale(double sx, double sy){
        double[][] matrix = new double[3][3];
        // matrix[col][row]
        matrix[0][0] = sx;
        matrix[1][1] = sy;
        matrix[2][2] = 1;

        return matrix;
    }

    /**
     * Method to create a transformation matrix to rotate a coordinate point about the origin.
     * @param angle degree to rotate the coordinate points.
     * @return 3x3 transformation matrix
     */
    public double[][] basicRotate(double angle){
        double[][] matrix = new double[3][3];
        double cos = Math.cos(Math.toRadians(angle));
        double sin = Math.sin(Math.toRadians(angle));
        // matrix[col][row]
        matrix[0][0] = cos;
        matrix[1][0] = -sin;
        matrix[0][1] = sin;
        matrix[1][1] = cos;
        matrix[2][2] = 1;

        return matrix;
    }

    /**
     * Method to create a transformation matrix to scale a coordinate point about an arbitrary center.
     * @param sx scale in the x direction
     * @param sy scale in the y direction
     * @param cx x value of arbitrary center
     * @param cy y value of arbitrary center
     * @return 3x3 transformation matrix
     */
    public double[][] scale(double sx, double sy, double cx, double cy){
        double[][] matrix1 = basicTranslate(-cx,-cy); //Translate to origin
        double[][] matrix2 = basicScale(sx,sy); //Scale
        double[][] matrix3 = basicTranslate(cx,cy); //Translate back to arbitrary origin

        matrix1 = multiplyMatrix2D(matrix1,matrix2); //First matrix concatenation
        matrix1 = multiplyMatrix2D(matrix1,matrix3); //Second matrix concatenation

        return matrix1;
    }

    /**
     * Method to create a transformation matrix to rotate a coordinate point about an arbitrary center.
     * @param angle degree to rotate the coordinate points
     * @param cx x value of arbitrary center
     * @param cy y value of arbitrary center
     * @return 3x3 transformation matrix
     */
    public double[][] rotate(double angle, double cx, double cy){
        double[][] matrix1 = basicTranslate(-cx,-cy); //Translate to origin
        double[][] matrix2 = basicRotate(angle); //Rotate
        double[][] matrix3 = basicTranslate(cx,cy); //Translate back to arbitrary origin

        matrix1 = multiplyMatrix2D(matrix1,matrix2); //First matrix concatenation
        matrix1 = multiplyMatrix2D(matrix1,matrix3); //Second matrix concatenation

        return matrix1;
    }






}
