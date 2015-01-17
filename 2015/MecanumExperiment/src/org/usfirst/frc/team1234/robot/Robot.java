
package org.usfirst.frc.team1234.robot;


import edu.wpi.first.wpilibj.Gyro;
import org.usfirst.frc.team1234.robot.RobotDrive2876.MotorType;
//import edu.wpi.first.wpilibj.communication.UsageReporting;
//import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
//import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
//import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.SampleRobot;
import org.usfirst.frc.team1234.robot.RobotDrive2876;
//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
    RobotDrive2876 myRobot;
    XboxController stick;
    public final static int MOTOR_FRONT_L = 5;
    public final static int MOTOR_REAR_L = 2;
    public final static int MOTOR_FRONT_R = 4;
    public final static int MOTOR_REAR_R = 3;
    
    protected static final int kMaxNumberOfMotors = 4;//Copied from RobotDrive.class
    
    Gyro gyro;
    
    public Robot() {
        myRobot =  new RobotDrive2876(MOTOR_FRONT_L, MOTOR_REAR_L,
                MOTOR_FRONT_R, MOTOR_REAR_R);
//        myRobot.setInvertedMotor(MotorType.kRearLeft, true);
//        myRobot.setInvertedMotor(MotorType.kRearRight, true);
        myRobot.setInvertedMotor(MotorType.kFrontLeft, true);
        myRobot.setInvertedMotor(MotorType.kFrontRight, true);
        myRobot.setExpiration(0.1);
        stick = new XboxController(0);
        gyro = new Gyro(0);
        gyro.initGyro();
    }

    /**
     * Drive left & right motors for 2 seconds then stop
     */
    public void autonomous() {
        myRobot.setSafetyEnabled(false);
        myRobot.drive(-0.5, 0.0);	// drive forwards half speed
        Timer.delay(2.0);		//    for 2 seconds
        myRobot.drive(0.0, 0.0);	// stop robot
    }

    /**
     * Runs the motors with arcade steering.
     */
    public void operatorControl() {
        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	double x = stick.getLeftX();
            double y = stick.getLeftY();
            double rotation = stick.getRightX();
            double g = gyro.getAngle();
            myRobot.mecanumDrive_Cartesian(x, y, rotation, 0);
//            myRobot.arcadeDrive(y, 0, false);
            Timer.delay(0.005);		// wait for a motor update time
        }
    }

    /**
     * Runs during test mode
     */
    public void test() {
    }
//    /**
//     * Drive method for Mecanum wheeled robots.
//     *
//     * A method for driving with Mecanum wheeled robots. There are 4 wheels
//     * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
//     * When looking at the wheels from the top, the roller axles should form an X across the robot.
//     *
//     * This is designed to be directly driven by joystick axes.
//     *
//     * @param x The speed that the robot should drive in the X direction. [-1.0..1.0]
//     * @param y The speed that the robot should drive in the Y direction.
//     * This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
//     * @param rotation The rate of rotation for the robot that is completely independent of
//     * the translation. [-1.0..1.0]
//     * @param gyroAngle The current angle reading from the gyro.  Use this to implement field-oriented controls.
//     */
//    private void mecanumDrive_Cartesian(double x, double y, double rotation, double gyroAngle) {
////        if(!kMecanumCartesian_Reported) {
////            UsageReporting.report(tResourceType.kResourceType_RobotDrive, getNumMotors(), tInstances.kRobotDrive_MecanumCartesian);
////            kMecanumCartesian_Reported = true;
////        }
//        double xIn = x;
//        double yIn = y;
//        // Negate y for the joystick.
//        yIn = -yIn;
//        // Compenstate for gyro angle.
//        double rotated[] = rotateVector(xIn, yIn, gyroAngle);
//        xIn = rotated[0];
//        yIn = rotated[1];
//
//        double wheelSpeeds[] = new double[kMaxNumberOfMotors];
//        int kFrontLeft_val = 0;//MotorType.kFrontLeft_val
//        int kFrontRight_val = 1;//MotorType.kFrontRight_val
//        int kRearLeft_val = 2;//MotorType.kRearLeft_val
//        int kRearRight_val = 3;//MotorType.kRearRight_val
//        
//        wheelSpeeds[kFrontLeft_val] = xIn + yIn + rotation;
//        wheelSpeeds[kFrontRight_val] = -xIn + yIn - rotation;
//        wheelSpeeds[kRearLeft_val] = -xIn + yIn + rotation;
//        wheelSpeeds[kRearRight_val] = xIn + yIn - rotation;
//
//        normalize(wheelSpeeds);
//        m_frontLeftMotor.set(wheelSpeeds[MotorType.kFrontLeft_val] * m_invertedMotors[MotorType.kFrontLeft_val] * m_maxOutput, m_syncGroup);
//        m_frontRightMotor.set(wheelSpeeds[MotorType.kFrontRight_val] * m_invertedMotors[MotorType.kFrontRight_val] * m_maxOutput, m_syncGroup);
//        m_rearLeftMotor.set(wheelSpeeds[MotorType.kRearLeft_val] * m_invertedMotors[MotorType.kRearLeft_val] * m_maxOutput, m_syncGroup);
//        m_rearRightMotor.set(wheelSpeeds[MotorType.kRearRight_val] * m_invertedMotors[MotorType.kRearRight_val] * m_maxOutput, m_syncGroup);
//
//        if (m_syncGroup != 0) {
//            CANJaguar.updateSyncGroup(m_syncGroup);
//        }
//
//        if (m_safetyHelper != null) m_safetyHelper.feed();
//    }
//    protected static double[] rotateVector(double x, double y, double angle) {
//        double cosA = Math.cos(angle * (3.14159 / 180.0));
//        double sinA = Math.sin(angle * (3.14159 / 180.0));
//        double out[] = new double[2];
//        out[0] = x * cosA - y * sinA;
//        out[1] = x * sinA + y * cosA;
//        return out;
//    }
//    protected static void normalize(double wheelSpeeds[]) {
//        double maxMagnitude = Math.abs(wheelSpeeds[0]);
//        int i;
//        for (i=1; i<kMaxNumberOfMotors; i++) {
//            double temp = Math.abs(wheelSpeeds[i]);
//            if (maxMagnitude < temp) maxMagnitude = temp;
//        }
//        if (maxMagnitude > 1.0) {
//            for (i=0; i<kMaxNumberOfMotors; i++) {
//                wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
//            }
//        }
//    }
}
