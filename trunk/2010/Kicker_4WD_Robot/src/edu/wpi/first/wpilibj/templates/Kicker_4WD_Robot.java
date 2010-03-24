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
    int accelbutton;
    ColorImage image;
    AxisCamera camera;
    Servo pan;
    Servo tilt;
    Gyro gyro;
    Encoder eCam;
    Encoder eLeftDrive;
    Encoder eRightDrive;
    DigitalInput limSwitch;
    boolean rollerIsRolling;
    boolean flag;
    boolean intervalFlag = true;
    DigitalInput autonomousGreenSwitch;
    DigitalInput autonomousSilverSwitch;
    DriverStationLCD dslcd;
    boolean kickSwitch;
    long startTime = 0;


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

        // servos
        tilt = new Servo(Constants.PAN_SERVO);
        pan = new Servo(Constants.TILT_SERVO);

        // Gyro
        gyro = new Gyro(Constants.ANALOG_MODULE_SLOT, Constants.GYRO_CHANNEL_RATE);     // indicates port Number

        // encoder
        eCam = new Encoder(Constants.ENCODER_CAM_CHANNEL_A,
                Constants.ENCODER_CAM_CHANNEL_B);
        eLeftDrive = new Encoder(Constants.ENCODER_LEFT_DRIVE_CHANNEL_A,
                Constants.ENCODER_LEFT_DRIVE_CHANNEL_B, true);
        eRightDrive = new Encoder(Constants.ENCODER_RIGHT_DRIVE_CHANNEL_A,
                Constants.ENCODER_RIGHT_DRIVE_CHANNEL_B);

        //Limit switch
        limSwitch = new DigitalInput(Constants.LIMIT_SWITCH_CHANNEL);

        //Autonomous Switch
        autonomousSilverSwitch = new DigitalInput(Constants.DIGITAL_MODULE_SLOT,
                Constants.AUTONOMOUS_SILVER_SWITCH_CHANNEL);
        autonomousGreenSwitch = new DigitalInput(Constants.DIGITAL_MODULE_SLOT,
                Constants.AUTONOMOUS_GREEN_SWITCH_CHANNEL);


//        Watchdog.getInstance().feed();
        camera = AxisCamera.getInstance();
        camera.writeBrightness(0);
        camera.writeResolution(AxisCamera.ResolutionT.k320x240);
        // camera.writeResolution(AxisCamera.ResolutionT.k160x120);

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

        dslcd = DriverStationLCD.getInstance();
        eLeftDrive.setDistancePerPulse(.1); //inches per pulse
        eRightDrive.setDistancePerPulse(.1);
        Watchdog.getInstance().feed();
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

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        Watchdog.getInstance().feed();
        Watchdog.getInstance().setExpiration(10);
        System.out.println("In Auto");
        eCam.start();
        Watchdog.getInstance().feed();
        Timer.delay(.02);
        Watchdog.getInstance().feed();

        gyro.reset();

        System.out.println("" + autonomousGreenSwitch.get() + autonomousSilverSwitch.get());
        if (autonomousGreenSwitch.get() == true
                && autonomousSilverSwitch.get() == true) { //offense
            System.out.println("Offense");
            dslcd.println(DriverStationLCD.Line.kUser3, 1, "Offense");
            offensePush();
        } else if ((autonomousGreenSwitch.get() == true
                && autonomousSilverSwitch.get() == false)
                || (autonomousGreenSwitch.get() == false
                && autonomousSilverSwitch.get() == true)) { //mid-field
            System.out.println("Midfield");
            dslcd.println(DriverStationLCD.Line.kUser3, 1, "Midfield");
            midfieldPush();
        } else {    // defense
            System.out.println("Defense");
            dslcd.println(DriverStationLCD.Line.kUser3, 1, "Defense");
            midfieldPush();  //currently the code for defense is the same for midfield
        }
        Watchdog.getInstance().feed();

        eCam.stop();
        eLeftDrive.reset();
        eRightDrive.reset();
        eLeftDrive.stop();
        eRightDrive.stop();
        eCam.reset();
        gyro.reset();
    }

    public void offensePush() {
        eLeftDrive.reset();
        eRightDrive.reset();
        eLeftDrive.start();
        eRightDrive.start();
        //make sure kicker is pulled back!!
        roller.set(1.0);
        moveForwardXInches(132.0);
        turnXDegreesLeft(20.0);
        moveForwardXInches(60.0);
        roller.set(0.0);
        Timer.delay(1.0);
        kickBall_Auto();
        moveBackwardXInches(72.0);
        turnXDegreesLeft(80.0);
        moveBackwardXInches(48.0);
        eLeftDrive.stop();
        eRightDrive.stop();
        roller.set(1.0);
    }

    public void midfieldPush() {
        eLeftDrive.reset();
        eRightDrive.reset();
        eLeftDrive.start();
        eRightDrive.start();
        //make sure kicker is pulled back!!
        roller.set(1.0);
        moveForwardXInches(132.0);
        turnXDegreesRight(40.0);
        moveForwardXInches(72.0);
        roller.set(0.0);
        Timer.delay(1.0);
        kickBall_Auto();
        moveBackwardXInches(72.0);
        eLeftDrive.stop();
        eRightDrive.stop();
        roller.set(1.0);
    }

    public void turnXDegreesRight(double xdegrees) {  //NOTE:  CANNOT TURN LEFT!
        drive.setLeftRightMotorSpeeds(0.0, 0.0);
        gyro.reset();
        Watchdog.getInstance().feed();
        while (gyro.getAngle() < xdegrees) {
            drive.setLeftRightMotorSpeeds(-.4,.4);
            System.out.println("made it to 2");
            System.out.println("Gyro: " + gyro.getAngle());
            System.out.println("made it to 3");
        }
        drive.setLeftRightMotorSpeeds(0.0, 0.0);
        gyro.reset();
        Watchdog.getInstance().feed();
    }

    public void turnXDegreesLeft(double xdegrees) {  //NOTE:  CANNOT TURN RIGHT
        drive.setLeftRightMotorSpeeds(0.0, 0.0);
        gyro.reset();
        Watchdog.getInstance().feed();
        while (gyro.getAngle() > (-1 * xdegrees)) {
            drive.setLeftRightMotorSpeeds(.4,-.4);
            System.out.println("made it to 2");
            System.out.println("Gyro: " + gyro.getAngle());
            System.out.println("made it to 3");
        }
        drive.setLeftRightMotorSpeeds(0.0, 0.0);
        gyro.reset();
        Watchdog.getInstance().feed();
    }

    public void moveForwardXInches(double xinches) {
        drive.setLeftRightMotorSpeeds(0.0, 0.0);
        eLeftDrive.reset();
        eRightDrive.reset();
        Watchdog.getInstance().feed();
        while (eLeftDrive.getDistance() < xinches)  {
            System.out.println("EL:  " + eLeftDrive.getDistance());
            drive.setLeftRightMotorSpeeds(.3, .3);
        }
        drive.setLeftRightMotorSpeeds(0.0, 0.0);
        Watchdog.getInstance().feed();
        eLeftDrive.reset();
        eRightDrive.reset();
    }

    public void moveBackwardXInches(double xinches) {
        drive.setLeftRightMotorSpeeds(0.0, 0.0);
        eLeftDrive.reset();
        eRightDrive.reset();
        Watchdog.getInstance().feed();
        while (eLeftDrive.getDistance() > (-1 * xinches))  {
            System.out.println("EL:  " + eLeftDrive.getDistance());
            drive.setLeftRightMotorSpeeds(-.3, -.3);
        }
        drive.setLeftRightMotorSpeeds(0.0, 0.0);
        Watchdog.getInstance().feed();
        eLeftDrive.reset();
        eRightDrive.reset();
    }

    public void updateRoller() {
        readButtons(stickRight, rightButtons, "right");

        if (rightButtons[1] == true) {
            if (rollerIsRolling == false) {
                rollerIsRolling = true;
                roller.set(1.0);
            } else {
                rollerIsRolling = false;
                roller.set(0.0);
            }
        } else if (rightButtons[2] == true) {
            if (roller.get() == 0.0) {
                roller.set(-1.0);
            } else {
                roller.set(0.0);
            }
        }
    }

    /*    public void updateCam() {
    double speed = 0.0;

    readButtons(stickCopilot, copilotButtons, "copilot");

    if (copilotButtons[7] == true) {
    speed = cam.get();
    if (speed > 0.0) {
    speed = speed - .05;
    }
    cam.set(speed);
    System.out.println("CAM:  " + speed);
    }
    if (copilotButtons[6] == true) {
    speed = cam.get();
    if (speed < .8) {
    speed = speed + .05;
    }
    cam.set(speed);
    System.out.println("CAM:  " + speed);
    }
    if (copilotButtons[1] == true) {
    cam.set(0);
    }
    if (copilotButtons[2] == true) {
    eCam.reset();
    }
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
            eCam.setDistancePerPulse((btn - 1) * 100);
            System.out.println("=========================================");
            System.out.println(" strButton=" + strButton
                    + " speed=" + speed + " limit=" + limit + " tmp=" + tmp);

            eCam.start();
            dumpEncoderInfo(eCam);
            drive.setLeftRightMotorSpeeds(-speed, 0.0);
            while (eCam.get() < limit) {
                Watchdog.getInstance().feed();
            }
            drive.setLeftRightMotorSpeeds(0.0, 0.0);
            eCam.stop();
            dumpEncoderInfo(eCam);
            int counts = eCam.get();
            totalKickRotations += counts;
            System.out.println("EncoderRotationCount:  "
                    + counts
                    + "  totalKickRotations: " + totalKickRotations);
            eCam.reset();
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
        readButtons(stickCopilot, copilotButtons, "copilot");
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
                cam.set(.5);
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

    public void kickBall_Neil() {
        readButtons(stickCopilot, copilotButtons, "copilot");

        if (copilotButtons[1]) {
            if (limSwitch.get() == true) {
                //go through one cyle until true again
                while (limSwitch.get() == true) {
                    cam.set(9.0);
                }
                while (limSwitch.get() == false) {
                    cam.set(9.0);
                }
            } else {
                //go through one cyle until true again
                while (limSwitch.get() == false) {
                    cam.set(9.0);
                }
            }
        } else {
            cam.set(.3);
        }
    }

    public void kickBall_Auto() {
       Watchdog.getInstance().feed();
       if (limSwitch.get() == true) {
            //go through one cyle until true again
            while (limSwitch.get() == true) {
                cam.set(.5);
            }
            Watchdog.getInstance().feed();
            while (limSwitch.get() == false) {
                cam.set(.5);
            }
        } else {
            //go through one cyle until true again
            while (limSwitch.get() == false) {
                cam.set(.5);
            }
        }
       cam.set(0.0);
    }

    public void operatorControl() {
        Watchdog.getInstance().feed();
        Watchdog.getInstance().setExpiration(3.0);
        System.out.println("In Operator control");
        initializeButtons();
        userOptions();
        System.out.println(dslcd.kLineLength);


        eCam.reset();
        eLeftDrive.reset();
        eRightDrive.reset();
        eLeftDrive.start();
        eRightDrive.start();
        dumpEncoderInfo(eCam);
        kickSwitch = limSwitch.get();
        int stuff3 = 1;

        while (isOperatorControl() && isEnabled()) {
            Watchdog.getInstance().feed();
            Timer.delay(0.005);
            updateDashboard();
            // kickApoo(eCam);
            updateRoller();
            //updateCam();
            kickBall();
            dslcd.updateLCD();
            if (driveMode == Constants.ARCADE_DRIVE) {
                drive.arcadeDrive(stickLeft);
            } else {
                drive.tankDrive(stickLeft.getY(), stickRight.getY());
            }
            stuff3++;
            if (stuff3 % 100 == 0)
                System.out.println("EL:" + eLeftDrive.getRaw() + "\t" + "ER:" + eRightDrive.getRaw());
        }
    }
}
