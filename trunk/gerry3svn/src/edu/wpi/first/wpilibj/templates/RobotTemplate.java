/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

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
    public static final int SHOOTER_MOTOR_PWM = 4;
    public static final int CONVEYOR_MOTOR_PWM = 3;
    public static final int DIGITAL_MODULE_SLOT = 4;
    public static final int JOYSTICK_NUM_BUTTONS = 11;
    public static final int JOYSTICK_FIRST_BUTTON = 1;
    public static final int JOYSTICK_LAST_BUTTON = Constants.JOYSTICK_FIRST_BUTTON +
                Constants.JOYSTICK_NUM_BUTTONS;
}


public class RobotTemplate extends SimpleRobot {
    
    RobotDrive drive = new RobotDrive(2, 1);
    
    Jaguar conveyor;
    Jaguar shooter;

    Joystick stickLeft = new Joystick(Constants.JOYSTICK_LEFT);
    Joystick stickRight = new Joystick(Constants.JOYSTICK_RIGHT);
    Joystick stickCopilot = new Joystick(Constants.JOYSTICK_COPILOT);

    Servo pan;
    Servo tilt;

    boolean rightButtons[];
    boolean leftButtons[];
    boolean copilotButtons[];
    int accelbutton;

    public RobotTemplate() {
        System.out.println("Starting 2010 FRC RobotTemplate");
        shooter = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
                Constants.SHOOTER_MOTOR_PWM);
        conveyor = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
                Constants.CONVEYOR_MOTOR_PWM);
        rightButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        leftButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        copilotButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];

    }
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        Watchdog.getInstance().feed();
        System.out.println("In Auto");
    }

    void updateDashboard() {
        Dashboard lowDashData = DriverStation.getInstance().getDashboardPackerLow();
        lowDashData.addCluster();
        {
            lowDashData.addCluster();
            {     //analog modules
                lowDashData.addCluster();
                {
                    for (int i = 1; i <= 8; i++) {
                        lowDashData.addFloat((float) AnalogModule.getInstance(1).getAverageVoltage(i));
                    }
                }
                lowDashData.finalizeCluster();
                lowDashData.addCluster();
                {
                    for (int i = 1; i <= 8; i++) {
                        lowDashData.addFloat((float) AnalogModule.getInstance(2).getAverageVoltage(i));
                    }
                }
                lowDashData.finalizeCluster();
            }
            lowDashData.finalizeCluster();

            lowDashData.addCluster();
            { //digital modules
                lowDashData.addCluster();
                {
                    lowDashData.addCluster();
                    {
                        int module = 4;
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
                        lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
                        lowDashData.addCluster();
                        {
                            for (int i = 1; i <= 10; i++) {
                                lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
                            }
                        }
                        lowDashData.finalizeCluster();
                    }
                    lowDashData.finalizeCluster();
                }
                lowDashData.finalizeCluster();

                lowDashData.addCluster();
                {
                    lowDashData.addCluster();
                    {
                        int module = 6;
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayReverse());
                        lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
                        lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
                        lowDashData.addCluster();
                        {
                            for (int i = 1; i <= 10; i++) {
                                lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
                            }
                        }
                        lowDashData.finalizeCluster();
                    }
                    lowDashData.finalizeCluster();
                }
                lowDashData.finalizeCluster();

            }
            lowDashData.finalizeCluster();

            lowDashData.addByte(Solenoid.getAll());
        }
        lowDashData.finalizeCluster();
        lowDashData.commit();

    }

    public void initializeButtons() {
        for (int i = Constants.JOYSTICK_FIRST_BUTTON;
             i <= Constants.JOYSTICK_NUM_BUTTONS; i++) {
                leftButtons[i] = rightButtons[i] = false;
        }
    }

    public void readButtons(Joystick stick, boolean[] buttons, String side) {
        int i;
        for (i = Constants.JOYSTICK_FIRST_BUTTON; i < buttons.length; i++) {
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
        // updateShooter();

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

    /*
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
    }*/
    
    public void operatorControl() {
        System.out.println("In OperatorControl");
        
        while (isOperatorControl() && isEnabled()) {
            updateDashboard();
            Watchdog.getInstance().feed();
            System.out.println("feed");
            // drive.arcadeDrive(stickRight);
            drive.tankDrive(stickLeft, stickRight);
            Timer.delay(0.005);
            actOnButtons();
        }
    }
}
