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

    private static final double turnKp = 5;
    private static final double turnKi = 0;
    private static final double turnKd = 0;
    RobotDrive drive;
    SendableGyro gyro;
    SendablePIDController turnPID;
    public double limitleft, limitright;

    // Initialize your subsystem here
    public DriveTrain() {
        drive = new RobotDrive(RobotMap.LEFT_DRIVE_MOTOR_PORT, RobotMap.RIGHT_DRIVE_MOTOR_PORT);
        drive.setSafetyEnabled(false);
        gyro = new SendableGyro(RobotMap.GYRO_PORT);
        turnPID = new SendablePIDController(turnKp, turnKi, turnKd, gyro, new PIDOutput() {

            public void pidWrite(double output) {
                limitleft = output;
                limitright = -output;
                //drive.tankDrive(output, -output);
                drive.tankDrive(-output, output);
            }
        });

        turnPID.setOutputRange(-.8, .8);
        // not implemented in wpilib code
        turnPID.setInputRange(-90, 90);

        turnPID.setTolerance(5);
        SmartDashboard.putData("T_PID", turnPID);

        limitleft = limitright = 0;
    }

    public void init() {
        gyro.reset();
        turnPID.reset();
        turnPID.setSetpoint(0);
        limitleft = limitright = 0;
    }

    public void startTurn(double degrees) {
        gyro.reset();
        turnPID.reset();
        turnPID.setSetpoint(degrees);
        turnPID.enable();
        Timer.delay(.1);
        System.out.println("start turning from "
                + RobotMap.roundtoTwo(gyro.getAngle())
                + " to setpoint " + degrees);
    }

    public boolean isTurnFinished() {
        System.out.println("is turn done: " + turnPID.onTarget()
                + " deg:" + RobotMap.roundtoTwo(gyro.getAngle())
                + " out:" + RobotMap.roundtoTwo(turnPID.get())
                + " err:" + RobotMap.roundtoTwo(turnPID.getError()));

        return turnPID.onTarget();
    }

    public void endTurn() {
        turnPID.disable();
    }

    private double limitdrive(double joy, double last) {
        double limit = .06;
        double change = joy - last;
        if (change > limit) {
            change = limit;
        } else if (change < -limit) {
            change = -limit;
        }
        double newlimit = last + change;
        return newlimit;
    }

    public void drive(double left, double right) {
        drive.tankDrive(left, right);
    }

    public void drive(Joystick left, Joystick right, double sense) {
        if (left.getZ() < 0) {
            drive.tankDrive(left.getY() * sense, right.getY() * sense);
        } else {
            // TODO scale drive so full power can't be hit.
            limitleft = limitdrive(left.getY() / 1.5, limitleft);
            limitright = limitdrive(right.getY() / 1.5, limitright);
            drive.tankDrive(limitleft * sense, limitright * sense);
        }
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Drive(RobotMap.DRIVE_FORWARD));
    }

    public void updateDash() {

        SmartDashboard.putDouble("Gyro", RobotMap.roundtoTwo(gyro.getAngle()));

        SmartDashboard.putBoolean("T_PID_en", turnPID.isEnable());
        SmartDashboard.putDouble("T_PID_error",
                RobotMap.roundtoTwo(turnPID.getError()));
        SmartDashboard.putBoolean("T_PID_ontarget", turnPID.onTarget());


    }
}
