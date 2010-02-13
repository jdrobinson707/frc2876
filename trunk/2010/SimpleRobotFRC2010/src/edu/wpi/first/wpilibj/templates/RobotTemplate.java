/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.image.*;
import edu.wpi.first.wpilibj.camera.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
interface Constants {

    public static final int DRIVE_MOTOR_LEFT_PWM = 1;
    public static final int DRIVE_MOTOR_RIGHT_PWM = 2;
    public static final int JOYSTICK_LEFT = 1;
    public static final int JOYSTICK_RIGHT = 3;
    public static final int JOYSTICK_COPILOT = 2;
    public static final int SHOOTER_MOTOR_PWM = 4;
    public static final int CONVEYOR_MOTOR_PWM = 7;
    public static final int DIGITAL_MODULE_SLOT = 4;
    public static final int JOYSTICK_NUM_BUTTONS = 11;
    public static final int JOYSTICK_FIRST_BUTTON = 1;
    public static final int JOYSTICK_LAST_BUTTON = Constants.JOYSTICK_FIRST_BUTTON
            + Constants.JOYSTICK_NUM_BUTTONS;
    public static final int PAN_SERVO = 9;
    public static final int TILT_SERVO = 10;
    public static final int GYRO_CHANNEL = 2;      // Slot number for the Gyro
    public static final int ENCODER_CHANNEL_A = 10;
    public static final int ENCODER_CHANNEL_B = 11;
}

public class RobotTemplate extends SimpleRobot {

    RobotDrive drive;
    Jaguar conveyor;
    Jaguar shooter;
    Joystick stickLeft;
    Joystick stickRight;
    Joystick stickCopilot;
    boolean rightButtons[];
    boolean leftButtons[];
    boolean copilotButtons[];
    int accelbutton;
    ColorImage image;
    AxisCamera camera;
    Servo pan;
    Servo tilt;
    Gyro gyro;
    Encoder encoder;

    public RobotTemplate() {
        System.out.println("Starting 2010 FRC RobotTemplate");

        // Joysticks
        stickRight = new Joystick(Constants.JOYSTICK_RIGHT);
        stickLeft = new Joystick(Constants.JOYSTICK_LEFT);
        stickCopilot = new Joystick(Constants.JOYSTICK_COPILOT);

        // and buttons
        rightButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        leftButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        copilotButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];

        // motors
        shooter = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
                Constants.SHOOTER_MOTOR_PWM);
        conveyor = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
                Constants.CONVEYOR_MOTOR_PWM);

        // servos
        tilt = new Servo(Constants.PAN_SERVO);
        pan = new Servo(Constants.TILT_SERVO);

        // Gyro
        gyro = new Gyro(Constants.GYRO_CHANNEL);     // indicates port Number
        encoder = new Encoder(Constants.ENCODER_CHANNEL_A, Constants.ENCODER_CHANNEL_B);

//        Watchdog.getInstance().feed();
        /*camera = AxisCamera.getInstance();
        camera.writeBrightness(0);
        camera.writeResolution(AxisCamera.ResolutionT.k320x240);*/
        // camera.writeResolution(AxisCamera.ResolutionT.k160x120);

        drive = new RobotDrive(Constants.DRIVE_MOTOR_LEFT_PWM, Constants.DRIVE_MOTOR_RIGHT_PWM);
        Watchdog.getInstance().feed();

    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    /* public void autonomous(){

    System.out.println("In Auto");
    for (int i = 0; i < 9; i++) {
    Watchdog.getInstance().setExpiration(5);
    Watchdog.getInstance().feed();
    drive.drive(.2, 0.0); // drive 50% fwd 0% turn
    Timer.delay(1.0); // wait 2 seconds
    drive.drive(0.0, 0.3); // drive 0% fwd, 75% turn
    }
    } */
    public void autonomous() {

        Watchdog.getInstance().setExpiration(8.0);
        System.out.println("In Auto");
        encoder.start();
        while (isAutonomous() && isEnabled()) {
            Watchdog.getInstance().feed();
            System.out.print(encoder.getDistance() + "distance\r");
            if (encoder.getDistance() > -3200) {
                drive.drive(-.5, 0);
                Timer.delay(1);
//                drive.drive(-.5, 1);
            }
            drive.drive(0, 0);
//            drive.drive(-.5, 0); // drive forward
//            //Timer.delay(2.0); // wait 3 seconds
//            drive.drive(-0.5, 1); // turn
//            Timer.delay(3.0);
        }
        encoder.stop();
        encoder.reset();
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

    public void updateVisionDashboard(double joyStickX, double gyroAngle) {

        Dashboard highDashData = DriverStation.getInstance().getDashboardPackerHigh();
        highDashData.addCluster(); // wire (2 elements)
        {
            highDashData.addCluster(); // tracking data
            {
                highDashData.addDouble(joyStickX); // Joystick X
                highDashData.addDouble(((gyroAngle + 360.0 + 180.0) % 360.0) - 180.0); // angle
                highDashData.addDouble(0); // angular rate
                highDashData.addDouble(0); // other X
            }
            highDashData.finalizeCluster();
//            highDashData.addCluster(); // target Info (2 elements)
//            {
//                highDashData.addArray();
//                {
//                    for (int i = 0; i < targets.length; i++) {
//                        highDashData.addCluster(); // targets
//                        {
//                            highDashData.addDouble(targets[i].m_score); // target score
//                            highDashData.addCluster(); // Circle Description (5 elements)
//                            {
//                                highDashData.addCluster(); // Position (2 elements)
//                                {
//                                    highDashData.addFloat((float) (targets[i].m_xPos / targets[i].m_xMax)); // X
//                                    highDashData.addFloat((float) targets[i].m_yPos); // Y
//                                    }
//                                highDashData.finalizeCluster();
//
//                                highDashData.addDouble(targets[i].m_rotation); // Angle
//                                highDashData.addDouble(targets[i].m_majorRadius); // Major Radius
//                                highDashData.addDouble(targets[i].m_minorRadius); // Minor Radius
//                                highDashData.addDouble(targets[i].m_rawScore); // Raw score
//                                }
//                            highDashData.finalizeCluster(); // Position
//                            }
//                        highDashData.finalizeCluster(); // targets
//                        }
//                }
//                highDashData.finalizeArray();
//
//
//                highDashData.addInt((int) time.getTime());
//            }
//            highDashData.finalizeCluster(); // target Info
        }
        highDashData.finalizeCluster(); // wire
        highDashData.commit();
    }

    public void initializeButtons() {
        for (int i = Constants.JOYSTICK_FIRST_BUTTON;
                i < leftButtons.length; i++) {
            leftButtons[i] = rightButtons[i] = false;
        }
    }

    public void readButtons(Joystick stick, boolean[] buttons, String side) {
        int i;
        for (i = Constants.JOYSTICK_FIRST_BUTTON; i < buttons.length; i++) {
            buttons[i] = stick.getRawButton(i);
        }
    }
    /*
    public void actOnButtons() {
    readButtons(stickLeft, leftButtons, "left");
    readButtons(stickCopilot, copilotButtons, "copilot");
    readButtons(stickRight, rightButtons, "right");

    updateConveyor();


    if (leftButtons[8] == true) {
    accelbutton = 8;

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
    }
*/
    public void updateConveyor() {
        double speed = 0.0;
        /*if (copilotButtons[7] == true) {
            speed = conveyor.get();
            speed = speed - .1;
            conveyor.set(speed);
        }*/
        if (copilotButtons[6] == true) {
            speed = conveyor.get();
            speed = speed + .1;
            conveyor.set(speed);
        }
        if (copilotButtons[1] == true) {
            conveyor.set(0);
        }
        if (copilotButtons[2] == true) {
            encoder.reset();
        }
    }

    

    /*    private void updateZButton() {
    double zValR;
    double zValL;
    zValR = stickRight.getZ();
    zValL = stickLeft.getZ();
    zValR = (zValR + 1) / 2;
    zValL = (zValL + 1) / 2;

    tilt.set(zValR);
    pan.set(zValL);
    //System.out.println(stickCopilot.getZ() + " "
    //        + stickLeft.getZ() + " "
    //        + stickRight.getZ() + " "
    //        + zValR);

    }*/
    private void dumpEncoderInfo(Encoder e) {
        System.out.println("ENCODER: "
                + " dir=" + e.getDirection()
                + " dist=" + e.getDistance()
                + " period=" + e.getPeriod()
                + " rate=" + e.getRate()
                + " raw=" + e.getRaw()
                + " stopped=" + e.getStopped());
    }
    private int totalKickRotations = 0;

    private void kickApoo(Encoder e) {
        String strButton = "none";
        readButtons(stickLeft, leftButtons, "left");
        if (leftButtons[1] == true) {
            strButton = "1";
        } else if (leftButtons[2] == true) {
            strButton = "2";
        } else if (leftButtons[3] == true) {
            strButton = "3";
        } else if (leftButtons[4] == true) {
            strButton = "4";
        } else if (leftButtons[5] == true) {
            strButton = "5";
        } else if (leftButtons[6] == true) {
            strButton = "6";
        } else if (leftButtons[7] == true) {
            strButton = "7";
        } else if (leftButtons[8] == true) {
            strButton = "8";
        }

        if (!strButton.equals("none")) {
            int btn = Integer.parseInt(strButton);
            double speed = ((double) btn) / 10.0;
            if (speed < 0 || speed > 1.0) {
                speed = .5;
            }
            double limit = 360;
            double tmp = (stickLeft.getZ() + 1) * 200;
            if (tmp != 0) {
                limit = tmp;
            }
            encoder.setDistancePerPulse((btn - 1) * 100);
            System.out.println("=========================================");
            System.out.println(" strButton=" + strButton
                    + " speed=" + speed + " limit=" + limit + " tmp=" + tmp);

            encoder.start();
            dumpEncoderInfo(encoder);
            drive.setLeftRightMotorSpeeds(-speed, 0.0);
            while (encoder.get() < limit) {
                Watchdog.getInstance().feed();
            }
            drive.setLeftRightMotorSpeeds(0.0, 0.0);
            encoder.stop();
            dumpEncoderInfo(encoder);
            int counts = encoder.get();
            totalKickRotations += counts;
            System.out.println("EncoderRotationCount:  "
                    + counts
                    + "  totalKickRotations: " + totalKickRotations);
            encoder.reset();
        }

    }

    public void operatorControl() {
        Watchdog.getInstance().feed();
        Watchdog.getInstance().setExpiration(3.0);
        System.out.println("In Operator control");
        initializeButtons();

        encoder.reset();
        dumpEncoderInfo(encoder);

        while (isOperatorControl() && isEnabled()) {

            Watchdog.getInstance().feed();
            Timer.delay(0.5);
            //kickApoo(encoder);
            readButtons(stickCopilot, copilotButtons, "copilot");
            updateConveyor();
        }
    }
    /*
    public void operatorControl() {
    System.out.println("In OperatorControl");
    // camera
    Watchdog.getInstance().setExpiration(3.0);
    encoder.start();

    while (isOperatorControl() && isEnabled()) {
    updateDashboard();
    updateVisionDashboard(stickCopilot.getX(), gyro.getAngle());
    System.out.print(encoder.getDistance() + "\r");
    Watchdog.getInstance().feed();
    Timer.delay(0.05);  //changed timer delay from .5
    drive.tankDrive(stickLeft, stickRight);
    //actOnButtons();
    updateZButton();
    if(leftButtons[1] == true){
    while(encoder.get() <= 360){
    drive.drive(5.0, 0.0);
    }

    encoder.stop();
    encoder.reset();
    }
    }
    //encoder.stop();
    System.out.println("\nEnd operatorControl");
    }*/
}
     
