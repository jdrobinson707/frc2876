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

    public static final int JOYSTICK_LEFT = 1;
    public static final int JOYSTICK_RIGHT = 2;
    public static final int JOYSTICK_ARM = 3;
    public static final int UNINITIALIZED_DRIVE = 0;
    public static final int ARCADE_DRIVE = 1;
    public static final int TANK_DRIVE = 2;
    public static final int DRIVE_MOTOR_LEFT = 1;
    public static final int DRIVE_MOTOR_RIGHT = 2;
    public static final int LINE_TRACKER_LEFT = 3;
    public static final int LINE_TRACKER_MIDDLE = 4;
    public static final int LINE_TRACKER_RIGHT = 5;
    public static final int ARM_JAGUAR = 3;
    public static final int ANALOG_CHANNEL_SLOT = 1;
    public static final int ANALOG_CHANNEL_CHANNEL = 3;
    public static final int PRESSURE_SWITCH_CHANNEL = 11;
    public static final int COMPRESSOR_RELAY_CHANNEL = 2;
    public static final int ARM_SOLENOID_1_CHANNEL = 1;
    public static final int ARM_SOLENOID_2_CHANNEL = 2;
    public static final int GRIP_SOLENOID_1_CHANNEL = 3;
    public static final int SOLENOID_SLOT = 8;

    public static final double TOP_PEG = 600.0;
    public static final double MIDDLE_PEG = 400.0;
    public static final double LOW_PEG = 200.0;
    public static final double FEEDER_HEIGHT = 500.0;
}

public class RobotTemplate extends SimpleRobot {

    RobotDrive drive;
    Joystick stickLeft;
    Joystick stickRight;
    Joystick stickArm;
    int driveMode;
    DriverStation ds;
    Encoder leftEncoder;
    Encoder rightEncoder;
    AnalogChannel ac;
    PIDController motorControl;
    double value;
    LineTracker lt;
    Arm arm;
    boolean armE1;
    boolean armE2;
    boolean grip;
    Compressor compressor;
    DriverStationLCD dslcd;

    public RobotTemplate() {
        stickRight = new Joystick(Constants.JOYSTICK_RIGHT);
        stickLeft = new Joystick(Constants.JOYSTICK_LEFT);
        stickArm = new Joystick(Constants.JOYSTICK_ARM);
        driveMode = Constants.TANK_DRIVE;
        drive = new RobotDrive(Constants.DRIVE_MOTOR_LEFT, Constants.DRIVE_MOTOR_RIGHT);
        //drive = new RobotDrive(1, 2, 3, 4); old robot
        drive.setExpiration(15);

        ds = DriverStation.getInstance();
        dslcd = DriverStationLCD.getInstance();

        //drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        //drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        //drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        //drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

        leftEncoder = new Encoder(7, 8, false, CounterBase.EncodingType.k1X);
        rightEncoder = new Encoder(9, 10, false, CounterBase.EncodingType.k1X);

        CameraInit();

        lt = new LineTracker(drive, leftEncoder, rightEncoder, ds);

        value = 0.0;

        armE1 = true;
        armE2 = false;
        grip = true;

        arm = new Arm();
        
        compressor = new Compressor(11,2);
    }

    public void CameraInit() {
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

    public void SetArmValue() {
        double driverValue = ds.getAnalogIn(1);
        if (driverValue == 1.0) {
            value = 200;
        } else if (driverValue == 3.0) {
            value = 400;
        } else if (driverValue == 5.0) {
            value = 600;
        }
    }

    public boolean CheckJoystick() {
        boolean isSet = false;
        if (stickArm.getRawButton(6)) {
            value = 200;
            isSet = true;
        } else if (stickArm.getRawButton(7)) {
            value = 400;
            isSet = true;
        } else if (stickArm.getRawButton(11)) {
            value = 600;
            isSet = true;
        } else if (stickArm.getRawButton(10)) {
            value = 500;
            isSet = true;
        } else if (stickArm.getRawButton(4)) {
            armE1 = true;
            armE2 = false;
            isSet = true;
        } else if (stickArm.getRawButton(5)) {
            armE1 = false;
            armE2 = true;
            isSet = true;
        } else if (stickArm.getRawButton(3)) {
            grip = true;
            isSet = true;
        } else if (stickArm.getRawButton(2)) {
            grip = false;
            isSet = true;
        }
        if (isSet) {
            System.out.println("isSet:" + isSet + "  value:" + value);
        }
        return isSet;
    }

    public void autonomous() {
        System.out.println("Starting Autonomous");

        while (isAutonomous() && isEnabled()) {
            Timer.delay(.3);
            //OpenArm();
            //LineTracker();
            //if (motorControl.isEnable()) {
            //    armMovement();
            //}

            //lt.FollowLine();
            //SetArmValue();
            //arm.runMovement(650.0);
            //System.out.println("first movement " + arm);
            lt.start();
            //lt.FollowLine();
            //CheckJoystick();
            //arm.armMovement(value);


            //arm.ExtendArm(armE1, armE2);
            //arm.OpenClaw(grip1, grip2);

            //armJag.set(stickLeft.getY() / 2.0);

            //range: .67 - 3.75 Volts
            //range: 125 - 670 Value

            //if (FindTarget()){
            //    System.out.println("FOUND TARGET!");
            //    PutPiece();
            //    break;

            //}

        }
        System.out.println("Ending Autonomous");
    }

    public void operatorControl() {

        System.out.println("In Operator control");

        leftEncoder.reset();
        rightEncoder.reset();
        leftEncoder.start();
        rightEncoder.start();
        
        System.out.println("Start Compressor");
        System.out.println("Switch: " + compressor.getPressureSwitchValue());

        //compressor.start();


        while (isOperatorControl() && isEnabled()) {
            Timer.delay(0.1);

            int x = 1;

            if (stickLeft.getRawButton(3) || stickRight.getRawButton(3))
            {
                x = 1;
                dslcd.println(DriverStationLCD.Line.kUser2, 1, "Full Speed");

            }
            if (stickLeft.getRawButton(2) || stickRight.getRawButton(2))
            {
                x = 2;
                dslcd.println(DriverStationLCD.Line.kUser2, 1, "Half Speed");
            }

            // drive.tankDrive(-stickLeft.getY(), -stickRight.getY());
            //System.out.println("Switch: " + compressor.getPressureSwitchValue());
            // Slow down forward/backward speed of robot

            //these lines are luke's
            //if (x == 1) {
            //    drive.tankDrive(stickLeft.getY() / x, stickRight.getY() / x);
            //}
            //if (x == 2) {
            //    drive.tankDrive(-stickRight.getY() / x, -stickLeft.getY() / x);
            //}

            // comment this if above lines not commented
            drive.tankDrive(-stickLeft.getY() / x, -stickRight.getY() / x);

            if (stickLeft.getTrigger())
            {
                compressor.start();
                System.out.println("START COMPRESSOR");
            }
            if (stickRight.getTrigger())
            {
                compressor.stop();
                System.out.println("STOP COMPRESSOR!");
            }

            arm.set(stickArm.getY());

            if (CheckJoystick()) {
                //arm.runMovement(value);
                arm.ExtendArm(armE1, armE2);
                System.out.println("ArmE1: " + armE1 + " ArmE2: " + armE2);
                arm.OpenClaw(grip);
                System.out.println("Grip: " + grip);
            }
            
            System.out.println(arm);

            dslcd.updateLCD();
        }

        System.out.println("Leaving Operator Control");
        compressor.stop();
    }
}




