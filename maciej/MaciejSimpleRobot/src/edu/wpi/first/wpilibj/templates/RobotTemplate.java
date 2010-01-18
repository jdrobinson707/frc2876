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
    public static final int SHOOTER_MOTOR_PWM = 4;
    public static final int CONVEYOR_MOTOR_PWM = 3;
    public static final int ALLIANCE_SWITCH_GPIO = 1;
    public static final int ANALOG_MODULE_SLOT = 1;
    public static final int DIGITAL_MODULE_SLOT = 4;
    public static final int GYRO_ANGLE_CHANNEL = 1;
    public static final int GYRO_TEMP_CHANNEL = 2;
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
    Jaguar conveyor;
    Jaguar shooter;
    RobotDrive robotDrive;
    Joystick stickLeft;
    Joystick stickRight;
    Joystick stickCopilot;
    Gyro gyro;
    Servo pan;
    Servo tilt;
    DriverStation driverStation;
    DigitalInput allianceSwitch;
    boolean rightButtons[];
    boolean leftButtons[];
    boolean copilotButtons[];
    int accelbutton;
    AxisCamera cam;
    DashboardUpdater dbu;
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
        leftMotor = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
                Constants.DRIVE_MOTOR_LEFT_PWM);
        rightMotor = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
                Constants.DRIVE_MOTOR_RIGHT_PWM);

        robotDrive = new RobotDrive(leftMotor, rightMotor);
        // Play with this to make joystick drive robot forward when pushed
        // forward.
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

        stickLeft = new Joystick(Constants.JOYSTICK_LEFT);
        stickRight = new Joystick(Constants.JOYSTICK_RIGHT);
        stickCopilot = new Joystick(Constants.JOYSTICK_COPILOT);

        gyro = new Gyro(Constants.ANALOG_MODULE_SLOT,
                Constants.GYRO_ANGLE_CHANNEL);

        shooter = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
                Constants.SHOOTER_MOTOR_PWM);
        conveyor = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
                Constants.CONVEYOR_MOTOR_PWM);

        rightButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        leftButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        copilotButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];

        pan = new Servo(Constants.DIGITAL_MODULE_SLOT, Constants.PAN_CHANNEL);
        tilt = new Servo(Constants.DIGITAL_MODULE_SLOT, Constants.TILT_CHANNEL);
        cam = AxisCamera.getInstance();
        cam.writeResolution(AxisCamera.ResolutionT.k320x240);
        cam.writeBrightness(0);

        dbu = new DashboardUpdater();
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

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        DBG(WARN, "Enter autonomous");

        while (this.isAutonomous() && this.isEnabled()) {
            int maxPWM = 150;
            shooter.set(0);
            conveyor.set(0);
            Watchdog.getInstance().feed();
            Timer.delay(0.5);
            for (int i = 0; i < 10; i++) {
                shooter.set((i + 1) * 10);
                conveyor.set((i + 1) * 10);
                Watchdog.getInstance().feed();
                Timer.delay(0.5);
            }
        }
        shooter.set(0);
        conveyor.set(0);
        DBG(WARN, "Exit autonomous");
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        DBG(WARN, "Enter operatorControl");
        while (isOperatorControl() && isEnabled()) {

            robotDrive.tankDrive(stickLeft, stickRight);

            Watchdog.getInstance().feed();
            Timer.delay(0.05);
        }
        DBG(WARN, "Exit operatorControl");
    }
}
