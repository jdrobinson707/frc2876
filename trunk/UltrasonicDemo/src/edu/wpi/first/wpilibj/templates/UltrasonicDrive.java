/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar; //If your team uses victors, import them instead
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

/**
 * This program sets up a basic robot, with two motors and ultrasonic sensor
 *
 */
public class UltrasonicDrive extends SimpleRobot {

    //Initializes the motors
    private final SpeedController left = new Jaguar(3);
    private final SpeedController right = new Jaguar(4);

    //Initializes the encoders
    private final Ultrasonic sonar = new Ultrasonic(1, 1);  //1, 2

    //Initializes the drive
    private RobotDrive drive = new RobotDrive(left, right);

    public UltrasonicDrive() {

    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        drive.setSafetyEnabled(false);
        double now = Timer.getFPGATimestamp();
        double last = now;
        System.out.println(sonar.isRangeValid());
        System.out.println(sonar.isEnabled());
        while (isOperatorControl() && isEnabled()) {
            //DriveDistance();

            //DriveDistancePID();
            now = Timer.getFPGATimestamp();
            if (now - last > 1.0) {
                System.out.println("Sonar Distance: " + sonar.getRangeInches());
                last = now;
            }

        }
    }

}
