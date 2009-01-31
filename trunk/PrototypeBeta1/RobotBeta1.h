#ifndef _ROBOTBETA1_H
#define _ROBOTBETA1_H

class RobotBeta1 : public SimpleRobot {
public:
		RobotBeta1(void);
		void Autonomous(void);
		void OperatorControl(void);
		void RobotMain(void);
		void UpdateDashboard(void);
		
private:
		RobotDrive *itsDrive;
		Joystick *joystickUSB1;
		Joystick *joystickUSB2;
		Gyro *gyro;
		
		PCVideoServer *pc;
		DashboardDataFormat *dashboardDataFormat;
		
		void driveStrait(long maxTime);
		void turn90Right();
		void turn130Left(); 
		void resetGyro();
		void TestCamera();
		
};

#endif
