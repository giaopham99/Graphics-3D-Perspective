package com.csci4810;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

//Per Professor Arabnia's permission, starting code to activate and display pixels provided by
// https://www.javacodex.com/Graphics/Points
// https://stackoverflow.com/questions/64131301/drawing-multiple-lines-with-java
// https://stackoverflow.com/questions/3325546/how-to-color-a-pixel/3325804

public class Frame extends JPanel implements ActionListener {
    Timer timer = new Timer(0,this);
    Color black = new Color(0, 0, 0); // Color black
    int rgb = black.getRGB();
    private BufferedImage canvas;
    Graphics2D g2d;
    int width;
    int height;

    public Frame(int width, int height){
        this.width = width;
        this.height = height;
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;

        g2d.drawImage(canvas, null, null);
        timer.start();
    }

    //update to display next pixel
    public void actionPerformed(ActionEvent e){
        repaint();
    }

    /**Basic line drawing algorithm
     * the algorithm will calculate each set of coordinates to draw from the first point to the second point and
     * add it to two lists used in display purposes
     * will be calculated from L->R and T->D
     * @param x0 first x coordinate
     * @param y0 first y coordinate
     * @param x1 second x coordinate
     * @param y1 second y coordinate
     */
    public void basicAlgo(double x0, double y0, double x1, double y1){
        x0 = Math.round(x0);
        y0 = Math.round(y0);
        x1 = Math.round(x1);
        y1 = Math.round(y1);

        double x, y;
        //Swap so that x0 < x1
        if(x0 > x1){
            double tempX = x0;
            x0 = x1;
            x1 = tempX;
            double tempY = y0;
            y0 = y1;
            y1 = tempY;
        }

        double dx = x1-x0;
        double dy = y1-y0;
        double m;

        if(dx==0 && dy!=0 && x0>=0 && x0<width){ //VERTICAL
            //Swap so that y0 < y1
            if(y0 > y1){
                double temp = y0;
                y0 = y1;
                y1 = temp;
                dy = y1-y0;
            }

            m = dx/dy;

            for(int i=0; i<=(dy-1) && y1<height;i++){
                x = (int) ((m*i) + x0);
                y = y0 + i;
                if(x>=0 && x<width && y>=0 && y<height) //only perform if within canvas size
                    canvas.setRGB((int)x,(int)y,rgb); //set pixel
            }
        }else if(dy==0 && dx!=0 && y0>=0 && y0<height) { //HORIZONTAL
            m = dy/dx;
            for (int i = 0; i <= (dx-1) && x1<width; i++) {
                x = x0 + i;
                y = (int) ((m*i) + y0);
                if(x>=0 && x<width && y>=0 && y<height) //only perform if within canvas size
                    canvas.setRGB((int)x,(int)y,rgb); //set pixel
            }
        }else if (x0==x1 && y0==y1 && x0>=0 && x0<width && y0>=0 && y0<height){ //SAME POINT
            canvas.setRGB((int)x0,(int)y0,rgb); //set pixel
        }else if(dx==dy || dx==-dy) { //DIAGONAL
            m = dy/dx;
            for (int i = 0; i <= (dx-1); i++) {
                x = x0 + i;
                y = (int) ((m*i) + y0);
                if(x>=0 && x<width && y>=0 && y<height) //only perform if within canvas size
                    canvas.setRGB((int)x,(int)y,rgb); //set pixel
            }
        }else if(dx>dy && x1>x0 && y1>y0) { //POSITIVE MORE HORIZONTAL
            m = dy/dx;
            for(int i=0; i<=(dx-1);i++){
                x = x0 + i;
                y = (int) ((m*i) + y0);
                if(x>=0 && x<width && y>=0 && y<height) //only perform if within canvas size
                    canvas.setRGB((int)x,(int)y,rgb); //set pixel
            }
        }else if(dx>-dy && x1>x0 && y1<y0) { //NEGATIVE MORE HORIZONTAL
            m = dy/dx;
            for(int i=0; i<=(dx-1);i++) {
                x = x0 + i;
                y = (int) ((m * i) + y0);
                if(x>=0 && x<width && y>=0 && y<height) //only perform if within canvas size
                    canvas.setRGB((int)x,(int)y,rgb); //set pixel
            }
        }else if(dx<dy && x1>x0 && y1>y0) { //POSITIVE MORE VERTICAL
            m = dx/dy;
            for(int i=0; i<=(dy-1);i++){
                x = (int) ((m*i) + x0);
                y = y0 + i;
                if(x>=0 && x<width && y>=0 && y<height) //only perform if within canvas size
                    canvas.setRGB((int)x,(int)y,rgb); //set pixel
            }
        } else if(dx<-dy && x1>x0 && y1<y0) { //NEGATIVE MORE VERTICAL
            m = dx / -dy;
            for (int i = 0; i <= (-dy - 1); i++) {
                x = (int) ((m * i) + x0);
                y = y0 - i;
                if(x>=0 && x<width && y>=0 && y<height) //only perform if within canvas size
                    canvas.setRGB((int)x,(int)y,rgb); //set pixel
            }
        }
    }

    /**
     * Clear the canvas
     * https://stackoverflow.com/questions/1440750/set-bufferedimage-to-be-a-color-in-java
     */
    public void clear(){
        g2d = canvas.createGraphics();
        g2d.setBackground(new Color(0,0,0,0));
        g2d.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
