/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Jaguar; //If your team uses victors, import them instead
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

/**
 * This program sets up a basic robot, with two motors and ultrasonic sensor
 *
 */
public class UltrasonicDrive extends SimpleRobot {

    //Initializes the motors
    private final SpeedController left = new Jaguar(3);
    private final SpeedController right = new Jaguar(4);

    //Initializes the encoders
    private final AnalogChannel sonar = new AnalogChannel(1, 7);

    //Initializes the drive
    private RobotDrive drive = new RobotDrive(left, right);

    public UltrasonicDrive() {
        drive.setSafetyEnabled(false);
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {

        double now = Timer.getFPGATimestamp();
        double last = now;
        while (isOperatorControl() && isEnabled()) {
            //DriveDistance();

            //DriveDistancePID();
            now = Timer.getFPGATimestamp();
            if (now - last > 1.0) {
                System.out.println("volt: " + sonar.getVoltage() + " dist:" + getDist());
                last = now;
            }

        }
    }

    public double getDist() {
        return sonar.getVoltage() / .009766;

    }

    private class SonarInput implements PIDSource {

        public double pidGet() {
            return getDist();
        }
    }

    private class SonarOutput implements PIDOutput {

        public void pidWrite(double output) {
            drive.tankDrive(output, output);
        }
    }

    public void driveAndDance() {
        double kp = .5;
        double ki = 0;
        double kd = .1;

        PIDController pid = new PIDController(kp, ki, kd, new SonarInput(), new SonarOutput());
        pid.setOutputRange(-.5, 5);
        pid.setPercentTolerance(5);
        pid.enable();
        double now = Timer.getFPGATimestamp();
        double last = now;
        Timer.delay(1);
        pid.setSetpoint(24);

        while (isAutonomous() && isEnabled()) {
            now = Timer.getFPGATimestamp();
            if (now - last > .5) {
                System.out.println("In:" + getDist() + " Out:" + pid.get() + " Err:" + pid.getError());
                last = now;
            }
        }
    }

    public void driveAndStop() {
        double kp = .5;
        double ki = 0;
        double kd = .5;

        PIDController pid = new PIDController(kp, ki, kd, new SonarInput(), new SonarOutput());
        pid.setOutputRange(-.5, 5);
        pid.setPercentTolerance(5);
        pid.enable();
        double now = Timer.getFPGATimestamp();
        double last = now;
        Timer.delay(1);
        pid.setSetpoint(24);

        // while (pid.onTarget() == false) {
        while (Math.abs(pid.getError()) > 2) {
            now = Timer.getFPGATimestamp();
            if (now - last > .5) {
                System.out.println("In:" + getDist() + " Out:" + pid.get() + " Err:" + pid.getError());
                last = now;
            }
        }
        pid.disable();
        drive.tankDrive(0, 0);
    }

    public void autonomous() {
        driveAndStop();
        driveAndDance();
    }
}
