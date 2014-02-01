/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    public AnalogChannel frontSonar = RobotMap.DRIVETRAIN_FRONTSONAR;
    public AnalogChannel sideSonar = RobotMap.DRIVETRAIN_SIDESONAR;
    public Gyro gyro = RobotMap.DRIVETRAIN_GYRO;

    private static final int DRIVE_ENCODER_MIN_RATE = 10;
    private static final int DRIVE_ENCODER_MIN_PERIOD = 10;
    private static final double DRIVE_WHEEL_RADIUS = 3.7;
    private static final int PULSE_PER_ROTATION = 360;
    private static final double GEAR_RATIO = 42 / 39;
    private static final double DRIVE_ENCODER_PULSE_PER_ROT = PULSE_PER_ROTATION * GEAR_RATIO;
    private static final double DRIVE_ENCODER_DIST_PER_TICK
            = ((Math.PI * 2 * DRIVE_WHEEL_RADIUS) / DRIVE_ENCODER_PULSE_PER_ROT);

    private static final double sideKp = 0.3500;
    private static final double sideKi = 0.000;
    private static final double sideKd = 0.100;
    private static final double sideKf = 0.000;
    edu.wpi.first.wpilibj.PIDController sidePID;
    Preferences prefs;

    public DriveTrain() {
        encoderSetup();
        initSidePID();
    }

    private void encoderSetup() {
        rightEncoder.setMaxPeriod(DRIVE_ENCODER_MIN_PERIOD);
        rightEncoder.setMinRate(DRIVE_ENCODER_MIN_RATE);
        rightEncoder.setDistancePerPulse(DRIVE_ENCODER_DIST_PER_TICK);

        leftEncoder.setMaxPeriod(DRIVE_ENCODER_MIN_PERIOD);
        leftEncoder.setMinRate(DRIVE_ENCODER_MIN_RATE);
        leftEncoder.setDistancePerPulse(DRIVE_ENCODER_DIST_PER_TICK);

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
        //robotDrive2.arcadeDrive(move, rotate);
    }

    public void startEncoder() {
        leftEncoder.start();
        rightEncoder.start();
    }

    public void resetEncoder() {
        leftEncoder.start();
        rightEncoder.start();
    }

    public double getFrontSonarDist() {
        double scalingFactor = 0.009766;
        double inches = frontSonar.getVoltage() / scalingFactor;
        return inches;
    }

    public double getSideSonarDist() {
        double scalingFactor = 0.009766;
        double inches = sideSonar.getVoltage() / scalingFactor;
        return inches;
    }

    private class SonarOuput implements PIDOutput {
        double baseSpeed=0.60;
        public void pidWrite(double output) {
//            if (now - last > .5) {
//                System.out.println("l: " + (baseSpeed + output) + " r: " + (baseSpeed - output));
//            }
            robotDrive2.tankDrive(-(baseSpeed+output), -(baseSpeed-output)); //wall on right
            //robotDrive2.tankDrive(baseSpeed-output, baseSpeed+output); //wall on left
        }
    }
    
    private class SonarInput implements PIDSource {
        public double pidGet() {
            return getSideSonarDist();
        }
    }

    private void initSidePID() {
        
        // Encoders can measure distance or rate of rotation.
        // We want distance from the drive train encoders so need
        // to configure encoders to give us distance when using PID.
        rightEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
        leftEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
        sidePID = new edu.wpi.first.wpilibj.PIDController(sideKp, sideKi, sideKd, sideKf,
                new SonarInput(), new SonarOuput());
        // limit the output range of distance PID so there is
        // room to correct for turning.  If the robot is driving
        // 10 ft and starts to drift left, we need to be able to turn
        // it right. If the outputs are at 1,1 for jaguars it is hard
        // to use the turnPID to correct the drift.
        sidePID.setOutputRange(-.15, .15);
        sidePID.setPercentTolerance(5);

        //LiveWindow.addActuator("DriveTrain", "sidePID", sidePID);
    }

    void updateSidePID() {
        prefs = Preferences.getInstance();
        double kp = prefs.getDouble("sidekp", sideKp);
        double ki = prefs.getDouble("sideki", sideKi);
        double kd = prefs.getDouble("sidekd", sideKd);
        sidePID.setPID(kp, ki, kd);
    }

    public void setSideDistance(double inches) {
        leftEncoder.reset();
        rightEncoder.reset();
        leftEncoder.start();
        rightEncoder.start();
        sidePID.reset();
        updateSidePID();
        sidePID.setSetpoint(inches);
        sidePID.enable();
        SmartDashboard.putNumber("sidePID setpoint", sidePID.getSetpoint());
        System.out.println("sidekp " + sidePID.getP() + " sideki " + sidePID.getI() + " sidekd " + sidePID.getD());
    }
    double now = Timer.getFPGATimestamp();
    double last = Timer.getFPGATimestamp();

    private double roundTwo(double in) {
        double rd=0;
        return rd;
    }
    public void driveSideAutonomous() {
        now = Timer.getFPGATimestamp();
        if (now - last > .2) {
//            double output = sidePID.get();
            //robotDrive2.arcadeDrive(output, output);
            System.out.println("SidePID out: " + sidePID.get() + " input: " + sideSonar.pidGet() + " error: " + sidePID.getError()
                    + " l: " + leftDriveJaguar.get() + " r: " + rightDriveJaguar.get());
            System.out.println("Front Sonar: "+getFrontSonarDist()+"Side Sonar: "+getSideSonarDist());
            last = now;
        }
    }
}
