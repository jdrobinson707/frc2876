//Contributers:  Neil Patel, Maciej, Eddy Louis, Gerry Porthier
//Started:  1/18/09
//Last Updated:  1/27/09
//BHS ROBOTIX TEAM

#include <stdio.h>
#include <iostream.h>

#include <WPILib.h>
#include <Dashboard.h>
#include <VisionAPI.h>
#include <AxisCamera.h>
#include <FrcError.h>
#include <PCVideoServer.h>
#include "DashboardDataFormat.h"

using namespace std;

// #define DBG(fmt, args...) printf("%s:%d:%s:"fmt, __FILE__, __LINE__, __func__, args);
//#define DBG(fmt, args...) printf(fmt);
#define DBG printf

class RobotBeta1 : public SimpleRobot {
	//member variables
	
	public:
		//constructors
		RobotBeta1(void) :
			itsDrive(1,2),
			joystickUSB1(1),
			joystickUSB2(2),
			gyro(1),
			sonar(3, 3)
		{
			printf("\n\nInitializing RobotBeta1...\n");
			// GetWatchdog().SetExpiration(1000);

			if (StartCameraTask() == -1) { 
				DBG("Failed to spawn camera task; Error code %s\n", 
							GetVisionErrorText(GetLastVisionError()) ); 
			} else {
				 pc = new PCVideoServer();
			}

			printf("Done\n\n");
		}
		
		//public methods
		void Autonomous(void);
		void OperatorControl(void);
		void RobotMain(void);
		void UpdateDashboard(void);
		
		
	private:
		RobotDrive itsDrive;
		Joystick joystickUSB1;
		Joystick joystickUSB2;
		Gyro gyro;
		Ultrasonic sonar;
		PCVideoServer *pc;
		DashboardDataFormat dashboardDataFormat;
		
		void driveStrait(long maxTime);
		void turn90Right();
		void turn130Left(); 
		void resetGyro();
		void TestCamera();
		
};


void RobotBeta1::Autonomous(void) {
	printf("\nStarting Autonomous...Trial 15\n");
	while(IsAutonomous()) {
#if 0
		printf("is enabled: %s\n", (sonar.IsEnabled() == true) ? "true" : "false");
		printf("is valid: %s\n", (sonar.IsRangeValid() == true) ? "true" : "false");
		printf("range in: %f\n", sonar.GetRangeInches());
#endif
#if 0
		GetWatchdog().Feed();
		driveStrait(500);
		GetWatchdog().Feed();
		turn130Left();
		itsDrive.Drive(0, 0);
#endif
		GetWatchdog().Feed();
		Wait(0.4);

	}
	printf("\nEnd Autonomous Mode\n");	
}

void RobotBeta1::OperatorControl(void) {
	int slowDownProccessing = 0;
	printf("\nStart Operator Control...\n");
	
	resetGyro();
	
	TestCamera();
	
	GetWatchdog().SetEnabled(true);
	while (IsOperatorControl())  {
		GetWatchdog().Feed();
		itsDrive.TankDrive(joystickUSB1,joystickUSB2);
		if ((slowDownProccessing++ % 25) == 0) {
			printf("\r\t\tGyro Angle:\t%f", gyro.GetAngle());
		}
		Wait(0.006);
	}
	printf("\nEnd Operator Control\n");
}
void RobotBeta1::driveStrait(long maxTime) {
	int slowDownProccessing = 0;
	long cTime = 0;
	
	resetGyro();
	float angle = 0;
	angle = gyro.GetAngle(); // get heading
	GetWatchdog().SetEnabled(true);
	
	
	while ((IsAutonomous()) && (cTime <= maxTime))  { 
		GetWatchdog().Feed();
		float angle = gyro.GetAngle(); // get heading
		if ((slowDownProccessing % 250) == 0) {
			printf("\n\t\tGyro Angle:  %f\t\t\tDrive Adj:  %f", gyro.GetAngle(), (angle * 0.03));
			cout << "\n\t\tTime:  "; cout << cTime; cout << "\t\t\tExit:  "; cout << maxTime; cout << "\n";
		}
		slowDownProccessing++; cTime++;
		itsDrive.Drive(-.5, (angle * 0.03));// turn to correct heading 
		Wait(0.004); 
	}
	itsDrive.Drive(0.0, 0.0);
}

void RobotBeta1::turn90Right(void) {
	float cGyroAngle = 0.0;
	const float maxAngle = 47.3;

	while((cGyroAngle <= maxAngle) && (IsAutonomous())) {
		cout << "Here\t\t"; cout << "Exit:  "; cout << (cGyroAngle <= maxAngle); cout << "\r";
		itsDrive.Drive(-.25, -1);
		Wait(0.006);
		GetWatchdog().Feed();
		cGyroAngle = gyro.GetAngle();
	}
}

void RobotBeta1::turn130Left(void) {
	float cGyroAngle;
	cGyroAngle = 0.0;
	const float maxAngle = 66.67;
	
	while((cGyroAngle <= maxAngle) && (IsAutonomous())) {
			cout << "Here\t\t"; cout << "Exit:  "; cout << (cGyroAngle <= maxAngle); cout << "\r";
			itsDrive.Drive(-.5, -1);
			Wait(0.006);
			GetWatchdog().Feed();
			cGyroAngle = gyro.GetAngle();
	}
}

void RobotBeta1::resetGyro(void) {
	DBG("Enter\n");
	float angle;
	do {
		gyro.Reset();
		angle = gyro.GetAngle();
		printf("calibrate angle %f\r", angle);
		GetWatchdog().Feed();
		Wait(0.1);
		GetWatchdog().Feed();
	} while (((int)angle) != 0);
	DBG("\nExit\n");
}

void RobotBeta1::TestCamera(void) {
	Image *image;
	double timestamp;
	DBG("Enter");
	image = frcCreateImage(IMAQ_IMAGE_RGB);
	if (image == NULL) {
		DBG("failed to create image %s\n", GetVisionErrorText(GetLastVisionError()));
	}
	if (!GetImage(image, &timestamp)) { 
		DBG("error: %s", GetVisionErrorText(GetLastVisionError()));
	}
}

void RobotBeta1::RobotMain(void)
{

	Dashboard &dashboard = m_ds->GetDashboardPacker();
	INT32 i=0;
	while (true)
	{
		GetWatchdog().Feed();
		//myRobot.ArcadeDrive(stick); // drive with arcade style (use right stick)
		dashboard.Printf("It's been %f seconds, according to the FPGA.\n", GetClock());
		dashboard.Printf("Iterations: %d\n", ++i);
		UpdateDashboard();
		Wait(0.02);
	}
}

    void RobotBeta1::UpdateDashboard(void) {
	static float num = 0.0;
		dashboardDataFormat.m_AnalogChannels[0][0] = num;
		dashboardDataFormat.m_AnalogChannels[0][1] = 5.0 - num;
		dashboardDataFormat.m_DIOChannels[0]++;
		dashboardDataFormat.m_DIOChannelsOutputEnable[0]--;
		num += 0.01;
		if (num > 5.0) num = 0.0;
		dashboardDataFormat.PackAndSend();
}


 
START_ROBOT_CLASS(RobotBeta1);
