/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.ColorImage;
/*import edu.wpi.first.wpilibj.image.*;
import edu.wpi.first.wpilibj.camera.*;*/

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
interface Constants {

    public static final int ANALOG_MODULE_SLOT = 1;
    public static final int DIGITAL_MODULE_SLOT = 4;
    //joystick channels
    public static final int JOYSTICK_LEFT = 1;
    public static final int JOYSTICK_RIGHT = 2;
    public static final int JOYSTICK_COPILOT = 3;
    public static final int JOYSTICK_NUM_BUTTONS = 11;
    public static final int JOYSTICK_FIRST_BUTTON = 1;
    public static final int JOYSTICK_LAST_BUTTON = Constants.JOYSTICK_FIRST_BUTTON
            + Constants.JOYSTICK_NUM_BUTTONS;
    //Digital Sidecar PWM OUT channels
    public static final int DRIVE_MOTOR_LEFT_FRONT_PWM = 1;
    public static final int DRIVE_MOTOR_LEFT_REAR_PWM = 2;
    public static final int DRIVE_MOTOR_RIGHT_FRONT_PWM = 3;
    public static final int DRIVE_MOTOR_RIGHT_REAR_PWM = 4;
    public static final int ROLLER_MOTOR_PWM = 6;
    public static final int CAM_MOTOR_PWM = 5;
    public static final int PAN_SERVO = 9;
    public static final int TILT_SERVO = 10;
    //Digital Sidecar I/O Channels
    public static final int LIMIT_SWITCH_CHANNEL = 1;
    public static final int ENCODER_RIGHT_DRIVE_CHANNEL_A = 3;
    public static final int ENCODER_RIGHT_DRIVE_CHANNEL_B = 4;
    public static final int ENCODER_LEFT_DRIVE_CHANNEL_A = 5;
    public static final int ENCODER_LEFT_DRIVE_CHANNEL_B = 6;
    public static final int ENCODER_CAM_CHANNEL_A = 7;
    public static final int ENCODER_CAM_CHANNEL_B = 8;
    public static final int AUTONOMOUS_GREEN_SWITCH_CHANNEL = 10;
    public static final int AUTONOMOUS_SILVER_SWITCH_CHANNEL = 11;
    //Analog Channels
    public static final int GYRO_CHANNEL_RATE = 1;
    public static final int GYRO_CHANNEL_TEMP = 2;
    //other constants
    public static final int UNINITIALIZED_DRIVE = 0;
    public static final int ARCADE_DRIVE = 1;
    public static final int TANK_DRIVE = 2;
}

public class Kicker_4WD_Robot extends SimpleRobot {

    RobotDrive drive;
    int driveMode;
    Jaguar cam;
    Jaguar roller;
    Joystick stickLeft;
    Joystick stickRight;
    Joystick stickCopilot;
    boolean rightButtons[];
    boolean leftButtons[];
    boolean copilotButtons[];
    Gyro gyro;
    DigitalInput limSwitch;
    boolean rollerIsRolling;
    boolean flag;
    boolean intervalFlag = true;
    DigitalInput autonomousGreenSwitch;
    DigitalInput autonomousSilverSwitch;
    DriverStationLCD dslcd;
    boolean kickSwitch;
    long startTime = 0;
    ColorImage image;
    AxisCamera camera;

    public Kicker_4WD_Robot() {
        System.out.println("Starting 2010 FRC RobotTemplate");

        // Joysticks
        stickRight = new Joystick(Constants.JOYSTICK_RIGHT);
        stickLeft = new Joystick(Constants.JOYSTICK_LEFT);
        stickCopilot = new Joystick(Constants.JOYSTICK_COPILOT);

        // and buttons
        rightButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        leftButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];
        copilotButtons = new boolean[Constants.JOYSTICK_NUM_BUTTONS];

        // manipulator motors
        cam = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
                Constants.CAM_MOTOR_PWM);
        roller = new Jaguar(Constants.DIGITAL_MODULE_SLOT,
                Constants.ROLLER_MOTOR_PWM);

        // Gyro
        gyro = new Gyro(Constants.ANALOG_MODULE_SLOT, Constants.GYRO_CHANNEL_RATE);     // indicates port Number

        // Camera
        camera = AxisCamera.getInstance();
        camera.writeBrightness(0);
        camera.writeResolution(AxisCamera.ResolutionT.k320x240);

        //Limit switch
        limSwitch = new DigitalInput(Constants.LIMIT_SWITCH_CHANNEL);

        //Autonomous Switch
        autonomousSilverSwitch = new DigitalInput(Constants.DIGITAL_MODULE_SLOT,
                Constants.AUTONOMOUS_SILVER_SWITCH_CHANNEL);
        autonomousGreenSwitch = new DigitalInput(Constants.DIGITAL_MODULE_SLOT,
                Constants.AUTONOMOUS_GREEN_SWITCH_CHANNEL);


        Watchdog.getInstance().feed();

        driveMode = Constants.UNINITIALIZED_DRIVE;
        drive = new RobotDrive(Constants.DRIVE_MOTOR_LEFT_FRONT_PWM,
                Constants.DRIVE_MOTOR_LEFT_REAR_PWM,
                Constants.DRIVE_MOTOR_RIGHT_FRONT_PWM,
                Constants.DRIVE_MOTOR_RIGHT_REAR_PWM);

        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);



        //starting roller condition
        rollerIsRolling = false;
        roller.set(0.0);

        //starting limit switch condition
        flag = true;

        Watchdog.getInstance().feed();

        dslcd = DriverStationLCD.getInstance();
        dslcd.println(DriverStationLCD.Line.kUser2, 1, "                     ");
        dslcd.println(DriverStationLCD.Line.kUser3, 1, "                     ");
        dslcd.println(DriverStationLCD.Line.kUser4, 1, "                     ");
        dslcd.println(DriverStationLCD.Line.kUser5, 1, "                     ");
        dslcd.updateLCD();
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

    public void autonomous() {
        String str = "Enter Autonomous";
        System.out.println(str);
        dslcd.println(DriverStationLCD.Line.kUser4, 1, str);
        startTime = Timer.getUsClock();
        int kickCount = 0;
        gyro.reset();
        double startGyro = gyro.getAngle();
        int spinCounter = 0;

        while (isAutonomous() && isEnabled()) {
            Watchdog.getInstance().feed();
            Timer.delay(0.05);
            long nowTime = Timer.getUsClock();
            long difTime = (nowTime - startTime);
            
            double currentGyro = gyro.getAngle();
            double diffGyro = startGyro - currentGyro;

            System.out.println(diffGyro);
            spinCounter = 0;
            
            int times = 0;
            while (times < 4) {
                long startDriveTime = nowTime;
                while ( nowTime - startDriveTime <= 2) {

                }

                }
            }

//            if (diffGyro >= 90 && spinCounter != 4) {
//                    drive.tankDrive(0.5, 0.5);
//                    
//                    
//                }else{
//                    drive.tankDrive(0.20, -0.20);
//                    spinCounter++;
//                    gyro.reset();
//                    long lastTime = nowTime;


        /*    System.out.println(difTime);
            // if more than 2 seconds goes by stop driving
            if (difTime >= (2 * 1000 * 1000)) {
                System.out.println("stop driving");
                drive.tankDrive(0, 0);
            } else {
                // if less than 2s goes by, drive
                drive.tankDrive(-.75, -.75);
                System.out.println("driving");
            }
            if (difTime < (2.55 * 1000 * 1000) && (difTime > (2.5 * 1000 * 1000) && (kickCount == 0))) {

                copilotButtons[1] = true;
                kickBall();
                copilotButtons[1] = false;
                kickCount = (kickCount + 1);

            }*/

        str = "Exit Autonomous";
        System.out.println(str);
        dslcd.println(DriverStationLCD.Line.kUser4, 1, str);
    }

    public void updateRoller() {
        readButtons(stickRight, rightButtons, "right");

        if (copilotButtons[6] == true) {
//            if (rollerIsRolling == false) {
//                rollerIsRolling = true;
            roller.set(1.0);
//            } else {
//                rollerIsRolling = false;
//                roller.set(0.0);
//            }
        } else if (copilotButtons[7] == true) {
//            if (roller.get() == 0.0) {
//                roller.set(-1.0);
//            } else {
//                roller.set(0.0);
//            }
            roller.set(0.0);
        }
    }

    public void userOptions() {
        // determine if tank or arcade mode, based upon position of "Z" wheel on kit joystick
        if (stickRight.getZ() <= 0) {    // Logitech Attack3 has z-polarity reversed; up is negative
            // use arcade drive

            if (driveMode != Constants.ARCADE_DRIVE) {
                // if newly entered arcade drive, print out a message
                String str = "Arcade Drive";
                System.out.println(str);
                dslcd.println(DriverStationLCD.Line.kUser4, 1, str);
                driveMode = Constants.ARCADE_DRIVE;
            }
        } else {
            // use tank drive

            if (driveMode != Constants.TANK_DRIVE) {
                // if newly entered tank drive, print out a message
                String str = "Tank Drive";
                System.out.println(str);
                dslcd.println(DriverStationLCD.Line.kUser4, 1, str);
                driveMode = Constants.TANK_DRIVE;
            }
        }
    }

    private void kickBall() {
        if (isOperatorControl()) {
            readButtons(stickCopilot, copilotButtons, "copilot");
        }
        long nowTime = Timer.getUsClock();

        if (nowTime - startTime >= (2 * 1000 * 1000)) {
            dslcd.println(DriverStationLCD.Line.kUser2, 1, "Kick: Now!           ");
        } else {
            long countDown = nowTime - startTime;
            countDown = (2 * 1000 * 1000) - countDown;
            countDown /= 10000;
            dslcd.println(DriverStationLCD.Line.kUser2, 1, "Kick: " + countDown + "     ");
        }

        if (copilotButtons[1] == true && cam.getSpeed() == 0) {
            if (nowTime - startTime >= (2 * 1000 * 1000)) {
                roller.set(0.0);
                cam.set(.9);
                intervalFlag = false;
            }
//                long endTimeInterval = Timer.getUsClock();
            //              endTimeInterval /= 10000;
            //            System.out.println("interval end time: " + endTimeInterval);
        }
        if ((!limSwitch.get()) && (flag == false)) {
            // false = 0 = closed
            flag = true;
            kickSwitch = limSwitch.get();
            System.out.println("kicking switch=" + kickSwitch);
        } else if ((limSwitch.get()) && (flag == true)) {
            // true = 1 = open. kicker is loaded
            cam.set(0.0);
            roller.set(1.0);
            long startTimeInterval = Timer.getUsClock();
            startTimeInterval /= 10000;
            System.out.println("interval start time: " + startTimeInterval);
            flag = false;
            startTime = nowTime;
            intervalFlag = true;
            kickSwitch = limSwitch.get();
            System.out.println("stop kick switch= " + kickSwitch);
        }
        if (limSwitch.get() != kickSwitch) {
            System.out.println("limSwitch changed  switch=" + limSwitch.get());
            kickSwitch = limSwitch.get();
        }

        if (copilotButtons[2] == true) {
            cam.set(0.0);
        }
    }

    public void operatorControl() {
        Watchdog.getInstance().feed();
        Watchdog.getInstance().setExpiration(3.0);
        System.out.println("In Operator control");
        initializeButtons();
        userOptions();
        kickSwitch = limSwitch.get();

        while (isOperatorControl() && isEnabled()) {

            Watchdog.getInstance().feed();
            Timer.delay(0.05);
            updateDashboard();
            updateRoller();
            // Broken -- this line causes the robot to drive without touching the joysticks.  Needs to be fixed.
            kickBall();
            dslcd.updateLCD();
//            if (driveMode == Constants.ARCADE_DRIVE) {
//                drive.arcadeDrive(stickLeft);
//            } else {
            double right, left;
            right = stickRight.getY();
            left = stickLeft.getY();
            System.out.println("The right is value " + right
                    + ", the left value is " + left
                    + "The gyro value is " + gyro.getAngle());
            drive.tankDrive(left, right);
            //           drive.tankDrive(stickLeft.getY(), stickRight.getY());
//            }
        }
    }
}
