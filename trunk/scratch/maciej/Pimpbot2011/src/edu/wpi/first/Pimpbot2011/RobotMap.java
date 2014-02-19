package edu.wpi.first.Pimpbot2011;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static final int leftMotor = 1;
    // public static final int rightMotor = 2;

    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static final int rangefinderPort = 1;
    // public static final int rangefinderModule = 1;
    public static final int ANALOG_SLOT = 1;
    public static final int DIGITAL_SLOT = 1;
    public static final int SOLENOID_SLOT = 1;

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
    
    public static final int POT_CHANNEL = 3;
    public static final int PRESSURE_SWITCH_CHANNEL = 11;
    public static final int COMPRESSOR_RELAY_CHANNEL = 2;
    public static final int ARM_SOLENOID_1_CHANNEL = 1;
    public static final int ARM_SOLENOID_2_CHANNEL = 2;
    public static final int GRIP_SOLENOID_1_CHANNEL = 3;
    
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

    public static final int ULTRASONIC = 1;
    public static final int GYRO_ANGLE = 2;
    public static final int GYRO_TEMP = 1;
    
    public static double roundtoTwo(double num) {
        return Math.floor(num * 100.0) / 100.0;
    }

}
