
package org.usfirst.frc.team1234.robot;


import edu.wpi.first.wpilibj.AnalogInput;
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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
		myRobot.setInvertedMotor(MotorType.kFrontLeft, true);
		myRobot.setInvertedMotor(MotorType.kFrontRight, true);
		myRobot.setExpiration(0.1);
		stick = new XboxController(0);
		AnalogInput analog = new AnalogInput(0);
		gyro = new Gyro(analog);
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
		boolean fieldOfViewToggle=false;
		boolean lastBPress=false;
		boolean lastStartPress=false;
		boolean lastYPress=false;
		boolean lastAPress=false;
		boolean lastXPress=false;
		double a = .75;
		double lastMotor = stick.getLeftX(), lastX = 0, stepX = .01;
		while (isOperatorControl() && isEnabled()){
			double x = stick.getLeftX();
			double y = stick.getLeftY();
			double rotation = stick.getRightX();
			double angle = gyro.getAngle();
			boolean isBPressed = stick.getButton(XboxController.BUTTON_B);
			boolean isStartPressed = stick.getButton(XboxController.BUTTON_START);
			boolean isAPressed = stick.getButton(XboxController.BUTTON_A);
			boolean isYPressed = stick.getButton(XboxController.BUTTON_Y);
			boolean isXPressed = stick.getButton(XboxController.BUTTON_X);
			if (isStartPressed && !lastStartPress){
				fieldOfViewToggle = !fieldOfViewToggle;
				lastStartPress=true;
			}
			lastStartPress=isStartPressed;
			if (isBPressed && !lastBPress){
				gyro.reset();
				lastBPress=true;
			}
			lastBPress=isBPressed;
			if (isAPressed){ //linear joystick input
				a=0; 
				lastAPress=true;
			}
			lastAPress=isAPressed;
			if (isXPressed){
				a=.75; 
				lastXPress=true;
			}
			lastXPress=isXPressed;
			if (isYPressed){
				a=1; 
				lastYPress=true;
			}
			lastYPress=isYPressed;
			
			x = a * (Math.pow(x, 3)) + x * (1 - a);
			
//			this is an example of coercion (motor will never go faster than x (.5))
//			if (x>.5) x=.5;
//			if (x<-.5) x=-.5;
//			if (y>.5) y= .5;
//			if (y<-.5) y=-.5;
//			
//			this is an example of coercion that doesn't limit the joystick's capabilities
//			x*=.5;
//			y*=.5;
			//y = a(x^3) + (1-a)x  0<=a<=1

			
//			if close, make them the same, otherwise add/subtract .1 (stepX)  
//			if (Math.abs(x - lastMotor) < stepX) {
//				lastMotor = x;
//			}
//			else if (lastX < x) {
//				lastMotor += stepX;
//			}
//			else if (lastX > x) {
//				lastMotor -= stepX;
//			}
//			x=lastMotor;
//			lastX = x;
//			
			lastBPress=isBPressed;
			if (fieldOfViewToggle) {
				myRobot.mecanumDrive_Cartesian(x, y, rotation, angle);
			}

			
			myRobot.mecanumDrive_Cartesian(x, y, rotation, 0);
			//          myRobot.arcadeDrive(y, 0, false);
			SmartDashboard.putNumber("Gyro Rate", gyro.getRate());
			SmartDashboard.putNumber("Gyro pidGet", gyro.pidGet());
			SmartDashboard.putNumber("Gyro Angle", angle);
			SmartDashboard.putBoolean("FOV On", fieldOfViewToggle);
			SmartDashboard.putNumber("Left Stick X", x);
			SmartDashboard.putNumber("Left Stick Y", y);
			SmartDashboard.putNumber("Right Stick X", rotation);
			SmartDashboard.putNumber("Motor Speed", lastMotor);
			SmartDashboard.putNumber("Value of A", a); //0<=a<=1 (0=linear speed slope & 1=cubed speed slope)
			Timer.delay(0.005);		// wait for a motor update time

		}
		gyro.reset();
	}


	/**
	 * Runs during test mode
	 */
	public void test() {
	}
}
