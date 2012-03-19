package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;

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
    //public static final int LEFT_DRIVE_MOTOR_PORT = 1;
    public static final int LEFT_DRIVE_MOTOR_PORT = 3;
    //public static final int RIGHT_DRIVE_MOTOR_PORT = 3;
    public static final int RIGHT_DRIVE_MOTOR_PORT = 2;
    public static final int CONVEYOR_LOW_PORT = 1;
    public static final int CONVYOR_HIGH_PORT = 2;
    public static final int BRIDGE_ARM_PORT = 4;
    public static final int GYRO_PORT = 2;
    //public static final int GYRO_PORT = 1;
    public static final int SHOOTER_PORT = 5;
    //public static final int SHOOTER_PORT = 2;
    public static final int LINE_TRACKER_PORT = 1;
    public static final int PONTENTIOMETER_PORT = 1;
    //public static final int BRIDGE_ARM_UP_LIMIT = 621;
    //public static final int BRIDGE_ARM_DOWN_LIMIT = 0;
    public static final int BRIDGE_ARM_UP_LIMIT = 622;
    public static final int BRIDGE_ARM_DOWN_LIMIT = -3;

//    public static final int MANUAL_SHOOT_B = 1;
//    public static final int CONVEYOR_LOW_ON_B = 3;
//    public static final int CONVEYOR_LOW_REVERSE_B = 2;
//    public static final int CONVEYOR_HIGH_ON_B = 4;
//    public static final int CONVEYOR_HIGH_REVERSE_B = 5;
//    public static final int DRIVE_REVERSE_B = 6;
//    public static final int DRIVE_FORWARD_B = 7;
//    public static final int AUTO_SHOOT_TOP_B = 9;
//    public static final int AUTO_SHOOT_LEFT_B = 8;
    public static final int JOYSTICK_LEFT = 1;
    public static final int JOYSTICK_RIGHT = 2;
    public static final int JOYSTICK_EXTRA = 3;

    public static final int LM_LOW = 4;
    public static final int LM_MIDDLE = 2;
    public static final int LM_HIGH = 3;
    public static final double KEY_TOP_SHOOT_RPS = 17.4;
    public static final double FAR_SPEED_RPS = 27.5;
    public static final double START_SPEED_RPS = 0.61;

    public static final double DRIVE_FORWARD = 1;
    public static final double DRIVE_REVERSE = -1;

    public static double roundtoTwo(double num) {
        return Math.floor(num * 100.0) / 100.0;
    }

    public static int relayValToInt(Relay.Value val) {
        if (Relay.Value.kForward == val) {
            return 1;
        } else if (Relay.Value.kReverse == val) {
            return -1;
        } else if (Relay.Value.kOff == val) {
            return 0;
        }
        return 0;
    }
}
