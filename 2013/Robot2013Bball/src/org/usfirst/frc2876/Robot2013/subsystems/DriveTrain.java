// Generated with RobotBuilder version 0.0.1
package org.usfirst.frc2876.Robot2013.subsystems;

import org.usfirst.frc2876.Robot2013.RobotMap;
import org.usfirst.frc2876.Robot2013.commands.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrain extends Subsystem {
    // The following variables are automatically assigned by
    // robotbuilder and will be updated the next time you export to
    // Java from robot builder. Do not put any code or make any change
    // in the following block or it will be lost on an update. To
    // prevent this subsystem from being automatically updated, delete
    // the following line.
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    RobotDrive robotDrive2 = RobotMap.DRIVETRAIN_ROBOT_DRIVE_2;
    Jaguar rightDriveJaguar = RobotMap.DRIVETRAIN_RIGHTDRIVE_JAGUAR;
    Jaguar leftDriveJaguar = RobotMap.DRIVETRAIN_LEFTDRIVE_JAGUAR;
    public Encoder rightEncoder = RobotMap.DRIVETRAIN_RIGHTENCODER;
    public Encoder leftEncoder = RobotMap.DRIVETRAIN_LEFTENCODER;
    Gyro gyro = RobotMap.DRIVETRAIN_GYRO;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static final int DRIVE_ENCODER_MIN_RATE = 10;
    public static final int DRIVE_ENCODER_MIN_PERIOD = 10;
    public static final double DRIVE_WHEEL_RADIUS = 3.7;
    public static final int PULSE_PER_ROTATION = 360;
    public static final double GEAR_RATIO = 42 / 39;
    public static final double DRIVE_ENCODER_PULSE_PER_ROT =
            PULSE_PER_ROTATION * GEAR_RATIO;
    public static final double DRIVE_ENCODER_DIST_PER_TICK =
            ((Math.PI * 2 * DRIVE_WHEEL_RADIUS) / DRIVE_ENCODER_PULSE_PER_ROT);
    // Turn PID controller variables
    private static final double turnKp = 0.100;
    private static final double turnKi = 0.000;
    private static final double turnKd = 0.200;
    private static final double turnKf = 0.000;
    PIDController turnPID;
    // If this is set to false turn PID won't make the robot turn.
    // It will just calculate error and come up with the output value
    // that should be sent to jaguars to turn.
    boolean turnPIDOutputEnabled = false;
    // Distance PID controller variables
    private static final double dKp = 0.150;
    private static final double dKi = 0.000;
    private static final double dKd = 0.100;
    private static final double dKf = 0.000;
    PIDController dPID;
    // If this is set to false distance PID won't make the robot move.
    // It will just calculate error and come up with the output value
    // that should be sent to jaguars to drive.
    boolean dPIDOutputEnabled = false;
    Preferences prefs;

    public DriveTrain() {

        rightEncoder.setMaxPeriod(DRIVE_ENCODER_MIN_PERIOD);
        rightEncoder.setMinRate(DRIVE_ENCODER_MIN_RATE);

        leftEncoder.setMaxPeriod(DRIVE_ENCODER_MIN_PERIOD);
        leftEncoder.setMinRate(DRIVE_ENCODER_MIN_RATE);

        leftEncoder.setDistancePerPulse(DRIVE_ENCODER_DIST_PER_TICK);
        rightEncoder.setDistancePerPulse(DRIVE_ENCODER_DIST_PER_TICK);

        startEncoder(leftEncoder);
        startEncoder(rightEncoder);

        initDPID();
        initTurnPID();
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void initDefaultCommand() {
        // The following variables are automatically assigned by
        // robotbuilder and will be updated the next time you export to
        // Java from robot builder. Do not put any code or make any change
        // in the following block or it will be lost on an update. To
        // prevent this subsystem from being automatically updated, delete
        // the following line.
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        setDefaultCommand(new Drive());
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void resetSensors() {
        System.out.println("gyro " + gyro.getAngle());
        gyro.reset();
        leftEncoder.reset();
        rightEncoder.reset();
        turnPID.reset();
        dPID.reset();
    }

    void updateTurnPID() {
        prefs = Preferences.getInstance();
        double kp = prefs.getDouble("tkp", turnKp);
        double ki = prefs.getDouble("tki", turnKi);
        double kd = prefs.getDouble("tkd", turnKd);
        turnPID.setPID(kp, ki, kd);
    }

    void updatedPID() {
        prefs = Preferences.getInstance();
        double kp = prefs.getDouble("dkp", dKp);
        double ki = prefs.getDouble("dki", dKi);
        double kd = prefs.getDouble("dkd", dKd);
        turnPID.setPID(kp, ki, kd);
    }

    private void initTurnPID() {
        turnPID = new PIDController(turnKp, turnKi, turnKd, turnKf, gyro, new PIDOutput() {
            public void pidWrite(double output) {
                if (turnPIDOutputEnabled) {
                    robotDrive2.tankDrive(output, -output);
                    SmartDashboard.putNumber("turnPID L output", output);
                    SmartDashboard.putNumber("turnPID R output", -output);
                }
                SmartDashboard.putNumber("turnPID Error", turnPID.getError());
                SmartDashboard.putBoolean("turnPID ontarget", turnPID.onTarget());
            }
        });

        turnPID.setOutputRange(-0.8, 0.8);
        turnPID.setPercentTolerance(5);
        LiveWindow.addActuator("DriveTrain", "turnPID", turnPID);
    }

    public void setTurn(double degrees) {
        turnPIDOutputEnabled = true;
        dPIDOutputEnabled = false;
        gyro.reset();
        updateTurnPID();
        turnPID.reset();
        turnPID.setSetpoint(degrees);

        turnPID.enable();

        SmartDashboard.putNumber("turnPID setpoint", turnPID.getSetpoint());
        System.out.println("tkp " + turnPID.getP()
                + " tki " + turnPID.getI() + " tkd " + turnPID.getD());
    }

    public boolean isTurnDone() {
//        System.out.println("is turn done: " + turnPID.onTarget()
//                + " deg:" + RobotMap.roundtoTwo(gyro.getAngle())
//                + " out:" + RobotMap.roundtoTwo(turnPID.get())
//                + " err:" + RobotMap.roundtoTwo(turnPID.getError()));
        SmartDashboard.putNumber("turnPID Error", turnPID.getError());
        SmartDashboard.putBoolean("turnPID ontarget", turnPID.onTarget());
        //return turnPID.onTarget();
        return (Math.abs(turnPID.getError()) < 8);
    }

    public void endTurn() {
        turnPID.reset();
        turnPIDOutputEnabled = false;
        robotDrive2.tankDrive(0, 0);
        SmartDashboard.putNumber("turnPID Error", turnPID.getError());
        SmartDashboard.putBoolean("is turnPID ontarget", turnPID.onTarget());
        //updateDashboard();
    }

    public void drive(Joystick left, Joystick right) {
        robotDrive2.tankDrive(left.getY(), right.getY());
        SmartDashboard.putNumber("leftEnc", leftEncoder.getDistance());
        SmartDashboard.putNumber("rightEnc", rightEncoder.getDistance());
        SmartDashboard.putNumber("jagL", leftDriveJaguar.get());
        SmartDashboard.putNumber("jagR", rightDriveJaguar.get());
    }

    public void driveSmooth(Joystick left, Joystick right) {
        robotDrive2.tankDrive(left, right, true);
    }

    public void startEncoder(Encoder encoder) {
        encoder.reset();
        encoder.start();
    }

    public void stopEncoder(Encoder encoder) {
        encoder.stop();
    }

    public double getLeftEncoder() {
        return leftEncoder.getRaw();
    }

    public double getRightEncoder() {
        return rightEncoder.getRaw();
    }

    public double getLeftEncoderDistance() {
        return leftEncoder.getDistance();
    }

    public double getRightEncoderDistance() {
        return rightEncoder.getDistance();
    }

    // ============================================================
    // Since we have two encoders, left, right we need a custom class
    // to act as a PID input source. This one will take the average
    // of the left and right encoder.
    private class AvgEncoder implements PIDSource {

        public double pidGet() {
            double r = rightEncoder.getDistance();
            double l = leftEncoder.getDistance();
            //double avg = (r + l) / 2;
            // Since only one encoder is working using avg throws 
            // off distance alot. Just use working one for now.
            double avg = r;
            SmartDashboard.putNumber("AvgEnc", avg);
            return avg;
        }
    }

    private void initDPID() {
        // Encoders can measure distance or rate of rotation.
        // We want distance from the drive train encoders so need
        // to configure encoders to give us distance when using PID.
        rightEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
        leftEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
        dPID = new PIDController(dKp, dKi, dKd, dKf,
                new AvgEncoder(), new PIDOutput() {
            public void pidWrite(double output) {
                if (dPIDOutputEnabled) {
                    robotDrive2.tankDrive(output, output);
                }
            }
        });
        // limit the output range of distance PID so there is
        // room to correct for turning.  If the robot is driving
        // 10 ft and starts to drift left, we need to be able to turn
        // it right. If the outputs are at 1,1 for jaguars it is hard
        // to use the turnPID to correct the drift.
        //dPID.setOutputRange(-.8, .8);
        dPID.setOutputRange(-.5, .5);
        dPID.setPercentTolerance(5);

        LiveWindow.addActuator("DriveTrain", "dPID", dPID);
    }

    // Call this func from initialize in a command
    public void setDriveDistance(double inches) {
        turnPIDOutputEnabled = false;
        dPIDOutputEnabled = true;
        leftEncoder.reset();
        rightEncoder.reset();
        leftEncoder.start();
        rightEncoder.start();
        dPID.reset();
        updatedPID();
        dPID.setSetpoint(inches);
        dPID.enable();
        SmartDashboard.putNumber("dPID setpoint", dPID.getSetpoint());
        SmartDashboard.putNumber("turnPID setpoint", turnPID.getSetpoint());
        System.out.println("dkp " + dPID.getP()
                + " dki " + dPID.getI() + " dkd " + dPID.getD());
    }

    // Call this func from initialize in a command
    public void setDriveDistanceStraight(double inches) {
        turnPIDOutputEnabled = false;
        dPIDOutputEnabled = false;
        //
        leftEncoder.reset();
        rightEncoder.reset();
        leftEncoder.start();
        rightEncoder.start();
        dPID.reset();
        dPID.setSetpoint(inches);
        updatedPID();
        //
        gyro.reset();
        updateTurnPID();
        turnPID.reset();
        turnPID.setSetpoint(0);
        //
        turnPID.enable();
        dPID.enable();
        //
        SmartDashboard.putNumber("dPID setpoint", dPID.getSetpoint());
        SmartDashboard.putNumber("turnPID setpoint", turnPID.getSetpoint());
        System.out.println("dkp " + dPID.getP()
                + " dki " + dPID.getI() + " dkd " + dPID.getD());
    }

    // Call this func from execute in a command
    public void driveDistanceStraight() {
        double dOutput = dPID.get();
        double turn = turnPID.get();
        // Which one of these drive methods will work better?
        // What happens if dOutput + turn > 1 or dOutput - turn < -1?
        robotDrive2.arcadeDrive(dOutput, turn);
        //robotDrive2.tankDrive(dOutput - turn, dOutput + turn);
        //robotDrive2.tankDrive(dOutput, dOutput);
    }

    // Call this func from isFinished in a command
    public boolean isDistanceDone() {
        SmartDashboard.putNumber("dPID Error", dPID.getError());
        SmartDashboard.putBoolean("dPID ontarget", dPID.onTarget());
        //return dPID.onTarget();
        return (Math.abs(dPID.getError()) < 10);
    }

    public void endDistance() {
        dPIDOutputEnabled = false;
        dPID.reset();
        robotDrive2.tankDrive(0, 0);
        SmartDashboard.putNumber("dPID Error", dPID.getError());
        SmartDashboard.putBoolean("dPID ontarget", dPID.onTarget());
        //updateDashboard();
    }

    public void updateDashboard() {
        SmartDashboard.putNumber("jagL", leftDriveJaguar.get());
        SmartDashboard.putNumber("jagR", rightDriveJaguar.get());
        SmartDashboard.putNumber("leftEnc", leftEncoder.getDistance());
        SmartDashboard.putNumber("rightEnc", rightEncoder.getDistance());
        SmartDashboard.putNumber("dPID Output", dPID.get());
        //
        SmartDashboard.putNumber("turnPID Output", turnPID.get());
        SmartDashboard.putNumber("gyro pidGet", gyro.pidGet());
        //
        SmartDashboard.putBoolean("turnPID enabled", turnPID.isEnable());
        SmartDashboard.putBoolean("dPID enabled", dPID.isEnable());
    }
}
