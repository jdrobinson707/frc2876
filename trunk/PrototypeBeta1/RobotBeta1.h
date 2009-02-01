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
		
		TrackingThreshold tt1 /*PINK*/, tt2 /*GREEN*/;		// these are the 2 colors to track 		
		ParticleAnalysisReport pa1, pa2;	//Particle Analysis Report
		
		void driveStrait(long maxTime);
		void turn90Right();
		void turn130Left(); 
        void turnDeg(double degrees);
        void turnRad(double radians);
		void resetGyro();
		void TestCamera();
		void initializeColors();
		void recieveAndReactToCameraData();
};

#endif
