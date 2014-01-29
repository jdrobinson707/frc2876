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
 * This program sets up a basic robot, with two motors and encoders. It then
 * drives for five feet during teleop mode.
 *
 * @author: Fredric Silberberg
 */
public class EncoderDrive extends SimpleRobot {
    //*****THIS IS FOR SONAR, NOT ENCODER!!!!!!!!!!!!!!!!!!!!!!!
    
    
    //Initializes the motors
    private final SpeedController left = new Jaguar(3);
    private final SpeedController right = new Jaguar(4);

    //Initializes the encoders
    private final Ultrasonic sonar = new Ultrasonic(1, 1);  //1, 2

    //Initializes the drive
    private RobotDrive drive = new RobotDrive(left, right);

    public EncoderDrive() {

        //Sets distance in inches
        //This was obtained by finding the circumfrance of the wheels,
        //finding the number of encoder pulses per rotation,
        //and diving the circumfrance by that number.
        //leftEncoder.setDistancePerPulse(.000623);
        //rightEncoder.setDistancePerPulse(.000623);
        int DRIVE_ENCODER_MIN_RATE = 10;
        int DRIVE_ENCODER_MIN_PERIOD = 10;
        double DRIVE_WHEEL_RADIUS = 3.7;
        int PULSE_PER_ROTATION = 360;
        double GEAR_RATIO = 42 / 39;
        double DRIVE_ENCODER_PULSE_PER_ROT
                = PULSE_PER_ROTATION * GEAR_RATIO;
        double DRIVE_ENCODER_DIST_PER_TICK
                = ((Math.PI * 2 * DRIVE_WHEEL_RADIUS) / DRIVE_ENCODER_PULSE_PER_ROT);
        //leftEncoder.setDistancePerPulse(DRIVE_ENCODER_DIST_PER_TICK);
        //rightEncoder.setDistancePerPulse(DRIVE_ENCODER_DIST_PER_TICK);
        System.out.println("distancerPerPulse " + DRIVE_ENCODER_DIST_PER_TICK);
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
                System.out.println("Sonar Distance: "+sonar.getRangeInches());
                last = now;
            }
                    
        }
    }

//    public void DriveDistance() {
//        //Starts the encoders
//        leftEncoder.start();
//        rightEncoder.start();
//        //Drives until the average of the encoders is 60 inches, or five feet
//        while ((60 - ((leftEncoder.getDistance() + rightEncoder.getDistance()) / 2)) > 0) {
//            drive.tankDrive(1, 1);
//        }
//        drive.tankDrive(0, 0);
//        System.out.println("Ldist " + leftEncoder.getDistance()
//                + " Rdist " + rightEncoder.getDistance());
//
//    }
//
//    // ============================================================
//    // Since we have two encoders, left, right we need a custom class
//    // to act as a PID input source. This one will take the average
//    // of the left and right encoder.
//    private class AvgEncoder implements PIDSource {
//
//        public double pidGet() {
//            double r = rightEncoder.getDistance();
//            double l = leftEncoder.getDistance();
//            //double avg = (r + l) / 2;
//            // Since only one encoder is working using avg throws 
//            // off distance alot. Just use working one for now.
//            double avg = r;
//            //SmartDashboard.putNumber("AvgEnc", avg);
//            return avg;
//        }
//    }
//
//    public void DriveDistancePID() {
//        // Distance PID controller variables
//        double dKp = 0.150;
//        double dKi = 0.000;
//        double dKd = 0.100;
//        double dKf = 0.000;
//        PIDController dPID;
//
//        // Encoders can measure distance or rate of rotation.
//        // We want distance from the drive train encoders so need
//        // to configure encoders to give us distance when using PID.
//        rightEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
//        leftEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
//        dPID = new PIDController(dKp, dKi, dKd, dKf,
//                new AvgEncoder(), new PIDOutput() {
//                    public void pidWrite(double output) {
//                        drive.tankDrive(output, output);
//                    }
//                });
//        // limit the output range of distance PID so there is
//        // room to correct for turning.  If the robot is driving
//        // 10 ft and starts to drift left, we need to be able to turn
//        // it right. If the outputs are at 1,1 for jaguars it is hard
//        // to use the turnPID to correct the drift.
//        //dPID.setOutputRange(-.8, .8);
//        //dPID.setOutputRange(-.5, .5);
//        dPID.setPercentTolerance(5);
//
//        leftEncoder.reset();
//        rightEncoder.reset();
//        leftEncoder.start();
//        rightEncoder.start();
//
//        dPID.reset();
//        dPID.enable();
//
//        dPID.setSetpoint(36);
//        System.out.println("setpoint" + dPID.getSetpoint()
//                + "dkp " + dPID.getP() + " dki " + dPID.getI() + " dkd " + dPID.getD());
//
//        double last = Timer.getFPGATimestamp();
//        while (isOperatorControl() && isEnabled()) {
//            double now = Timer.getFPGATimestamp();
//            if (now - last > 1000) {
//                System.out.println("onTarget=" + dPID.onTarget() + " err=" + dPID.getError());
//                last = now;
//            }
//            //System.out.println("onTarget=" + dPID.onTarget() + " err=" + dPID.getError());
//        }
//    }

}
