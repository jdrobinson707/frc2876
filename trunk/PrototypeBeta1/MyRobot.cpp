#include "WPILib.h"
#if 0
/**
 * This is a demo program showing the use of the RobotBase class.
 * The SimpleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 */ 
class RobotDemo : public SimpleRobot
{
	RobotDrive myRobot; // robot drive system
	Joystick stick; // only joystick

public:
	RobotDemo(void):
		myRobot(1, 2),	// these must be initialized in the same order
		stick(1)		// as they are declared above.
	{
		printf("In RobotDemo ctor\n");
		GetWatchdog().SetExpiration(100);
		printf("End RobotDemo ctor\n");
	}

	/**
	 * Drive left & right motors for 2 seconds then stop
	 */
	void Autonomous(void)
	{
		printf("Start my autonomous\n");
		GetWatchdog().SetEnabled(false);
		myRobot.Drive(0.5, 0.0); 	// drive forwards half speed
		Wait(2.0); 				//    for 2 seconds
		myRobot.Drive(0.0, 0.0); 	// stop robot
		printf("End my autonomous\n");
	}

	/**
	 * Runs the motors with arcade steering. 
	 */
	void OperatorControl(void)
	{
		printf("Start my operatorcontrol\n");
		GetWatchdog().SetEnabled(true);
		while (IsOperatorControl())
		{
			GetWatchdog().Feed();
			myRobot.ArcadeDrive(stick); // drive with arcade style (use right stick)
		}
		printf("End my operatorcontrol\n");
	}
};

START_ROBOT_CLASS(RobotDemo);
#endif