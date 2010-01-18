//Contributers:   Maciej, Eddy Louis, Gerry Porthier
//Started:  1/18/09
//Last Updated:  1/27/09
//BHS ROBOTIX TEAM

//basic libraries
#include <stdio.h>
#include <iostream.h>
#include <Math.h>
//basic robot libraries
#include <WPILib.h>
#include <Dashboard.h>
#include "DashboardDataFormat.h"
#include "BaeUtilities.h"
//Camera libraries
#include "AxisCamera.h" 
#include "BaeUtilities.h"
#include "FrcError.h"
#include "TrackAPI.h"
#include <VisionAPI.h>
#include <PCVideoServer.h>
//Libraries provided by other Demos
#include "Target.h"
//self made libraries
#include "Math2.h"
#include "RobotBeta1.h"

// using namespace std;

#define DBG if (dbg_flag)printf 

void RobotBeta1::driveStrait(long maxTime) {
	int slowDownProccessing = 0;
	long cTime = 0;
	
	resetGyro();
	float angle = 0;
	angle = gyro->GetAngle(); // get heading
	GetWatchdog().SetEnabled(true);
	
	
	while ((IsAutonomous()) && (cTime <= maxTime))  { 
		GetWatchdog().Feed();
		float angle = gyro->GetAngle(); // get heading
		if ((slowDownProccessing % 250) == 0) {
			DBG("\n\t\tGyro Angle:  %f\t\t\tDrive Adj:  %f", gyro->GetAngle(), (angle * 0.03));
			cout << "\n\t\tTime:  "; cout << cTime; cout << "\t\t\tExit:  "; cout << maxTime; cout << "\n";
		}
		slowDownProccessing++; cTime++;
		robotDrive->Drive(-.5, (angle * 0.03));// turn to correct heading 
		Wait(0.004); 
	}
	robotDrive->Drive(0.0, 0.0);
}

void RobotBeta1::turn90Right(void) {
	float cGyroAngle = 0.0;
	const float maxAngle = 47.3;

	while((cGyroAngle <= maxAngle) && (IsAutonomous())) {
		cout << "Here\t\t"; cout << "Exit:  "; cout << (cGyroAngle <= maxAngle); cout << "\r";
		robotDrive->Drive(-.25, -1);
		Wait(0.006);
		GetWatchdog().Feed();
		cGyroAngle = gyro->GetAngle();
	}
}

void RobotBeta1::turn130Left(void) {
	float cGyroAngle;
	cGyroAngle = 0.0;
	const float maxAngle = 66.67;
	
	while((cGyroAngle <= maxAngle) && (IsAutonomous())) {
			cout << "Here\t\t"; cout << "Exit:  "; cout << (cGyroAngle <= maxAngle); cout << "\r";
			robotDrive->Drive(-.5, -1);
			Wait(0.006);
			GetWatchdog().Feed();
			cGyroAngle = gyro->GetAngle();
	}
}
void RobotBeta1::turnDeg(double degrees) {
	double radians = 0;
	radians = degreesToRadians(degrees);
    float cGyroAngle = 0.0;
    const float maxAngle = radians;

    while((cGyroAngle <= maxAngle) && (IsAutonomous())) {
            cout << "Here\t\t"; cout << "Exit:  "; cout << (cGyroAngle <= maxAngle); cout << "\r";
            robotDrive->Drive(-.25, -1);
            Wait(0.006);
            GetWatchdog().Feed();
            cGyroAngle = gyro->GetAngle();
    }
}

void RobotBeta1::turnRad(double radians) {
    float cGyroAngle = 0.0;
    const float maxAngle = radians;

    while((cGyroAngle <= maxAngle) && (IsAutonomous())) {
            cout << "Here\t\t"; cout << "Exit:  "; cout << (cGyroAngle <= maxAngle); cout << "\r";
            robotDrive->Drive(-.25, -1);
            Wait(0.006);
            GetWatchdog().Feed();
            cGyroAngle = gyro->GetAngle();
    }
}

void RobotBeta1::resetGyro(void) {
	DBG("Enter\n");
	float angle;
	do {
		gyro->Reset();
		angle = gyro->GetAngle();
		DBG("calibrate angle %f\r", angle);
		GetWatchdog().Feed();
		Wait(0.1);
		GetWatchdog().Feed();
	} while (((int)angle) != 0);
	DBG("\nExit\n");
}
