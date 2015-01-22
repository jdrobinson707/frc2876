package org.usfirst.frc.team1234.robot;

import edu.wpi.first.wpilibj.*;
//import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class RobotDrive2876 implements MotorSafety {	

	protected MotorSafetyHelper m_safetyHelper;

	/**
	 * The location of a motor on the robot for the purpose of driving
	 */
	public static class MotorType {

		/**
		 * The integer value representing this enumeration
		 */
		public final int value;
		static final int kFrontLeft_val = 0;
		static final int kFrontRight_val = 1;
		static final int kRearLeft_val = 2;
		static final int kRearRight_val = 3;
		/**
		 * motortype: front left
		 */
		public static final MotorType kFrontLeft = new MotorType(kFrontLeft_val);
		/**
		 * motortype: front right
		 */
		public static final MotorType kFrontRight = new MotorType(kFrontRight_val);
		/**
		 * motortype: rear left
		 */
		public static final MotorType kRearLeft = new MotorType(kRearLeft_val);
		/**
		 * motortype: rear right
		 */
		public static final MotorType kRearRight = new MotorType(kRearRight_val);

		private MotorType(int value) {
			this.value = value;
		}
	}
	public static final double kDefaultExpirationTime = 0.1;
	public static final double kDefaultSensitivity = 0.5;
	public static final double kDefaultMaxOutput = 1.0;
	protected static final int kMaxNumberOfMotors = 4;
	protected final int m_invertedMotors[] = new int[4];
	protected double m_sensitivity;
	protected double m_maxOutput;
	protected SpeedController m_frontLeftMotor;
	protected SpeedController m_frontRightMotor;
	protected SpeedController m_rearLeftMotor;
	protected SpeedController m_rearRightMotor;
	protected boolean m_allocatedSpeedControllers;
	protected byte m_syncGroup = 0;
	protected static boolean kArcadeRatioCurve_Reported = false;
	protected static boolean kTank_Reported = false;
	protected static boolean kArcadeStandard_Reported = false;
	protected static boolean kMecanumCartesian_Reported = false;
	protected static boolean kMecanumPolar_Reported = false;

	/**
	 * Constructor for RobotDrive with 4 motors specified with channel numbers.
	 * Set up parameters for a four wheel drive system where all four motor
	 * pwm channels are specified in the call.
	 * This call assumes Talons for controlling the motors.
	 * @param frontLeftMotor Front left motor channel number
	 * @param rearLeftMotor Rear Left motor channel number
	 * @param frontRightMotor Front right motor channel number
	 * @param rearRightMotor Rear Right motor channel number
	 */
	public RobotDrive2876(final int frontLeftMotor, final int rearLeftMotor,
			final int frontRightMotor, final int rearRightMotor) {
		m_sensitivity = kDefaultSensitivity;
		m_maxOutput = kDefaultMaxOutput;
		m_rearLeftMotor = new Talon(rearLeftMotor);
		m_rearRightMotor = new Talon(rearRightMotor);
		m_frontLeftMotor = new Talon(frontLeftMotor);
		m_frontRightMotor = new Talon(frontRightMotor);
		for (int i = 0; i < kMaxNumberOfMotors; i++) {
			m_invertedMotors[i] = 1;
		}
		m_allocatedSpeedControllers = true;
		setupMotorSafety();
		drive(0, 0);
	}

	/**
	 * Drive the motors at "speed" and "curve".
	 *
	 * The speed and curve are -1.0 to +1.0 values where 0.0 represents stopped and
	 * not turning. The algorithm for adding in the direction attempts to provide a constant
	 * turn radius for differing speeds.
	 *
	 * This function will most likely be used in an autonomous routine.
	 *
	 * @param outputMagnitude The forward component of the output magnitude to send to the motors.
	 * @param curve The rate of turn, constant for different forward speeds.
	 */
	public void drive(double outputMagnitude, double curve) {
		double leftOutput, rightOutput;

		if(!kArcadeRatioCurve_Reported) {
			UsageReporting.report(tResourceType.kResourceType_RobotDrive, getNumMotors(), tInstances.kRobotDrive_ArcadeRatioCurve);
			kArcadeRatioCurve_Reported = true;
		}
		if (curve < 0) {
			double value = Math.log(-curve);
			double ratio = (value - m_sensitivity) / (value + m_sensitivity);
			if (ratio == 0) {
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude / ratio;
			rightOutput = outputMagnitude;
		} else if (curve > 0) {
			double value = Math.log(curve);
			double ratio = (value - m_sensitivity) / (value + m_sensitivity);
			if (ratio == 0) {
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude / ratio;
		} else {
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude;
		}
		setLeftRightMotorOutputs(leftOutput, rightOutput);
	}

	/**
	 * Drive method for Mecanum wheeled robots.
	 *
	 * A method for driving with Mecanum wheeled robots. There are 4 wheels
	 * on the robot, arranged so that the front and back wheels are toed in 45 degrees.
	 * When looking at the wheels from the top, the roller axles should form an X across the robot.
	 *
	 * This is designed to be directly driven by joystick axes.
	 *
	 * @param x The speed that the robot should drive in the X direction. [-1.0..1.0]
	 * @param y The speed that the robot should drive in the Y direction.
	 * This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
	 * @param rotation The rate of rotation for the robot that is completely independent of
	 * the translation. [-1.0..1.0]
	 * @param gyroAngle The current angle reading from the gyro.  Use this to implement field-oriented controls.
	 */
	public void mecanumDrive_Cartesian(double x, double y, double rotation, double gyroAngle) {
		if(!kMecanumCartesian_Reported) {
			UsageReporting.report(tResourceType.kResourceType_RobotDrive, getNumMotors(), tInstances.kRobotDrive_MecanumCartesian);
			kMecanumCartesian_Reported = true;
		}
		double xIn = x;
		double yIn = y;
		// Negate y for the joystick.
		yIn = -yIn;
		// Compenstate for gyro angle.
		double rotated[] = rotateVector(xIn, yIn, gyroAngle);
		xIn = rotated[0];
		yIn = rotated[1];

		double wheelSpeeds[] = new double[kMaxNumberOfMotors];
//		wheelSpeeds[MotorType.kFrontLeft_val] = xIn + yIn + rotation;		
		wheelSpeeds[MotorType.kFrontLeft_val] = xIn + yIn - rotation;
		wheelSpeeds[MotorType.kFrontRight_val] = -xIn + yIn - rotation;
		wheelSpeeds[MotorType.kRearLeft_val] = -xIn + yIn + rotation;
//		wheelSpeeds[MotorType.kRearRight_val] = xIn + yIn - rotation;
		wheelSpeeds[MotorType.kRearRight_val] = xIn + yIn + rotation;
		
		normalize(wheelSpeeds);

		double frontLeftSPD = wheelSpeeds[MotorType.kFrontLeft_val] * m_invertedMotors[MotorType.kFrontLeft_val] * m_maxOutput;
		double frontRightSPD = wheelSpeeds[MotorType.kFrontRight_val] * m_invertedMotors[MotorType.kFrontRight_val] * m_maxOutput;
		double rearLeftSPD = wheelSpeeds[MotorType.kRearLeft_val] * m_invertedMotors[MotorType.kRearLeft_val] * m_maxOutput;
		double rearRightSPD = wheelSpeeds[MotorType.kRearRight_val] * m_invertedMotors[MotorType.kRearRight_val] * m_maxOutput;

		SmartDashboard.putNumber("Front Left Wheel Speed", frontLeftSPD);
		SmartDashboard.putNumber("Front Right Wheel Speed", frontRightSPD);
		SmartDashboard.putNumber("Rear Left Wheel Speed", rearLeftSPD);
		SmartDashboard.putNumber("Rear Right Wheel Speed", rearRightSPD);

		
		m_frontLeftMotor.set(frontLeftSPD, m_syncGroup);
		m_frontRightMotor.set(frontRightSPD, m_syncGroup);
		m_rearLeftMotor.set(rearLeftSPD, m_syncGroup);
		m_rearRightMotor.set(rearRightSPD, m_syncGroup);

		if (m_syncGroup != 0) {
			CANJaguar.updateSyncGroup(m_syncGroup);
		}

		if (m_safetyHelper != null) m_safetyHelper.feed();
	}
	
	public void testDrive() {
		m_frontLeftMotor.set(.5);
		m_frontRightMotor.set(.5);
		m_rearLeftMotor.set(-.5);
		m_rearRightMotor.set(-.5);
	}

	/** Set the speed of the right and left motors.
	 * This is used once an appropriate drive setup function is called such as
	 * twoWheelDrive(). The motors are set to "leftSpeed" and "rightSpeed"
	 * and includes flipping the direction of one side for opposing motors.
	 * @param leftOutput The speed to send to the left side of the robot.
	 * @param rightOutput The speed to send to the right side of the robot.
	 */
	public void setLeftRightMotorOutputs(double leftOutput, double rightOutput) {
		if (m_rearLeftMotor == null || m_rearRightMotor == null) {
			throw new NullPointerException("Null motor provided");
		}

		if (m_frontLeftMotor != null) {
			m_frontLeftMotor.set(limit(leftOutput) * m_invertedMotors[MotorType.kFrontLeft_val] * m_maxOutput, m_syncGroup);
		}
		m_rearLeftMotor.set(limit(leftOutput) * m_invertedMotors[MotorType.kRearLeft_val] * m_maxOutput, m_syncGroup);

		if (m_frontRightMotor != null) {
			m_frontRightMotor.set(-limit(rightOutput) * m_invertedMotors[MotorType.kFrontRight_val] * m_maxOutput, m_syncGroup);
		}
		m_rearRightMotor.set(-limit(rightOutput) * m_invertedMotors[MotorType.kRearRight_val] * m_maxOutput, m_syncGroup);

		if (this.m_syncGroup != 0) {
			CANJaguar.updateSyncGroup(m_syncGroup);
		}

		if (m_safetyHelper != null) m_safetyHelper.feed();
	}

	/**
	 * Limit motor values to the -1.0 to +1.0 range.
	 */
	protected static double limit(double num) {
		if (num > 1.0) {
			return 1.0;
		}
		if (num < -1.0) {
			return -1.0;
		}
		return num;
	}

	/**
	 * Normalize all wheel speeds if the magnitude of any wheel is greater than 1.0.
	 */
	protected static void normalize(double wheelSpeeds[]) {
		double maxMagnitude = Math.abs(wheelSpeeds[0]);
		int i;
		for (i=1; i<kMaxNumberOfMotors; i++) {
			double temp = Math.abs(wheelSpeeds[i]);
			if (maxMagnitude < temp) maxMagnitude = temp;
		}
		if (maxMagnitude > 1.0) {
			for (i=0; i<kMaxNumberOfMotors; i++) {
				wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
			}
		}
	}

	/**
	 * Rotate a vector in Cartesian space.
	 */
	protected static double[] rotateVector(double x, double y, double angle) {
		double cosA = Math.cos(angle * (3.14159 / 180.0));
		double sinA = Math.sin(angle * (3.14159 / 180.0));
		double out[] = new double[2];
		out[0] = x * cosA - y * sinA;
		out[1] = x * sinA + y * cosA;
		return out;
	}

	/**
	 * Invert a motor direction.
	 * This is used when a motor should run in the opposite direction as the drive
	 * code would normally run it. Motors that are direct drive would be inverted, the
	 * drive code assumes that the motors are geared with one reversal.
	 * @param motor The motor index to invert.
	 * @param isInverted True if the motor should be inverted when operated.
	 */
	public void setInvertedMotor(MotorType motor, boolean isInverted) {
		m_invertedMotors[motor.value] = isInverted ? -1 : 1;
	}

	/**
	 * Set the turning sensitivity.
	 *
	 * This only impacts the drive() entry-point.
	 * @param sensitivity Effectively sets the turning sensitivity (or turn radius for a given value)
	 */
	public void setSensitivity(double sensitivity) {
		m_sensitivity = sensitivity;
	}

	/**
	 * Configure the scaling factor for using RobotDrive with motor controllers in a mode other than PercentVbus.
	 * @param maxOutput Multiplied with the output percentage computed by the drive functions.
	 */
	public void setMaxOutput(double maxOutput) {
		m_maxOutput = maxOutput;
	}

	/**
	 * Set the number of the sync group for the motor controllers.  If the motor controllers are {@link CANJaguar}s,
	 * then they will all be added to this sync group, causing them to update their values at the same time.
	 *
	 * @param syncGroup the update group to add the motor controllers to
	 */
//	public void setCANJaguarSyncGroup(byte syncGroup) {
//		m_syncGroup = syncGroup;
//	}

	/**
	 * Free the speed controllers if they were allocated locally
	 */
//	public void free() {
//		if (m_allocatedSpeedControllers) {
//			if (m_frontLeftMotor != null) {
//				((PWM) m_frontLeftMotor).free();
//			}
//			if (m_frontRightMotor != null) {
//				((PWM) m_frontRightMotor).free();
//			}
//			if (m_rearLeftMotor != null) {
//				((PWM) m_rearLeftMotor).free();
//			}
//			if (m_rearRightMotor != null) {
//				((PWM) m_rearRightMotor).free();
//			}
//		}
//	}

	public void setExpiration(double timeout) {
		m_safetyHelper.setExpiration(timeout);
	}

	public double getExpiration() {
		return m_safetyHelper.getExpiration();
	}

	public boolean isAlive() {
		return m_safetyHelper.isAlive();
	}

	public boolean isSafetyEnabled() {
		return m_safetyHelper.isSafetyEnabled();
	}

	public void setSafetyEnabled(boolean enabled) {
		m_safetyHelper.setSafetyEnabled(enabled);
	}

	public String getDescription() {
		return "Robot Drive";
	}

	public void stopMotor() {
		if (m_frontLeftMotor != null) {
			m_frontLeftMotor.set(0.0);
		}
		if (m_frontRightMotor != null) {
			m_frontRightMotor.set(0.0);
		}
		if (m_rearLeftMotor != null) {
			m_rearLeftMotor.set(0.0);
		}
		if (m_rearRightMotor != null) {
			m_rearRightMotor.set(0.0);
		}
		if (m_safetyHelper != null) m_safetyHelper.feed();
	}

	private void setupMotorSafety() {
		m_safetyHelper = new MotorSafetyHelper(this);
		m_safetyHelper.setExpiration(kDefaultExpirationTime);
		m_safetyHelper.setSafetyEnabled(true);
	}

	protected int getNumMotors() {
		int motors = 0;
		if (m_frontLeftMotor != null) motors++;
		if (m_frontRightMotor != null) motors++;
		if (m_rearLeftMotor != null) motors++;
		if (m_rearRightMotor != null) motors++;
		return motors;
	}
}