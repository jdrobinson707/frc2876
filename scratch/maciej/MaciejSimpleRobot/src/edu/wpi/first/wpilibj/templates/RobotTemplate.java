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
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;

interface Constants {

    public static final int JOYSTICK_LEFT = 1;
    public static final int JOYSTICK_RIGHT = 2;
    public static final int JOYSTICK_COPILOT = 3;
    // which pwm inputs/outputs the drive motors are plugged into
    public static final int DRIVE_MOTOR_RIGHT_PWM = 1;
    public static final int DRIVE_MOTOR_LEFT_PWM = 2;
    public static final int ANALOG_MODULE_SLOT = 1;
    public static final int DIGITAL_MODULE_SLOT = 4;
    public static final int GYRO_ANGLE_CHANNEL = 2;
    public static final int JOYSTICK_NUM_BUTTONS = 17;
    public static final int JOYSTICK_FIRST_BUTTON = 1;
    public static final int JOYSTICK_LAST_BUTTON = Constants.JOYSTICK_FIRST_BUTTON
            + Constants.JOYSTICK_NUM_BUTTONS;
    public static final int PAN_CHANNEL = 10;
    public static final int TILT_CHANNEL = 9;
}

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {

    Jaguar leftMotor;
    Jaguar rightMotor;
    RobotDrive robotDrive;
    Joystick stickLeft;
    Joystick stickRight;
    Joystick stickCopilot;
    Gyro gyro;
    Servo pan;
    Servo tilt;
    DriverStation driverStation;
    DriverStationLCD dslcd;
    boolean rightButtons[];
    boolean leftButtons[];
    boolean copilotButtons[];
    AxisCamera cam;
    DashboardUpdater dbu;
    Encoder encoder;
    private int dbgLevel = OFF;
    private static final int DEBUG = 5;
    private static final int WARN = 2;
    private static final int ERR = 1;
    private static final int OFF = 0;

    private void DBG(int msgLevel, String msg) {
        if (msgLevel <= dbgLevel) {
            System.out.println(msg);
        }
    }

    public RobotTemplate() {
        Watchdog.getInstance().setExpiration(2.0);
        Watchdog.getInstance().feed();

//        leftMotor = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
//                Constants.DRIVE_MOTOR_LEFT_PWM);
//        rightMotor = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
//                Constants.DRIVE_MOTOR_RIGHT_PWM);
//
//        robotDrive = new RobotDrive(leftMotor, rightMotor);
//        // Play with this to make joystick drive robot forward when pushed
//        // forward.
//        robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
//        robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, false);

        robotDrive = new RobotDrive(1, 3, 2, 4);

        stickLeft = new Joystick(Constants.JOYSTICK_LEFT);
        stickRight = new Joystick(Constants.JOYSTICK_RIGHT);
        stickCopilot = new Joystick(Constants.JOYSTICK_COPILOT);

        gyro = new Gyro(Constants.ANALOG_MODULE_SLOT,
                Constants.GYRO_ANGLE_CHANNEL);

        rightButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        leftButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        copilotButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];

        pan = new Servo(Constants.DIGITAL_MODULE_SLOT, Constants.PAN_CHANNEL);
        tilt = new Servo(Constants.DIGITAL_MODULE_SLOT, Constants.TILT_CHANNEL);
        Watchdog.getInstance().feed();
//        cam = AxisCamera.getInstance();
//        cam.writeResolution(AxisCamera.ResolutionT.k320x240);
//        cam.writeBrightness(0);
        Watchdog.getInstance().feed();
//        dbu = new DashboardUpdater();
    }

    public void initializeButtons() {
        for (int i = Constants.JOYSTICK_FIRST_BUTTON;
                i <= Constants.JOYSTICK_NUM_BUTTONS; i++) {
            leftButtons[i] = rightButtons[i] = copilotButtons[i] = false;
        }
    }

    public void readButtons(Joystick stick, boolean[] buttons, String side) {
        int i;
        for (i = Constants.JOYSTICK_FIRST_BUTTON; i <= buttons.length; i++) {
            buttons[i] = stick.getRawButton(i);
            if (buttons[i] == true) {
                DBG(DEBUG, side + " stick button " + i + " pressed");
            } else {
                buttons[i] = false;
            }
        }
    }

    public void actOnButtons() {
        readButtons(stickLeft, leftButtons, "left");
        readButtons(stickCopilot, copilotButtons, "copilot");
        readButtons(stickRight, rightButtons, "right");
    }

    private void updateZButton() {
        double zValR;
        double zValL;
        zValR = stickRight.getZ();
        zValL = stickLeft.getZ();
        zValR = (zValR + 1) / 2;
        zValL = (zValL + 1) / 2;

        tilt.set(zValR);
        pan.set(zValL);
    }
    private boolean last_target = false;

    private void do_camera_test() {
        double kScoreThreshold = .01;
        try {
            if (cam.freshImage()) {// && turnController.onTarget()) {
                double gyroAngle = gyro.pidGet();
                ColorImage image = cam.getImage();
                Thread.yield();
                Target[] targets = Target.findCircularTargets(image);
                Thread.yield();
                image.free();
                if (targets.length == 0 || targets[0].m_score < kScoreThreshold) {
                    if (last_target == true) {
                        System.out.println("No target found");
                    }
                    last_target = false;

                    Target[] newTargets = new Target[targets.length + 1];
                    newTargets[0] = new Target();
                    newTargets[0].m_majorRadius = 0;
                    newTargets[0].m_minorRadius = 0;
                    newTargets[0].m_score = 0;
                    for (int i = 0; i < targets.length; i++) {
                        newTargets[i + 1] = targets[i];
                    }
                    dbu.updateVisionDashboard(0.0, gyro.getAngle(), 0.0, 0.0, newTargets);
                } else {
                    last_target = true;
                    System.out.println(targets[0]);
                    System.out.println("Target Angle: " + targets[0].getHorizontalAngle());
                    dbu.updateVisionDashboard(0.0, gyro.getAngle(), 0.0,
                            targets[0].m_xPos / targets[0].m_xMax, targets);
                }
            }
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        } catch (AxisCameraException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        DBG(WARN, "Enter autonomous");

        while (this.isAutonomous() && this.isEnabled()) {
        }
        DBG(WARN, "Exit autonomous");
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        DBG(WARN, "Enter operatorControl");
        Watchdog.getInstance().setExpiration(2.0);
        Watchdog.getInstance().feed();
        while (isOperatorControl() && isEnabled()) {
            Watchdog.getInstance().feed();
            robotDrive.tankDrive(stickLeft, stickRight);
            updateZButton();
            //do_camera_test();
            Timer.delay(0.005);
        }
        DBG(WARN, "Exit operatorControl");
    }
}
