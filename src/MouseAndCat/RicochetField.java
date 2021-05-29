/*
    Program name: "Mouse And Cat". This program uses a simple UI to
    simulate a mouse ricocheting of walls with a cat chasing it.
    Copyright (C) 2021  Quentin May

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

/*
Author information:
    Author: Quentin May
    Email: quentinemay@gmail.com, quentinemay@csu.fullerton.edu
*/

/*
Program information:
    Program name: Mouse And Cat
    Programming language: Java
    Files: main.java, AppUI.java, Computations.java, RicochetField.java, run.sh
    Date project began: 2021-May-18
    Date of last update: 2021-May-19
    Status: Finished
    Purpose: This program animates a ball (mouse) running in a direction with a cat chasing it.
    Base test system: Linux system with Bash shell and openjdk-14-jdk
*/

/*
This Module:
    File name: RicochetField.java
    Compile: javac RicochetField.java
    Purpose: This is the class file that is a modified JPanel that holds the functionality for drawing the field.
    This class is meant to be called from the main class.
*/
package MouseAndCat;

import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.JPanel;


public class RicochetField extends JPanel {
        private Point2D.Double catPosition = new Point2D.Double(10, 10);
        private int catRadius = 10;
        private double catDeltaX;
        private double catDeltaY;
        private double cat_distance_traveled_per_tic;
        
        private double distance;
        
        
        private Point2D.Double ballPosition = new Point2D.Double(0, 1);
        private int ballRadius = 5;
        private double deltaX;
        private double deltaY;
        private double distance_traveled_per_tic;
        
        
        private double catPixPerSec;
        private double pixPerSec;
        private boolean moving;
        
    public RicochetField() {

    }
    
    
    public void paintComponent(Graphics g) {
        
        Computations comp = new Computations();
        super.paintComponent(g);
        
        
        int wall = 0;
        double changeX = 0;
        double changeY = 0;


        
        
        if (pixPerSec != 0) {
            //Move cat first.
            double[] catDelta = comp.computeCatDeltaPerTic(catPosition, ballPosition, this.cat_distance_traveled_per_tic);
            catDeltaX = catDelta[0];
            catDeltaY = catDelta[1];
            
            g.setColor(Color.ORANGE);
            catPosition.x = catPosition.x + catDeltaX; 
            catPosition.y = catPosition.y + catDeltaY; 
            g.fillOval((int)(catPosition.getX() - catRadius), (int)(catPosition.getY() - catRadius), catRadius*2, catRadius*2);
            
            
            
            if ((ballPosition.x + deltaX + ballRadius) >= this.getSize().width) { //Ran into right wall.
                wall = 1;
                changeX = this.getSize().width - (ballPosition.x + ballRadius);
                changeY = (deltaY * changeX) / deltaX;
            }else if ((ballPosition.y + deltaY + ballRadius) >= this.getSize().height) { //Ran into bottom wall
                wall = 4;
                changeY = this.getSize().height - (ballPosition.y + ballRadius);
                changeX = (deltaX * changeY) / deltaY;

            } else if ((ballPosition.x + deltaX - ballRadius) <= 0) { //Ran into left wall
                wall = 3;
                changeX = -1 * (ballPosition.x - ballRadius);
                changeY = (deltaY * changeX) / deltaX;
            } else if ((ballPosition.y + deltaY - ballRadius) <= 0) { //Ran into top wall
                wall = 2;
                changeY =  -1 * (ballPosition.y - ballRadius);
                changeX = (deltaX * changeY) / deltaY;
            } else { //Didnt run into any walls
                g.setColor(Color.RED);
                ballPosition.setLocation(ballPosition.getX() + deltaX, ballPosition.getY() + deltaY);
                g.fillOval((int)(ballPosition.getX() - ballRadius), (int)(ballPosition.getY() - ballRadius), ballRadius*2, ballRadius*2); 
                distance = comp.calculateDistance(catPosition, ballPosition, catRadius, ballRadius);
                
                return;
            }
                g.setColor(Color.RED);
                //get the point where it WOULD contact the wall and set the ball to there. So were technically teleporting to the wall.
                ballPosition.setLocation(ballPosition.getX() + changeX, ballPosition.getY() + changeY);
                g.fillOval((int)(ballPosition.getX() - ballRadius), (int)(ballPosition.getY() - ballRadius), ballRadius*2, ballRadius*2); 
                distance = comp.calculateDistance(catPosition, ballPosition, catRadius, ballRadius);
                //At this point, we are too close to the wall, so we just change our deltas for the change in direction
                double[] delta = comp.computeDeltaAfterBounce(deltaX, deltaY, wall, this.distance_traveled_per_tic);
                deltaX = delta[0];
                deltaY = delta[1];
        } else { //speed == 0, so this is only called when we first initialize the ball.
            //Make the mouse
            g.setColor(Color.RED);
            ballPosition.setLocation(ballPosition.getX() + changeX, ballPosition.getY() + changeY);
            g.fillOval((int)(ballPosition.getX() - ballRadius), (int)(ballPosition.getY() - ballRadius), ballRadius*2, ballRadius*2); 
            
            
            //Make the cat
            g.setColor(Color.ORANGE);
            g.fillOval((int)(catPosition.getX() - catRadius), (int)(catPosition.getY() - catRadius), catRadius*2, catRadius*2); 
        }
    }
    
    

    
    //Utility used by our AppUI to check if the ball is still moving.
    public boolean isMoving() {
        return moving;
    }
    
    //Utility used by our main to get ball position.
    public Point2D.Double getBallPosition() {
        return ballPosition;
    }
    
    public double getDistance() {
        return distance;
    }
    
    //Call this function from the main class before running
    public void initializeBall(double pixPerSec,double catPixPerSec, double locationX, double locationY, double refreshRate, double directionAngle) {
            Computations comp = new Computations();
            this.moving = true;
            this.pixPerSec = pixPerSec;
            this.catPixPerSec = catPixPerSec;
            
            distance_traveled_per_tic = comp.distancePerTic(refreshRate, pixPerSec);
            cat_distance_traveled_per_tic = comp.distancePerTic(refreshRate, catPixPerSec);
            
            this.catPosition.setLocation(10.0, 10.0);
            this.ballPosition.setLocation(locationX, locationY);
            
            distance = comp.calculateDistance(catPosition, ballPosition, catRadius, ballRadius);
            
            
            double[] delta = comp.computeInitialDeltaPerTic(directionAngle, this.distance_traveled_per_tic);
            deltaX = delta[0];
            deltaY = delta[1];
            
            double[] catDelta = comp.computeCatDeltaPerTic(catPosition, ballPosition, this.cat_distance_traveled_per_tic);
            catDeltaX = catDelta[0];
            catDeltaY = catDelta[1];
            

            

    }
}
