/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableGyro;
import edu.wpi.first.wpilibj.smartdashboard.SendablePIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.Drive;

/**
 *
 * @author User
 */
public class DriveTrain extends Subsystem {

    private static final double driveKp = 0.6;
    private static final double driveKi = 0.0;
    private static final double driveKd = 0.0;
    private static final double turnKp = 0.5;
    private static final double turnKi = 1.0;
    private static final double turnKd = 1.0;
    RobotDrive drive;
    SendableGyro gyro;
    SendablePIDController turnPID;
    SendablePIDController drivePID;

    // Initialize your subsystem here
    public DriveTrain() {
        drive = new RobotDrive(RobotMap.LEFT_DRIVE_MOTOR_PORT, RobotMap.RIGHT_DRIVE_MOTOR_PORT);
        drive.setSafetyEnabled(false);
        gyro = new SendableGyro(RobotMap.GYRO_PORT);
        SmartDashboard.putData("gyro", gyro);
        turnPID = new SendablePIDController(turnKp, turnKi, turnKd, gyro, new PIDOutput() {

            public void pidWrite(double output) {
                drive.tankDrive(output, -output);
                //drive.tankDrive(-output, output);
                
                SmartDashboard.putDouble("TurnPID Output", RobotMap.roundtoTwo(output));
                SmartDashboard.putDouble("Gyro Angle", RobotMap.roundtoTwo(gyro.getAngle()));
                SmartDashboard.putDouble("Turn PID Error:", RobotMap.roundtoTwo(turnPID.getError()));
            }
        });

        turnPID.setOutputRange(-.7, .7);
        // not implemented in wpilib code
        turnPID.setInputRange(-90, 90);
        turnPID.setTolerance(10);
        Timer.delay(.3);

        SmartDashboard.putData("TurnPID Object", turnPID);

        /*drivePID = new SendablePIDController(driveKp, driveKi, driveKd, sonar, new PIDOutput() {

        public void pidWrite(double output) {
        drive.tankDrive(output/2, output/2);
        }
        });
        drivePID.setOutputRange(-1.0, 1.0);
        drivePID.setInputRange(-27, 27);
        drivePID.enable();*/
    }

    public void startTurn(double degrees) {
        gyro.reset();
        turnPID.reset();
        turnPID.setSetpoint(degrees);
        turnPID.enable();
        Timer.delay(.3);
    }

    public boolean isTurnFinished() {
        return turnPID.onTarget();
    }

    public void endTurn() {
        turnPID.disable();
    }

    public void drive(Joystick left, Joystick right) {
        drive.tankDrive(left, right);
    }

    public void halfDrive(double left, double right)
    {
        drive.tankDrive(left / 1.5, right / 1.5);
    }

    public void reverseDrive(Joystick left, Joystick right)
    {
        drive.tankDrive(-right.getY(), -left.getY());
    }


    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Drive());
    }
}
