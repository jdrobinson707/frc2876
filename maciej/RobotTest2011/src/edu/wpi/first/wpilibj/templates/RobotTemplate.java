/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;


import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
interface Constants {

    public static final int UNINITIALIZED_DRIVE = 0;
    public static final int ARCADE_DRIVE = 1;
    public static final int TANK_DRIVE = 2;
    // joysticks
    public static final int JOYSTICK_LEFT = 1;
    public static final int JOYSTICK_RIGHT = 2;
    public static final int JOYSTICK_COPILOT = 3;
    // pwms
    public static final int DRIVE_MOTOR_LEFT = 1;
    public static final int DRIVE_MOTOR_RIGHT = 2;
    public static final int ARM_MOTOR = 3;
    // digital io
    public static final int LINE_TRACKER_LEFT = 3;
    public static final int LINE_TRACKER_MIDDLE = 4;
    public static final int LINE_TRACKER_RIGHT = 5;
    // analog channels
    public static final int POT_CHANNEL = 3;
}

public class RobotTemplate extends SimpleRobot {

    RobotDrive drive;
    Joystick stickLeft;
    Joystick stickRight;
    Joystick stickCopilot;

    int driveMode;
    DriverStation ds;
    Encoder leftEncoder;
    Encoder rightEncoder;
    Arm arm;

    public RobotTemplate() {
        driveMode = Constants.TANK_DRIVE;

        stickRight = new Joystick(Constants.JOYSTICK_RIGHT);
        stickLeft = new Joystick(Constants.JOYSTICK_LEFT);
        stickCopilot = new Joystick(Constants.JOYSTICK_COPILOT)
                ;
        drive = new RobotDrive(Constants.DRIVE_MOTOR_LEFT,
                Constants.DRIVE_MOTOR_RIGHT);
        drive.setExpiration(15);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

        leftEncoder = new Encoder(7, 8, false, CounterBase.EncodingType.k1X);
        rightEncoder = new Encoder(9, 10, false, CounterBase.EncodingType.k1X);

        arm = new Arm();

        cameraInit();
    }

    public void cameraInit() {
        AxisCamera.getInstance().writeCompression(0);
        AxisCamera.getInstance().writeBrightness(0);
        AxisCamera.getInstance().writeResolution(AxisCamera.ResolutionT.k320x240);
        AxisCamera.getInstance().writeColorLevel(100);
        AxisCamera.getInstance().writeExposureControl(AxisCamera.ExposureT.flickerfree60);
        AxisCamera.getInstance().writeWhiteBalance(AxisCamera.WhiteBalanceT.fixedFlour1);

    }

    public boolean FindTarget() {
        Tracker tracker = new Tracker();
        AxisCamera.getInstance().freshImage();
        boolean found = false;

        //while (isAutonomous() && isEnabled()){

        try {
            Target target = tracker.getTarget();
            while (target == null) {
                target = tracker.getTarget();
            }

            //calculate the speeds from the camera
            //double yspeed = -1 - target.getYPosition();
            //double xspeed = -target.getXPosition();
            //System.out.println(target + "  x=" + xspeed + " y=" + yspeed);

            /*if (!inYThreshold(target)) {
            drive.arcadeDrive(-.45 * yspeed, xspeed);
            } else if (!inXThreshold(target)) {
            drive.arcadeDrive(0, xspeed);
            } else if (target.getSize() < .2) {
            //double check the value
            AxisCamera.getInstance().freshImage();
            target = tracker.getTarget();

            while (target == null) {
            target = tracker.getTarget();
            }

            if (!inYThreshold(target)) {
            return;
            }

            }*/

            if (inYThreshold(target)) {
                //  drive.arcadeDrive(-.45, 0);
                System.out.println("Size: " + target.getSize() + " In AT TOP ");

            } else if (inXThreshold(target)) {
                //  drive.arcadeDrive(0, 0);
                System.out.println("Size: " + target.getSize() + " IN MIDDLE ");
                found = true;
            } else {

                // drive.arcadeDrive(-.55, 0);
                System.out.println("Size: " + target.getSize() + " NOT EVEN HERE ");
            }


        } catch (AxisCameraException e) {
        } catch (NIVisionException e) {
        }
        //}
        return found;
    }

    /**
     * Is the image at the top most part of the window
     *
     * @param target
     * @return atTargetY
     */
    public boolean inYThreshold(Target target) {
        if (target.getYPosition() < -.9) {
            return true;
        }
        return false;
    }

    /**
     * Is the image at center of the window
     *
     * @param target
     * @return atTargetX
     */
    public boolean inXThreshold(Target target) {
        if (target.getXPosition() < .08
                && target.getXPosition() > -.08) {
            return true;
        }
        return false;
    }

    public void testenc() {
        leftEncoder.reset();
        rightEncoder.reset();
        leftEncoder.start();
        rightEncoder.start();
        leftEncoder.setDistancePerPulse(-0.03202);
        rightEncoder.setDistancePerPulse(0.03202);

        /*while ((leftEncoder.getRaw()) < 3600 && (leftEncoder.getRaw()) > -3600) {
        drive.tankDrive(.6, .6);

        System.out.println(
        " Encoder left: " + leftEncoder.getRaw()
        + "(" + leftEncoder.getDistance() + ")"
        + " Encoder right: " + rightEncoder.getRaw()
        + "(" + rightEncoder.getDistance() + ")");
        }
        drive.tankDrive(0, 0);

        try
        {
        Thread.sleep(4000);

        } catch (InterruptedException ie) {
        System.out.println(ie.getMessage());
        }

        System.out.println(
        " Encoder left: " + leftEncoder.getRaw()
        + "(" + leftEncoder.getDistance() + ")"
        + " Encoder right: " + rightEncoder.getRaw());

        leftEncoder.stop();
        rightEncoder.stop();*/

    }

    public void userInput() {
        double setpoint;
        if (stickLeft.getRawButton(4)) {

            setpoint = 200.0;
        } else if (stickLeft.getRawButton(3)) {

            setpoint = 400.0;
        } else if (stickLeft.getRawButton(5)) {

            setpoint = 600.0;
        }

    }

    public void autonomous() {
        System.out.println("Starting Autonomous");
        while (isAutonomous() && isEnabled()) {
            Timer.delay(.5);

        }
        System.out.println("Ending Autonomous");
    }

    public void operatorControl() {
        System.out.println("In Operator control");
        leftEncoder.reset();
        rightEncoder.reset();
        leftEncoder.start();
        rightEncoder.start();

        while (isOperatorControl() && isEnabled()) {
            Timer.delay(0.30);
            // drive.tankDrive(-stickLeft.getY(), -stickRight.getY());
            drive.arcadeDrive(stickLeft);
            arm.set(stickCopilot.getY());
        }
        System.out.println("Leaving Operator Control");

    }
}




