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
    public static final int SOLENOID_SLOT = 3;
    public static final double TOP_PEG = 618.0;
    public static final double MIDDLE_PEG = 485.0;
    public static final double LOW_PEG = 338.0;
    public static final double MIDDLE_TOP_PEG = 655.0;
    public static final double MIDDLE_MIDDLE_PEG = 501.0;
    public static final double MIDDLE_LOW_PEG = 377.0;
    public static final double FEEDER_HEIGHT = 400.0; //not correct height
    public static final int CLAW_BUTTON = 1;
    public static final int EXTEND_ARM_BUTTON = 4;
    public static final int LOW_PEG_BUTTON = 6;
    public static final int MIDDLE_PEG_BUTTON = 7;
    public static final int TOP_PEG_BUTTON = 11;
    public static final int FEEDER_HOLE_BUTTON = 10;
    public static final int SONAR_CHANNEL = 6;
}

public class RobotTemplate extends SimpleRobot {

    RobotDrive drive;
    Joystick stickLeft;
    Joystick stickRight;
    Joystick stickArm;
    int driveMode;
    DriverStation ds;
    //Encoder leftEncoder;
    //Encoder rightEncoder;
    AnalogChannel ac;
    PIDController motorControl;
    double value;
    LineTracker lt;
    Arm arm;
    boolean armE1;
    boolean armE2;
    boolean grip;
    public boolean isDone = false;
    Solenoid minibotSolenoid1;
    Solenoid minibotSolenoid2;
    Compressor compressor;
    DriverStationLCD dslcd;
    boolean b1 = true;
    boolean b2 = false;
    //AnalogChannel sonar;
    Sonar sonar;

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

        sonar = new Sonar();

        //sonar = new AnalogChannel(Constants.ANALOG_CHANNEL_SLOT, Constants.SONAR_CHANNEL);

        //drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        //drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        //drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        //drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

        //leftEncoder = new Encoder(7, 8, false, CounterBase.EncodingType.k1X);
        //rightEncoder = new Encoder(9, 10, false, CounterBase.EncodingType.k1X);

        // CameraInit();

        lt = new LineTracker(drive, ds);

        value = 0.0;

        armE1 = true;
        armE2 = false;
        grip = true;

        //arm = new Arm();

        //minibotSolenoid1 = new Solenoid(Constants.SOLENOID_SLOT, 4);
        //minibotSolenoid2 = new Solenoid(Constants.SOLENOID_SLOT, 5);

        compressor = new Compressor(11, 2);
        compressor.start();
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

    /*public void testenc() {
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
    rightEncoder.stop();
    }*/
    public void SetArmValue() {
        double driverValue = ds.getAnalogIn(1);
        if (driverValue == 1.0) {
            value = Constants.MIDDLE_LOW_PEG;
        } else if (driverValue == 3.0) {
            value = Constants.MIDDLE_MIDDLE_PEG;
        } else if (driverValue == 5.0) {
            value = Constants.MIDDLE_TOP_PEG;
        }
    }

    public void autonomous1() {
        double analog1 = ds.getAnalogIn(1);
        double analog2 = ds.getAnalogIn(2);

        while (isAutonomous() && isEnabled() && !isDone) {

            //For autonomous:
            //3.  Close claw
            //2.  Raise arm completley
            //3.  Extend arm
            //4.  Follow Line
            //5.  The tube will hopefully be on the peg
            //6.  Open claw
            //7.  Retract arm

            //arm.RaiseArmSlightly();
            //arm.OpenClaw();
            //arm.ExtendArm();
            //arm.CloseClaw();
            //arm.RaiseArmToTop();
            //lt.FollowLine();

            //drive.tankDrive(-.7, -.7);
            //Timer.delay(.8);
            //drive.tankDrive(0, 0);

            //arm.OpenClaw();
            //arm.RetractArm();

            //if (b1) {
            Timer.delay(.3);
            RaiseToTop();
            System.out.println("isDone: " + isDone);
            Timer.delay(.5);
            arm.CloseClaw();
            System.out.println("Is Claw Open: " + arm.isClawOpen());
            Timer.delay(.3);
            arm.ExtendArm();
            Timer.delay(6);
            lt.FollowLine();
            //}

            //Timer.delay(.3);
            //arm.ExtendArm();
            //lt.FollowLine();

            //Timer.delay(5);

            //arm.OpenClaw();
            //arm.RetractArm();

            //Timer.delay(.2);
            //OpenArm();
            //LineTracker();
            //if (motorControl.isEnable()) {
            //    armMovement();
            //}

            //lt.FollowLine();
            //SetArmValue();
            //arm.runMovement(650.0);
            //System.out.println("first movement " + arm);

            //lt.start();

            // lt.FollowLine();
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
            //    break;f

            //}

        }
    }
    PIDController pid;

    public void test_auto_pid() {
        pid = new PIDController(.5, 1, 1, sonar,
                new PIDOutput() {

                    public void pidWrite(double output) {
                    }
                });
        pid.setSetpoint(20);
        pid.setTolerance(1.0);
        pid.setInputRange(6, 254);
        pid.setOutputRange(-.6, .6);
        pid.enable();
        Timer.delay(.5);
        isDone = false;
        while (isAutonomous() && isEnabled() && !isDone) {
            double speed = -pid.get();
            System.out.println(" Speed: " + speed + " Error from Set Point: " + pid.getError()
                    + " Distance: " + sonar.getDistanceF1());

            if (pid.onTarget()) {
                pid.disable();
                System.out.println(" REACHED SET POINT ");
                isDone = true;
                speed = 0;
            }
            drive.tankDrive(speed, speed);
            Timer.delay(.5);
        }

    }

    public void test_sonar_equation() {
        while (isAutonomous() && isEnabled() && !isDone) {
            double distance = sonar.getDistanceF1();
            double speed = 0.0;

            if (distance < 150) {
                speed = (1 / 150) * distance;
            } else {
                speed = 1;
            }

            //drive.tankDrive(speed, speed);
            System.out.println("SonarF1: " + distance);
            System.out.println("Speed: " + speed);
        }
    }

    public void test_sonar_auto() {

        while (isAutonomous() && isEnabled() && !isDone) {
            double distance = sonar.getDistanceF1();
            double speed = 0.0;

            /*if (distance > 23)
            {
            drive.tankDrive(1, 1);
            }
            else if (distance < 19)
            {
            drive.tankDrive(-1, -1);
            }
            else
            {
            drive.tankDrive(0, 0);
            }*/

            if (distance > 150) {
                drive.tankDrive(1, 1);
            }
            if (distance <= 150 && distance > 100) {
                drive.tankDrive(0.65, 0.65);
            }
            if (distance <= 100 && distance > 80) {
                drive.tankDrive(0.55, 0.55);
            }
            if (distance <= 80 && distance > 60) {
                drive.tankDrive(0.45, 0.45);
            }
            if (distance <= 60) {
                drive.tankDrive(0, 0);
            }

            System.out.println("SonarF1: " + distance);
        }
    }

    public void autonomous() {
        System.out.println("Starting Autonomous");
        //autonomous1();
        // test_sonar_auto();
        test_auto_pid();
        System.out.println("Ending Autonomous");
    }

    public void RaiseToTop() {
        if (arm.ac.pidGet() > Constants.MIDDLE_TOP_PEG - 50) {
            arm.set(0);
            Timer.delay(5);
            System.out.println("Reached peg!");
            arm.OpenClaw();
            arm.RetractArm();
            isDone = true;
            b2 = true;
            b1 = false;
        } else {
            arm.set(1);
            isDone = false;
        }
    }

    public void CheckButtons(Joystick stick, boolean[] buttons) {
        for (int i = 1; i < buttons.length; i++) {
            buttons[i] = stick.getRawButton(i);
            if (buttons[i] == true) {
                System.out.println("pressed button " + i);
            }
        }
    }

    public boolean CheckArmAction(boolean[] buttons) {
        boolean isSet = false;
        if (buttons[Constants.LOW_PEG_BUTTON]) {
            value = 200;
            isSet = true;
        } else if (buttons[Constants.MIDDLE_PEG_BUTTON]) {
            value = 400;
            isSet = true;
        } else if (buttons[Constants.TOP_PEG_BUTTON]) {
            value = 600;
            isSet = true;
        } else if (buttons[Constants.FEEDER_HOLE_BUTTON]) {
            value = 500;
            isSet = true;
        }
        if (isSet) {
            System.out.println("isSet:" + isSet + "  value:" + value);
            System.out.println(this.arm);
            arm.updateSetpoint(value);
        }
        return isSet;
    }

    public boolean CheckArmExtend(boolean[] buttons) {
        boolean isSet = false;
        if (buttons[Constants.EXTEND_ARM_BUTTON]) {
            armE1 = true;
            armE2 = false;
            isSet = true;

            arm.ToggleArmExtend();
            Timer.delay(.4);
        }
        return isSet;
    }

    public boolean CheckGrip(boolean[] buttons) {
        boolean isSet = false;
        if (buttons[Constants.CLAW_BUTTON]) {
            grip = false;
            isSet = true;
            arm.ToggleClaw();
            Timer.delay(.4);
        }
        return isSet;
    }

    public void operatorControl() {
        System.out.println("In Operator control");
        boolean armButtonArray[] = new boolean[12];
        int x = 1;
        CheckButtons(stickArm, armButtonArray);
        while (isOperatorControl() && isEnabled()) {

            CheckButtons(stickArm, armButtonArray);
            Timer.delay(0.1);
            drive.tankDrive(-stickLeft.getY() / x, -stickRight.getY() / x);

            CheckButtons(stickArm, armButtonArray);
            CheckArmExtend(armButtonArray);
            CheckGrip(armButtonArray);
            //CheckArmAction(armButtonArray);

            if (stickLeft.getRawButton(3) || stickRight.getRawButton(3)) {
                x = 1;
            }
            if (stickLeft.getRawButton(2) || stickRight.getRawButton(2)) {
                x = 2;
            }

            //if (stickLeft.getRawButton(1))
            //{
            //System.out.println("Sonar Value: " + sonar.getVoltage());
            //System.out.println("SonarRaw: " + sonar.getDistanceRaw());
            //System.out.println("SonarF1: " + sonar.getDistanceF1());
            //}
            //System.out.println("Arm: " + arm.get());

            //System.out.println("LeftStick: " + stickArm.getY());

            // arm is within limits so let joystick move it
            /*if (!arm.checkLimits()) {
                arm.set(stickArm.getY() / x);
            } else {
                // arm is not within limit, don't move it with joystick
                //arm.set(stickArm.getY() / x);
            }
*/
            if (stickArm.getRawButton(8) && stickArm.getRawButton(9)) {
                boolean b = minibotSolenoid2.get();
                minibotSolenoid1.set(b);
                minibotSolenoid2.set(!b);
                Timer.delay(.3);
            }
            if (armButtonArray[5]) {
                System.out.println("but5: " + arm);
            }
            dslcd.updateLCD();
        }
        //arm.OpenClaw();
        //arm.RetractArm();
        // need to lower arm

        System.out.println("Leaving Operator Control");
    }
}




