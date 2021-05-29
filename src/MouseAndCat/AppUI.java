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
    File name: AppUI.java
    Compile: javac AppUI.java
    Purpose: This is the class file that defines the user interface
    This class is meant to be called from the main class.
    It's the backbone of the program that involves in defining the user interface and holding basic 
    functionality of the program.
*/

package MouseAndCat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.*;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;


public class AppUI extends JFrame implements ActionListener  {
    
    private JPanel pnlTitle = new JPanel();
    private RicochetField pnlField = new RicochetField();
    private JPanel pnlControl = new JPanel();
    
    private GridBagLayout controlGrid = new GridBagLayout();
    private Clockhandlerclass clockhandler;
    
    private JTextField inputRefreshRateTextField = new JTextField("0");
    private JTextField inputSpeedTextField = new JTextField("0");
    private JTextField inputCatSpeedTextField = new JTextField("0");
    private JTextField inputDirectionTextField = new JTextField("0");
    
    private JLabel distanceLabel = new JLabel("400 or somethin");
    
    private JButton startButton = new JButton("Start");
    private JButton quitButton = new JButton("Quit");
    private JButton clearButton = new JButton("Clear");
    
    private Timer timer, catTimer;
    
    
    
    public AppUI() {
        this.setLayout(new BorderLayout(3, 1));
       
        pnlTitle.setPreferredSize(new Dimension(50, 50));
        pnlField.setPreferredSize(new Dimension(100, 100));
        pnlControl.setPreferredSize(new Dimension(200, 200));
        
        this.add(pnlTitle, BorderLayout.PAGE_START);
        this.add(pnlField, BorderLayout.CENTER);
        this.add(pnlControl, BorderLayout.SOUTH);

        /*
        Title Panel Setup
        */
        pnlTitle.setBackground(Color.YELLOW);
        pnlTitle.add(new JLabel("Ricochet Ball by Quentin May"));

        /*
        Field Panel Setup
        */
        pnlField.setBackground(Color.GREEN);
        pnlField.setLayout(new GridLayout(4, 2, 5, 5));

        /*
        Control Panel Setup
        */
        
        pnlControl.setBackground(Color.PINK);
        pnlControl.setLayout(controlGrid);
        

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 1;
        c.weightx = .01;
        c.weighty = .2;
        c.gridx = 0;
        c.gridy = 0;
        
        pnlControl.add(clearButton, c);
        clearButton.setBackground(Color.WHITE);
        clearButton.addActionListener(this);
        
        
//        startButton.setPreferredSize(new Dimension(50, 50));

        c.gridx = 1;
        pnlControl.add(startButton, c);
        startButton.setBackground(Color.WHITE);
        startButton.addActionListener(this);
        
        
        c.gridx = 2;
        pnlControl.add(quitButton, c);
        quitButton.setBackground(Color.WHITE);
        quitButton.addActionListener(this);
        
        
        c.gridx = 3;
        JPanel nestedLocation = new JPanel();
        nestedLocation.setBackground(Color.PINK);
        nestedLocation.setLayout(new GridLayout(3, 2, 5, 20));
        nestedLocation.add(new JLabel("Distance between:"));
        nestedLocation.add(new JLabel(" "));
        nestedLocation.add(distanceLabel);
        pnlControl.add(nestedLocation, c);

        
        c.gridx = 0;
        c.gridy = 1;
        JPanel nestedRefreshRate = new JPanel();
        nestedRefreshRate.setBackground(Color.PINK);
        nestedRefreshRate.setLayout(new GridLayout(2, 1));
        nestedRefreshRate.add(new JLabel("Refresh Rate (Hz)"));
        nestedRefreshRate.add(inputRefreshRateTextField);
        pnlControl.add(nestedRefreshRate, c);

        c.gridx = 1;
        JPanel nestedCatSpeed = new JPanel();
        nestedCatSpeed.setBackground(Color.PINK);
        nestedCatSpeed.setLayout(new GridLayout(2, 1));
        nestedCatSpeed.add(new JLabel("Cat Speed (pix/sec)"));
        nestedCatSpeed.add(inputCatSpeedTextField);
        pnlControl.add(nestedCatSpeed, c);
        
        c.gridx = 2;
        JPanel nestedSpeed = new JPanel();
        nestedSpeed.setBackground(Color.PINK);
        nestedSpeed.setLayout(new GridLayout(2, 1));
        nestedSpeed.add(new JLabel("Mouse Speed (pix/sec)"));
        nestedSpeed.add(inputSpeedTextField);
        pnlControl.add(nestedSpeed, c);
        
        
        
        c.gridx = 3;
        JPanel nestedDirection = new JPanel();
        nestedDirection.setBackground(Color.PINK);
        nestedDirection.setLayout(new GridLayout(2, 1));
        nestedDirection.add(new JLabel("Mouse Direction"));
        nestedDirection.add(inputDirectionTextField);
        pnlControl.add(nestedDirection, c);
        
        
        /*
        Clock Handler Setup
        */
        clockhandler = new Clockhandlerclass();
        timer = new Timer(10, clockhandler);
        catTimer = new Timer(10, clockhandler);
        
        this.setSize(1920,1040);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Mouse And Cat");
        this.setVisible(true); 
        pnlField.initializeBall(0.0, 0.0, 952, 375, 0.0, 0.0);
        pnlField.repaint();
        distanceLabel.setText((int)pnlField.getDistance() + "");
    }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == startButton) {
                if (startButton.getText() == "Start") { //When click start, initialize the ball then change the text to pause
                    System.out.println("Start Button called");
                    try {
                        double speed = Double.parseDouble(inputSpeedTextField.getText());
                        if (Double.isNaN(speed)) throw new Exception(); // If the inputSpeed isnt a double or speed is negative or zero, throw an error;
                        double catSpeed = Double.parseDouble(inputCatSpeedTextField.getText());
                        if (Double.isNaN(catSpeed)) throw new Exception();
                        double refreshRate = Double.parseDouble(inputRefreshRateTextField.getText());
                        if (Double.isNaN(refreshRate)) throw new Exception();
                        double direction = Double.parseDouble(inputDirectionTextField.getText());
                        if (Double.isNaN(direction) || direction < 0 || direction > 360) throw new Exception();
                        
                        
                        pnlField.initializeBall(speed,catSpeed, 952, 375, refreshRate, direction);
                        

                        //delay= 1000ms/hz. Converts refresh rate into delay between ticks.
                        timer.setDelay((int)(1000/refreshRate));
                        timer.start();
                        
                        catTimer.setDelay((int)(1000/refreshRate));
                        catTimer.start();
                        
                        startButton.setText("Pause");
                        
                    } catch (Exception err) {
                        System.out.println("Error");
                    }
                } else if (startButton.getText() == "Pause") { //When paused, just stop timer and change button to resume.
                    timer.stop();
                    catTimer.stop();
                    startButton.setText("Resume");
                } else if (startButton.getText() == "Resume") { //When resume, simply just start timer and change button to pause.
                    timer.start();
                    catTimer.start();
                    startButton.setText("Pause");
                }
            } else if (e.getSource() == quitButton) {
                System.exit(0);
            } else if (e.getSource() == clearButton) {
                //clear the boxes   
                timer.stop();
                catTimer.stop();
                inputCatSpeedTextField.setText("0");
                inputSpeedTextField.setText("0");
                inputRefreshRateTextField.setText("0");
                inputDirectionTextField.setText("0");
                startButton.setText("Start");
                pnlField.initializeBall(0.0, 0.0, 952, 375, 0.0, 0.0);
                pnlField.repaint();
                distanceLabel.setText((int)pnlField.getDistance() + "");
            }
        }

         private class Clockhandlerclass implements ActionListener {   
             public void actionPerformed(ActionEvent event) {

                 if (event.getSource() == timer) {
                    pnlField.repaint();
                    
                    
                } else if (event.getSource() == catTimer) {
                    //Collision detection
                    double distance = pnlField.getDistance();
                    distanceLabel.setText((int)distance + "");
                    if (distance == 0) {
                        timer.stop();
                        catTimer.stop();
                    }
                }
         }
    }
}
