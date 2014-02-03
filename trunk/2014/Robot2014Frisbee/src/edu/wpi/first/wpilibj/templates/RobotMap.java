package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;


/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    public static final int JOYSTICK_LEFT = 1;
    public static final int JOYSTICK_RIGHT = 2;
    public static final int DRIVEMODE_TANK = 0;
    public static final int DRIVEMODE_ARCADE = 1;
    
    public static Talon DRIVETRAIN_LEFTDRIVE_TALON;
    public static Talon DRIVETRAIN_RIGHTDRIVE_TALON;
    public static RobotDrive DRIVETRAIN_ROBOT_DRIVE_2;
    public static Encoder DRIVETRAIN_LEFTENCODER;
    public static Encoder DRIVETRAIN_RIGHTENCODER;
    public static AnalogSonar DRIVETRAIN_FRONTSONAR;
    public static AnalogSonar DRIVETRAIN_SIDESONAR; 
    public static Gyro DRIVETRAIN_GYRO;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static final int rangefinderPort = 1;
    // public static final int rangefinderModule = 1;
    public static void init() {
        DRIVETRAIN_LEFTDRIVE_TALON = new Talon(1, 1); //old 1,4
        DRIVETRAIN_RIGHTDRIVE_TALON = new Talon(1, 2); //old 1,3

        
        DRIVETRAIN_LEFTENCODER = new Encoder(1, 12, 1, 11, true, CounterBase.EncodingType.k4X); //1,3,1,4
        DRIVETRAIN_RIGHTENCODER = new Encoder(1, 14, 1, 13, false, CounterBase.EncodingType.k4X);  //1,1,1,2

        DRIVETRAIN_GYRO = new Gyro(1, 1);
        DRIVETRAIN_GYRO.setSensitivity(0.007);

        DRIVETRAIN_FRONTSONAR = new AnalogSonar(1,6);
        DRIVETRAIN_SIDESONAR = new AnalogSonar(1,7);

        
        DRIVETRAIN_ROBOT_DRIVE_2 = new RobotDrive(DRIVETRAIN_LEFTDRIVE_TALON, DRIVETRAIN_RIGHTDRIVE_TALON);
        DRIVETRAIN_ROBOT_DRIVE_2.setSafetyEnabled(false);
        DRIVETRAIN_ROBOT_DRIVE_2.setExpiration(0.1);
        DRIVETRAIN_ROBOT_DRIVE_2.setSensitivity(0.5);
        DRIVETRAIN_ROBOT_DRIVE_2.setMaxOutput(1.0);
        
        
    }
    
}
