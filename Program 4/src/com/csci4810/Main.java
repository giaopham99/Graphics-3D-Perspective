package com.csci4810;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String file;

        Scanner keyboard = new Scanner(System.in);
        String command = "";
        double sx,sy,sz = 1;
        double cx,cy, cz = 0;
        double angle;
        String axis;

        //2D Overall Transformation Matrix
        double[][] transformMatrix = new double[3][3];
        //Default value identity matrix for transformation matrix
        transformMatrix[0][0] = 1;
        transformMatrix[1][1] = 1;
        transformMatrix[2][2] = 1;

        //3D Overall Transformation Matrix
        double[][] transformMatrix3D = new double[4][4];
        //Default value identity matrix for transformation matrix
        transformMatrix3D[0][0] = 1;
        transformMatrix3D[1][1] = 1;
        transformMatrix3D[2][2] = 1;
        transformMatrix3D[3][3] = 1;

        CustomGraphic3D graphic = new CustomGraphic3D();

        //Initial File must be given in order to use program
        System.out.println("Pick a dimension:");
        System.out.println("a) 2D");
        System.out.println("b) 3D");
        String dimension = keyboard.nextLine();

        System.out.println("Please enter a file name that contains your data. \nMake sure the .txt file is" +
                " placed inside the DataFile folder.");
        file = keyboard.nextLine();
        if(dimension.equals("a"))
            graphic.inputLines2D(file);
        else if(dimension.equals("b"))
            graphic.inputLines3D(file);
        System.out.println("File data has been read.");

        while(!command.equals("0") && !command.equals("q")) {
            System.out.println("COMMANDS:");

            if(dimension.equals("a")) { //2D
                System.out.println("1) Input File Lines");
                System.out.println("2) Apply a Transformation");
                System.out.println("3) Display Pixels");
                System.out.println("4) Output Lines to a New File");
                System.out.println("5) Basic Translation");
                System.out.println("6) Basic Scale Around Origin");
                System.out.println("7) Basic Rotate Around Origin");
                System.out.println("8) Scale About Arbitrary Center");
                System.out.println("9) Rotate About Arbitrary Center");
                System.out.println("0) Quit");
                command = keyboard.next();
                switch (command) {
                    case "0":
                        break;
                    case "q":
                        break;
                    case "1":
                        System.out.println("Enter a file name: ");
                        keyboard.nextLine();
                        file = keyboard.nextLine();
                        System.out.println("Pick a dimension:");
                        System.out.println("a) 2D");
                        System.out.println("b) 3D");
                        dimension = keyboard.next();
                        if (dimension.equals("a"))
                            graphic.inputLines2D(file);
                        else if (dimension.equals("b"))
                            graphic.inputLines3D(file);
                        System.out.println("File data has been read.");
                        break;
                    case "2":
                        graphic.applyTransformation2D(transformMatrix);
                        System.out.println("Transformation applied.");
                        break;
                    case "3":
                        graphic.displayPixels2D();
                        break;
                    case "4":
                        System.out.println("Enter a file name: ");
                        keyboard.nextLine();
                        file = keyboard.nextLine();
                        graphic.outputLines2D(file);
                        break;
                    case "5":
                        System.out.println("Translate in x direction: ");
                        double tx = keyboard.nextDouble();
                        System.out.println("Translate in y direction: ");
                        double ty = keyboard.nextDouble();
                        transformMatrix = graphic.basicTranslate(tx, ty); //basic translate and save matrix
                        System.out.print("Would you like to apply this transformation? (y/n)");
                        command = keyboard.next();
                        if (command.equals("y")) {
                            graphic.applyTransformation2D(transformMatrix); //apply the transformation
                            System.out.println("Transformation applied.");
                        }
                        break;
                    case "6":
                        System.out.println("Scale factor in x direction: ");
                        sx = keyboard.nextDouble();
                        System.out.println("Scale factor in y direction: ");
                        sy = keyboard.nextDouble();
                        transformMatrix = graphic.basicScale(sx, sy); //basic scale and save matrix
                        System.out.print("Would you like to apply this transformation? (y/n)");
                        command = keyboard.next();
                        if (command.equals("y")) {
                            graphic.applyTransformation2D(transformMatrix); //apply the transformation
                            System.out.println("Transformation applied.");
                        }
                        break;
                    case "7":
                        System.out.println("Rotation Angle: ");
                        angle = keyboard.nextDouble();
                        transformMatrix = graphic.basicRotate(angle);
                        System.out.print("Would you like to apply this transformation? (y/n)");
                        command = keyboard.next();
                        if (command.equals("y")) {
                            graphic.applyTransformation2D(transformMatrix); //apply the transformation
                            System.out.println("Transformation applied.");
                        }
                        break;
                    case "8":
                        System.out.println("Scale factor in x direction: ");
                        sx = keyboard.nextDouble();
                        System.out.println("Scale factor in y direction: ");
                        sy = keyboard.nextDouble();
                        System.out.println("X-coordinate of the center of scale: ");
                        cx = keyboard.nextDouble();
                        System.out.println("Y-coordinate of the center of scale: ");
                        cy = keyboard.nextDouble();
                        transformMatrix = graphic.scale(sx, sy, cx, cy); //basic scale and save matrix
                        System.out.print("Would you like to apply this transformation? (y/n)");
                        command = keyboard.next();
                        if (command.equals("y")) {
                            graphic.applyTransformation2D(transformMatrix); //apply the transformation
                            System.out.println("Transformation applied.");
                        }
                        break;
                    case "9":
                        System.out.println("Rotation Angle: ");
                        angle = keyboard.nextDouble();
                        System.out.println("X-coordinate of the center of rotation: ");
                        cx = keyboard.nextDouble();
                        System.out.println("Y-coordinate of the center of rotation: ");
                        cy = keyboard.nextDouble();
                        transformMatrix = graphic.rotate(angle, cx, cy); //basic scale and save matrix
                        System.out.print("Would you like to apply this transformation? (y/n)");
                        command = keyboard.next();
                        if (command.equals("y")) {
                            graphic.applyTransformation2D(transformMatrix); //apply the transformation
                            System.out.println("Transformation applied.");
                        }
                        break;
                    default:
                        System.out.println("Sorry Command Not Found.");
                        break;
                }
            }else if(dimension.equals("b")) { //3D
                System.out.println("1) Input File Lines");
                System.out.println("2) Apply a Transformation");
                System.out.println("3) Display Pixels");
                System.out.println("4) Output Lines to a New File");
                System.out.println("5) Basic Translation");
                System.out.println("6) Basic Scale Around Origin");
                System.out.println("7) Scale About Arbitrary Center");
                System.out.println("8) Rotate About An Axis");
                System.out.println("9) Set Perspective Projection");
                System.out.println("0) Quit");
                command = keyboard.next();
                switch (command) {
                    case "0":
                        break;
                    case "q":
                        break;
                    case "1":
                        System.out.println("Enter a file name: ");
                        keyboard.nextLine();
                        file = keyboard.nextLine();
                        System.out.println("Pick a dimension:");
                        System.out.println("a) 2D");
                        System.out.println("b) 3D");
                        dimension = keyboard.next();
                        if (dimension.equals("a"))
                            graphic.inputLines2D(file);
                        else if (dimension.equals("b"))
                            graphic.inputLines3D(file);
                        System.out.println("File data has been read.");
                        break;
                    case "2":
                        graphic.applyTransformation3D(transformMatrix3D);
                        System.out.println("Transformation applied.");
                        break;
                    case "3":
                        graphic.displayPixels3D();
                        break;
                    case "4":
                        System.out.println("Enter a file name: ");
                        keyboard.nextLine();
                        file = keyboard.nextLine();
                        graphic.outputLines3D(file);
                        break;
                    case "5":
                        System.out.println("Translate in x direction: ");
                        double tx = keyboard.nextDouble();
                        System.out.println("Translate in y direction: ");
                        double ty = keyboard.nextDouble();
                        System.out.println("Translate in z direction: ");
                        double tz = keyboard.nextDouble();
                        transformMatrix3D = graphic.basicTranslate(tx, ty, tz); //basic translate and save matrix
                        System.out.print("Would you like to apply this transformation? (y/n)");
                        command = keyboard.next();
                        if (command.equals("y")) {
                            graphic.applyTransformation3D(transformMatrix3D); //apply the transformation
                            System.out.println("Transformation applied.");
                        }
                        break;
                    case "6":
                        System.out.println("Scale factor in x direction: ");
                        sx = keyboard.nextDouble();
                        System.out.println("Scale factor in y direction: ");
                        sy = keyboard.nextDouble();
                        System.out.println("Scale factor in z direction: ");
                        sz = keyboard.nextDouble();
                        transformMatrix3D = graphic.basicScale(sx, sy, sz); //basic scale and save matrix
                        System.out.print("Would you like to apply this transformation? (y/n)");
                        command = keyboard.next();
                        if (command.equals("y")) {
                            graphic.applyTransformation3D(transformMatrix3D); //apply the transformation
                            System.out.println("Transformation applied.");
                        }
                        break;
                    case "7":
                        System.out.println("Scale factor in x direction: ");
                        sx = keyboard.nextDouble();
                        System.out.println("Scale factor in y direction: ");
                        sy = keyboard.nextDouble();
                        System.out.println("Scale factor in z direction: ");
                        sz = keyboard.nextDouble();
                        System.out.println("X-coordinate of the center of scale: ");
                        cx = keyboard.nextDouble();
                        System.out.println("Y-coordinate of the center of scale: ");
                        cy = keyboard.nextDouble();
                        System.out.println("Z-coordinate of the center of scale: ");
                        cz = keyboard.nextDouble();
                        transformMatrix3D = graphic.scale(sx, sy, sz, cx, cy, cz); //basic scale and save matrix
                        System.out.print("Would you like to apply this transformation? (y/n)");
                        command = keyboard.next();
                        if (command.equals("y")) {
                            graphic.applyTransformation3D(transformMatrix3D); //apply the transformation
                            System.out.println("Transformation applied.");
                        }
                        break;
                    case "8":
                        System.out.println("Rotation Angle: ");
                        angle = keyboard.nextDouble();
                        System.out.println("Rotate about which axis:");
                        System.out.println("(x) X-Axis");
                        System.out.println("(y) Y-Axis");
                        System.out.println("(z) Z-Axis");
                        axis = keyboard.next();
                        transformMatrix3D = graphic.rotate(angle, axis);
                        System.out.print("Would you like to apply this transformation? (y/n)");
                        command = keyboard.next();
                        if (command.equals("y")) {
                            graphic.applyTransformation3D(transformMatrix3D); //apply the transformation
                            System.out.println("Transformation applied.");
                        }
                        break;
                    case "9":
                        System.out.println("Distance from Viewpoint to Screen (cm):");
                        double distance = keyboard.nextDouble();
                        System.out.println("Viewpoint (In the form x,y,z): ");
                        keyboard.nextLine();
                        String viewpointLine = keyboard.nextLine();
                        String[] viewpoint = viewpointLine.split(",",3);
                        graphic.set3DPositions(distance, viewpoint);
                        System.out.println("Positions have been updated.");
                        break;
                    default:
                        System.out.println("Sorry Command Not Found.");
                        break;
                }
            }
        }
        System.exit(0);
    }
}
