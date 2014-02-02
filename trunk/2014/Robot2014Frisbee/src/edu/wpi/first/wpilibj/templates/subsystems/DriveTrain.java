/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
    Talon rightDriveTalon = RobotMap.DRIVETRAIN_RIGHTDRIVE_TALON;
    Talon leftDriveTalon = RobotMap.DRIVETRAIN_LEFTDRIVE_TALON;
    public Encoder leftEncoder = RobotMap.DRIVETRAIN_LEFTENCODER;
    public Encoder rightEncoder = RobotMap.DRIVETRAIN_RIGHTENCODER;
    public AnalogChannel frontSonar = RobotMap.DRIVETRAIN_FRONTSONAR;
    public AnalogChannel sideSonar = RobotMap.DRIVETRAIN_SIDESONAR;
    public Gyro gyro = RobotMap.DRIVETRAIN_GYRO;
    
    

    private static final double encoderKp = 1.00;
    private static final double encoderKi = 0.000;
    private static final double encoderKd = 0.000;
    private static final double encoderKf = 0.000;
    public PIDController encoderPID;
    Preferences encoderPrefs;
    
    private static final double sideKp = 0.3500;
    private static final double sideKi = 0.000;
    private static final double sideKd = 0.100;
    private static final double sideKf = 0.000;
    edu.wpi.first.wpilibj.PIDController sideSonarPID;
    Preferences sideSonarPrefs;

    private static final int DRIVE_ENCODER_MIN_RATE = 10;
    private static final int DRIVE_ENCODER_MIN_PERIOD = 10;
    private static final double DRIVE_WHEEL_RADIUS = 2.25;
    private static final int PULSE_PER_ROTATION = 360;
    private static final double GEAR_RATIO = 42 / 39;
    private static final double DRIVE_ENCODER_PULSE_PER_ROT = PULSE_PER_ROTATION * GEAR_RATIO;
    private static final double DRIVE_ENCODER_DIST_PER_TICK
            = ((Math.PI * 2 * DRIVE_WHEEL_RADIUS) / DRIVE_ENCODER_PULSE_PER_ROT);


    public DriveTrain() {
        encoderSetup();
        LiveWindow.addSensor("Drive Train", "Gyro", gyro);
        LiveWindow.addSensor("Drive Train", "Left Encoder", leftEncoder);
        LiveWindow.addSensor("Drive Train", "Right Encoder", rightEncoder);
        
        initEncoderPID();
        initSideSonarPID();
    }

    //-------------------------GENERAL-------------------------//
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

        robotDrive2.arcadeDrive(move, rotate);

        //robotDrive2.arcadeDrive(move, rotate);
    }
    
    //-------------------------ENCODERS------------------------//
       private void encoderSetup() {
        rightEncoder.setMaxPeriod(DRIVE_ENCODER_MIN_PERIOD);
        rightEncoder.setMinRate(DRIVE_ENCODER_MIN_RATE);
        rightEncoder.setDistancePerPulse(DRIVE_ENCODER_DIST_PER_TICK);

        leftEncoder.setMaxPeriod(DRIVE_ENCODER_MIN_PERIOD);
        leftEncoder.setMinRate(DRIVE_ENCODER_MIN_RATE);
        leftEncoder.setDistancePerPulse(DRIVE_ENCODER_DIST_PER_TICK);

        startEncoder();
    }
    
    public void startEncoder() {
        leftEncoder.start();
        rightEncoder.start();
    }

    public void resetEncoder() {
        leftEncoder.start();
        rightEncoder.start();
    }
    
    //-----------------------ENCODER PID-----------------------//
    private class DiffEncoderInput implements PIDSource {
        public double pidGet() {
            double r = rightEncoder.getDistance();
            double l = leftEncoder.getDistance();
            //double avg = (r + l) / 2;
            // Since only one encoder is working using avg throws 
            // off distance alot. Just use working one for now.
            double diff = l - r;
            SmartDashboard.putNumber("Difference", diff);
            return diff;
        }
    }
    
    private class EncoderOutput implements PIDOutput{
        public void pidWrite(double output) {
                double base = .8;
                SmartDashboard.putNumber("pid output", output);
                SmartDashboard.putData("gyro",gyro);
                SmartDashboard.putData("leftenc", leftEncoder);
                SmartDashboard.putData("rightenc", rightEncoder);
                //robotDrive2.tankDrive(base + output, base + output);
                if (leftEncoder.getDistance() - rightEncoder.getDistance() > 0) {  //veering right
                    robotDrive2.tankDrive(base - output, base + output);
                    //rightDriveTalon.set(output);
                    //if (rightDriveTalon.get() + output < 1)
                    //    rightDriveTalon.set(rightDriveTalon.get() + output);
                    //else 
                    //    leftDriveTalon.set(leftDriveTalon.get()-output);
                } else {  //veering left
                    robotDrive2.tankDrive(base + output, base - output);
                    //leftDriveTalon.set(output);
                    //if (leftDriveTalon.get() + output < 1)
                    //    leftDriveTalon.set(leftDriveTalon.get() + output);
                    //else 
                    //    rightDriveTalon.set(rightDriveTalon.get()-output);
                }
            }
        }
    
    private void initEncoderPID(){
        encoderPID = new PIDController(1.0, 0, 0, new DiffEncoderInput(), new EncoderOutput());

        encoderPID.setOutputRange(-.2, .2);
        encoderPID.setPercentTolerance(5);

        LiveWindow.addActuator("DriveTrain", "encoderPID", encoderPID);
    }

    void updateEncoderPID() {
        encoderPrefs = Preferences.getInstance();
        double kp = encoderPrefs.getDouble("encoderkp", encoderKp);
        double ki = encoderPrefs.getDouble("encoderki", encoderKi);
        double kd = encoderPrefs.getDouble("encoderkd", encoderKd);
        encoderPID.setPID(kp, ki, kd);
    }

    //--------------------ENCODER AUTONOMOUS--------------------//
    public void startEncoderAutonomous() {//sets setpoint to 0 and resets everything
        resetEncoder();
        startEncoder();
        gyro.reset();
//        encoderPID.reset();
        updateEncoderPID();
        encoderPID.setSetpoint(0);
        encoderPID.enable();
    }

    public boolean encoderAutonomousOnTarget(double target) {
        if (Math.abs((leftEncoder.getDistance() + rightEncoder.getDistance()) / 2) > getTrueEncoderDistance(target)) {
            return true;
        }else{
            return false;
        }
    }

    public void endEncoderAutonomous() {
        encoderPID.reset();
        encoderPID.disable();
        robotDrive2.tankDrive(0, 0);
    }

    //Returns the distance in encoder-speak when we input number of inches
    public double getTrueEncoderDistance(double inches) {
        double distance;
        distance = inches / 1.398; //1.398 is our 100% accurate conversion factor based on hours of calculation
        return distance;
    }
    
    //--------------------------SONAR--------------------------//
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
  
    //------------------------SONAR PID------------------------//
    private class SonarInput implements PIDSource {
        public double pidGet() {
            return getSideSonarDist();
        }
    }
    
    private class SonarOutput implements PIDOutput {
        double baseSpeed=0.60;
        public void pidWrite(double output) {
//            if (now - last > .5) {
//                System.out.println("l: " + (baseSpeed + output) + " r: " + (baseSpeed - output));
//            }
            robotDrive2.tankDrive(-(baseSpeed+output), -(baseSpeed-output)); //wall on right
            //robotDrive2.tankDrive(baseSpeed-output, baseSpeed+output); //wall on left
        }

    }

    private void initSideSonarPID() {
        rightEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
        leftEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
        sideSonarPID = new edu.wpi.first.wpilibj.PIDController(sideKp, sideKi, sideKd, sideKf,
                new SonarInput(), new SonarOutput());
        // limit the output range of distance PID so there is
        // room to correct for turning.  If the robot is driving
        // 10 ft and starts to drift left, we need to be able to turn
        // it right. If the outputs are at 1,1 for jaguars it is hard
        // to use the turnPID to correct the drift.
        sideSonarPID.setOutputRange(-.15, .15);
        sideSonarPID.setPercentTolerance(5);

        //LiveWindow.addActuator("DriveTrain", "sidePID", sidePID);
    }

    void updateSideSonarPID() {
        sideSonarPrefs = Preferences.getInstance();
        double kp = sideSonarPrefs.getDouble("sidekp", sideKp);
        double ki = sideSonarPrefs.getDouble("sideki", sideKi);
        double kd = sideSonarPrefs.getDouble("sidekd", sideKd);
        sideSonarPID.setPID(kp, ki, kd);
    }

    public void setSideSonarDistance(double inches) {
        sideSonarPID.reset();
        updateSideSonarPID();
        sideSonarPID.setSetpoint(inches);
        sideSonarPID.enable();
        SmartDashboard.putNumber("sidePID setpoint", sideSonarPID.getSetpoint());
        System.out.println("sidekp " + sideSonarPID.getP() + " sideki " + sideSonarPID.getI() + " sidekd " + sideSonarPID.getD());
    }
    
    //-------------------SONAR AUTONOMOUS-------------------//
    double now = Timer.getFPGATimestamp();
    double last = Timer.getFPGATimestamp();
    
    public void driveSideSonarAutonomous() {
        now = Timer.getFPGATimestamp();
        if (now - last > .2) {
            System.out.println("SidePID out: " + sideSonarPID.get() + " input: " + sideSonar.pidGet() + " error: " + sideSonarPID.getError()
                    + " l: " + leftDriveTalon.get() + " r: " + rightDriveTalon.get());
            System.out.println("Front Sonar: "+getFrontSonarDist()+"Side Sonar: "+getSideSonarDist());
            last = now;
        }

    }
}
