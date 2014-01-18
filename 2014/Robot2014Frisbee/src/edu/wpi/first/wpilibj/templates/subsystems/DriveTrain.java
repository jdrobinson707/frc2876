/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.Drive;

/**
 *
 * @author mentor
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    RobotDrive robotDrive2 = RobotMap.DRIVETRAIN_ROBOT_DRIVE_2;
    Jaguar rightDriveJaguar = RobotMap.DRIVETRAIN_RIGHTDRIVE_JAGUAR;
    Jaguar leftDriveJaguar = RobotMap.DRIVETRAIN_LEFTDRIVE_JAGUAR;
    public Encoder leftEncoder = RobotMap.DRIVETRAIN_LEFTENCODER;
    public Encoder rightEncoder = RobotMap.DRIVETRAIN_RIGHTENCODER;
    public Gyro gyro = RobotMap.DRIVETRAIN_GYRO;
    
    private static final int DRIVE_ENCODER_MIN_RATE = 10;
    private static final int DRIVE_ENCODER_MIN_PERIOD = 10;
    private static final double DRIVE_WHEEL_RADIUS = 3.7;
    private static final int PULSE_PER_ROTATION = 360;
    private static final double GEAR_RATIO = 42 / 39;
    private static final double DRIVE_ENCODER_PULSE_PER_ROT = PULSE_PER_ROTATION * GEAR_RATIO;
    private static final double DRIVE_ENCODER_DIST_PER_TICK =
            ((Math.PI * 2 * DRIVE_WHEEL_RADIUS) / DRIVE_ENCODER_PULSE_PER_ROT);

    public DriveTrain(){
        encoderSetup();
    }
    
    private void encoderSetup(){
        rightEncoder.setMaxPeriod(DRIVE_ENCODER_MIN_PERIOD);
        rightEncoder.setMinRate(DRIVE_ENCODER_MIN_RATE);
        rightEncoder.setDistancePerPulse(DRIVE_ENCODER_DIST_PER_TICK);

        leftEncoder.setMaxPeriod(DRIVE_ENCODER_MIN_PERIOD);
        leftEncoder.setMinRate(DRIVE_ENCODER_MIN_RATE);
        leftEncoder.setDistancePerPulse(DRIVE_ENCODER_DIST_PER_TICK);

        startEncoder(leftEncoder);
        startEncoder(rightEncoder);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new Drive());
    }
    
    public void drive(Joystick left, Joystick right) {
        robotDrive2.tankDrive(left.getY(), right.getY());
    }

    public void driveXboxTank(double left, double right) {
        robotDrive2.tankDrive(left, right);
    }
    
    public void driveXboxArcade(double move, double rotate) {
        robotDrive2.arcadeDrive(move,rotate);
    }
    
    public void startEncoder(Encoder encoder) {
        encoder.reset();
        encoder.start();
    }
}
