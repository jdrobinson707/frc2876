/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author User
 */
public class Turn extends PIDSubsystem {
    private static final double Kp = -2;
    private static final double Ki = 0.0;
    private static final double Kd = 0.0;

    private static double SET_POINT = 45;
    Gyro gyro = new Gyro(RobotMap.GYRO_PORT);
    DriveTrain drive = new DriveTrain();

    // Initialize your subsystem here
    public Turn() {
        super("Turn", Kp, Ki, Kd);

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        setSetpoint(SET_POINT);

        // enable() - Enables the PID controller.
        enable();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        return gyro.getAngle();
    }

    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        //drive.drive(output / 4, output / 4);
    }
}