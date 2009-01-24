//Neil Patel
//Start Date:  1/21/09
//Last Updated Date:  1/21/09
//BHS ROBOTIX TEAM

#include "WPILib.h"

/*
void driveStrait() {
	int slowDownProccessing = 0;
	
	gyro.Reset();
	float angle = gyro.GetAngle(); // get heading
	while (((int)angle) != 0) {
		// gyro.Reset();
		angle = gyro.GetAngle();
		printf("calibrate angle %f\n", angle);
		Wait(0.1);
	}
	GetWatchdog().SetEnabled(true);
	while (IsAutonomous())  { 
		GetWatchdog().Feed();
		float angle = gyro.GetAngle(); // get heading
		if ((slowDownProccessing % 250) == 0) {
			printf("\n\t\tGyro Angle:  %f\t\t\tDrive Adj:  %f", gyro.GetAngle(), (angle * 0.03));
		}
		++slowDownProccessing;
		itsDrive.Drive(-.25, (angle * 0.03));// turn to correct heading 
		Wait(0.004); 
	}
	itsDrive.Drive(0.0, 0.0);
}
*/

/*
void 1/2 square {
	printf("\nStarting Autonomous...Trial 8\n");
	float cGyroAngle = 0.0;
	const float maxAngle = 47.3;
	for(int i = 0; ((i < 10) && (IsAutonomous())); i++) {
		
		GetWatchdog().Feed();
		driveStrait(500);
		GetWatchdog().Feed();
		gyro.Reset();
		while((cGyroAngle <= maxAngle) && (IsAutonomous())) {
			cout << "Here\t\t"; cout << "Exit:  "; cout << (cGyroAngle <= maxAngle); cout << "\r";
			itsDrive.Drive(-.25, -1);
			Wait(0.006);
			GetWatchdog().Feed();
			cGyroAngle = gyro.GetAngle();
		}
		itsDrive.Drive(0, 0);
		GetWatchdog().Feed();
	}
	printf("\nEnd Autonomous Mode\n");	
}
*/