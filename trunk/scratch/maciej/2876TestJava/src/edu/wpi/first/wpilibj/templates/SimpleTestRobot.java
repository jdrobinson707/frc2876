/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


// import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.*;

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
    public static final int JOYSTICK_LAST_BUTTON = Constants.JOYSTICK_FIRST_BUTTON +
                Constants.JOYSTICK_NUM_BUTTONS;

}


public class SimpleTestRobot extends SimpleRobot {

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
    //DashboardDataFormat ddf;
    Dashboard db;
    boolean rightButtons[];
    boolean leftButtons[];
    boolean copilotButtons[];
    int accelbutton;



    public SimpleTestRobot()
    {
        
        leftMotor = new Jaguar(Constants.DIGITAL_MODULE_SLOT, 
                Constants.DRIVE_MOTOR_LEFT_PWM);
        rightMotor = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
                Constants.DRIVE_MOTOR_RIGHT_PWM);
        robotDrive = new RobotDrive(leftMotor, rightMotor);
        
        stickLeft = new Joystick(Constants.JOYSTICK_LEFT);
        stickRight = new Joystick(Constants.JOYSTICK_RIGHT);
        stickCopilot = new Joystick(Constants.JOYSTICK_COPILOT);
        
        gyro = new Gyro(Constants.ANALOG_MODULE_SLOT, 
                Constants.GYRO_ANGLE_CHANNEL);
        shooter = new Jaguar(Constants.DIGITAL_MODULE_SLOT, 
                Constants.SHOOTER_MOTOR_PWM);
        conveyor = new Jaguar(Constants.DIGITAL_MODULE_SLOT, 
                Constants.CONVEYOR_MOTOR_PWM);
        
        // ddf = new DashboardDataFormat();
        // db = new Dashboard();
        //pan = new Servo(DIGITAL_MODULE_SLOT, 10);
        //tilt = new Servo(DIGITAL_MODULE_SLOT, 9);
        //leftEncoder = new Encoder(DIGITAL_MODULE_SLOT, LEFT_ENCODER_CHANNEL_A_GPIO, 
        //                DIGITAL_MODULE_SLOT, LEFT_ENCODER_CHANNEL_A_GPIO,
        //                false, Encoder::k4X);
        //rightEncoder = new Encoder(DIGITAL_MODULE_SLOT, RIGHT_ENCODER_CHANNEL_A_GPIO, 
        //                        DIGITAL_MODULE_SLOT, RIGHT_ENCODER_CHANNEL_A_GPIO,
        //                        true, Encoder::k4X);
        //driverStation = DriverStation::GetInstance();
        rightButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        leftButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        copilotButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];

    }

    public void initializeButtons() {
        for (int i = Constants.JOYSTICK_FIRST_BUTTON;
             i <= Constants.JOYSTICK_NUM_BUTTONS; i++) {
                leftButtons[i] = rightButtons[i] = false;
        }
    }

    public void readButtons(Joystick stick, boolean[] buttons, String side) {
        int i;
        for (i = Constants.JOYSTICK_FIRST_BUTTON; i <= buttons.length; i++) {
            buttons[i] = stick.getRawButton(i);
            if (buttons[i] == true) {
                // DBG("%s stick button %d pressed\n", side, i);
            }
        }
    }

    public void actOnButtons() {
        readButtons(stickLeft, leftButtons, "left");
        readButtons(stickCopilot, copilotButtons, "copilot");
        readButtons(stickRight, rightButtons, "right");

        updateConveyor();
        updateShooter();

        //      updatePanTilt();
        if (leftButtons[8] == true) {
                accelbutton = 8;
        }
        if (leftButtons[9] == true) {
                accelbutton = 9;
        }
        if (leftButtons[10] == true) {
                accelbutton = 10;
        }
        if (leftButtons[4] == true) {
                accelbutton = 4;
        }
        if (leftButtons[3] == true) {
                accelbutton = 3;
        }
        if (leftButtons[5] == true) {
                accelbutton = 5;
        }
    }

    public void updateConveyor() {
        double speed;
        if (copilotButtons[7] == true) {
            speed = conveyor.get();
            speed = speed - .1;
            conveyor.set(speed);
        }

        if (copilotButtons[6] == true) {
            speed = conveyor.get();
            speed = speed + .1;
            conveyor.set(speed);
        }
        if (copilotButtons[1] == true) {
            conveyor.set(0);
        }
    }


    public void updateShooter() {
        if (copilotButtons[2] == true) {
            shooter.set(0);
        }
        if (copilotButtons[4] == true) {
            shooter.set(.5);
        }
        if (copilotButtons[3] == true) {
            shooter.set(.6);
        }
        if (copilotButtons[5] == true) {
            shooter.set(.8);
        }
    }
    

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        while (isOperatorControl()) {
            getWatchdog().feed();
            try {
                wait(50000);
            } catch(InterruptedException iex) {
                
            }
        }
    }


}
