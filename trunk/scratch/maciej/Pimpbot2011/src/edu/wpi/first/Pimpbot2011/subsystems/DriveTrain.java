/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.Pimpbot2011.subsystems;

import edu.wpi.first.Pimpbot2011.RobotMap;
import edu.wpi.first.Pimpbot2011.commands.DriveWithJoystick;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableGyro;
import edu.wpi.first.wpilibj.smartdashboard.SendablePIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author maciej
 */
public class DriveTrain extends Subsystem {

    private static final double turnKp = 1.0;
    private static final double turnKi = 0.0;
    private static final double turnKd = 0.0;
    private static final double distKp = 0.0;
    private static final double distKi = 0.0;
    private static final double distKd = 0.0;
    private RobotDrive drive;
    AnalogChannel ultrasonic;
    SendableGyro gyro;
    SendablePIDController turnPID;
    SendablePIDController distPID;
    Jaguar leftJag = new Jaguar(RobotMap.DRIVE_MOTOR_LEFT);
    Jaguar rightJag = new Jaguar(RobotMap.DRIVE_MOTOR_RIGHT);

    public DriveTrain() {
        super("DriveTrain");

        drive = new RobotDrive(leftJag, rightJag);
        drive.setSafetyEnabled(false);

        ultrasonic = new AnalogChannel(RobotMap.ULTRASONIC);

        gyro = new SendableGyro(RobotMap.GYRO_ANGLE);
        SmartDashboard.putData("GYRO", gyro);

        distPID = initDistancePID();

        initTurnPID();
    }

    private SendablePIDController initDistancePID() {
        SendablePIDController pid =
                new SendablePIDController(distKp, distKi, distKd, ultrasonic,
                new PIDOutput() {

                    public void pidWrite(double output) {
                        //tankDrive(output, -output);
                    }
                });
        pid.setTolerance(10.0);
        pid.setInputRange(6, 254);
        pid.setOutputRange(-.8, .8);
        SmartDashboard.putData("drive PID", pid);

        return pid;
    }

    private void initTurnPID() {
        turnPID =
                new SendablePIDController(turnKp, turnKi, turnKd, gyro,
                new PIDOutput() {

                    public void pidWrite(double output) {
                        tankDrive(-output, output);
                        SmartDashboard.putDouble("left pwm", RobotMap.roundtoTwo(-output));
                        SmartDashboard.putDouble("right pwm", RobotMap.roundtoTwo(output));
                        SmartDashboard.putDouble("turn", RobotMap.roundtoTwo(gyro.getAngle()));
                        SmartDashboard.putDouble("turn error", RobotMap.roundtoTwo(turnPID.getError()));

                    }
                });
        turnPID.setTolerance(2.0);
        turnPID.setInputRange(-90, 90);
        turnPID.setOutputRange(-.55, .55);
        SmartDashboard.putData("turn PID", turnPID);
        // affects turning, .5 is default set in RobotDrive class.
        //drive.setSensitivity(.5);

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveWithJoystick());
    }

    private double voltToIn(double volt) {
        // each .01 v = 1 inch so mult volt by 100 to get inches
        return volt * 100;
    }

    public void tankDrive(double left, double right) {
        drive.tankDrive(left, right);
        SmartDashboard.putDouble("left pwm", left);
        SmartDashboard.putDouble("right pwm", right);
    }

    public void autoDrive() {
        double speed = 0;
        if (distPID.isEnable()) {
            speed = distPID.get();
        }
        double turn = 0;
        if (turnPID.isEnable()) {
            turn = turnPID.get();
        }
        if (speed > 0) {
            drive.drive(speed, turn);
        } else {
            if (turn > 0) {
                // turn right
                tankDrive(turn, -turn);
            } else if (turn < 0) {
                tankDrive(-turn, turn);
            }
        }
        SmartDashboard.putDouble("left pwm", leftJag.get());
        SmartDashboard.putDouble("right pwm", rightJag.get());
        SmartDashboard.putDouble("speed", RobotMap.roundtoTwo(speed));
        SmartDashboard.putDouble("turn", RobotMap.roundtoTwo(turn));
        SmartDashboard.putDouble("turn error", turnPID.getError());
        // System.out.println("driving " + speed + " " + turn);
    }

    public void turnStart(double degrees) {
        turnPID.reset();
        gyro.reset();
        turnPID.setSetpoint(degrees);
        turnPID.enable();
        Timer.delay(.1);
        System.out.println("start turning from "
                + RobotMap.roundtoTwo(gyro.getAngle())
                + " to setpoint " + degrees);
    }

    public void turnStop() {
        turnPID.disable();
        System.out.println("stop turning at "
                + RobotMap.roundtoTwo(gyro.getAngle()));
    }

    public void setTurnSetpoint(double degrees) {
        turnPID.setSetpoint(degrees);
    }

    public boolean isTurnDone() {
        System.out.println("is turn done " + turnPID.onTarget()
                + " deg=" + RobotMap.roundtoTwo(gyro.getAngle())
                + " out=" + RobotMap.roundtoTwo(turnPID.get())
                + " err=" + RobotMap.roundtoTwo(turnPID.getError()));
        return turnPID.onTarget();
    }

    public double getAngle() {
        return gyro.getAngle();
    }

    public void distStart(double inches) {
        distPID.setSetpoint(inches);
        distPID.enable();
    }

    public void distStop() {
        distPID.disable();
    }

    public void setDistanceSetpoint(double inches) {
        distPID.setSetpoint(inches);
    }

    public boolean isDistDone() {
        return distPID.onTarget();
    }

    public double getDistance() {
        return voltToIn(ultrasonic.getValue());
    }
}
