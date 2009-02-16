//THIS FILE CONTAINS ALL AUTONOMOUS AND CAMERA FUNCTIONS

//Neil Patel
//Date File Was Made:  2/12/09

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

#define DBG if (dbg_flag)printf 

void RobotBeta1::Autonomous(void) {
	DBG("Starting Autonomous...Trial 91\n");
	//conveyor->Set(.5);
	pan->Set(.36);
	tilt->Set(.51);
	
	while(IsAutonomous()) {
#if 0
		GetWatchdog().Feed();
		driveStrait(500);
		GetWatchdog().Feed();
		turn130Left();
		robotDrive.Drive(0, 0);
#endif
		recieveAndReactToCameraData();
		UpdateDashboard();
		GetWatchdog().Feed();
	} 
	conveyor->Set(0);
	DBG("\nEnd Autonomous Mode\n");	
	pan->Set(.36);
	tilt->Set(.51);
}

/************************************************************
NOTE:  FindTwoColors takes its own raw images so if			<-- umm... may not be true
	   the camera is upside down then FindTwoColors will
	   see that pink is above even though it really is below
************************************************************/
void RobotBeta1::recieveAndReactToCameraData(void) {
	int foundTrailor = 0;
	
	if (ourAlliance == DriverStation::kRed) {
		//if we are red, they are blue, and their trailor is tt2(green) is "BELOW" tt1(pink)
		foundTrailor = FindTwoColors(tt1, tt2, BELOW, &pa1, &pa2);
	} else {
		//if we are blue, they are red, and their trailor is tt2(green) is "ABOVE" tt1(pink)
		foundTrailor = FindTwoColors(tt1, tt2, ABOVE, &pa1, &pa2);		
		cout << " Running:  "; cout << foundTrailor; cout << "END";
	}
	
	if (foundTrailor) { //if the trailor's beam is found
		//STEP#1:	measure distance to trailer's beam
		double dToTrailor = 0;
		dToTrailor = distanceToTrailor((double)(pa1.boundingRect.height + pa2.boundingRect.height));
		cout << "\nDistance To Trailor:  ";  cout << dToTrailor; cout << "\n";
		
		//STEP#2:	CHARGE!!!!
		if (dToTrailor > 23.0) {
			robotDrive->Drive(-1.0, (-1 * pa1.center_mass_x_normalized * .5));
		}
		
		//STEP#2A: Align
		else if (dToTrailor <= 23.0 && dToTrailor > 14.0) {
			robotDrive->Drive(-.4, -1 * pa1.center_mass_x_normalized);
			tilt->Set(.56);
		}
		
		//STEP#3:	FIRE!!!!
		else {
			robotDrive->Drive(0,0);
			shooter->Set(.6);
			Wait(8.0);
			shooter->Set(0.0);
		}
	} else cout << "Trailor-Not-Found";
}

void RobotBeta1::moveToTrailor(double distance) {
	/*int firstTime = 0;
	if (distance > 6.0 && distance <= 8.0) {
		robotDrive->Drive(-.25, -pa1.center_mass_x_normalized);
	} else if (distance > 8.0 && distance <= 10.0) {
		robotDrive->Drive(-.5, -pa1.center_mass_x_normalized);
	} else if (distance > 10.0 && distance <= 35.0) {
		float iCurrentYVal = 10.0;
		while (iCurrentYVal > 0 && IsAutonomous() && distance > 10.0 && firstTime < 1) {
			GetWatchdog().Feed();
			robotDrive->Drive((-1.0 * (1.0 / iCurrentYVal)), -pa1.center_mass_x_normalized);
			Wait(.8);
			iCurrentYVal--;
		}
		if (firstTime >= 1) {
			robotDrive->Drive(-.5, -pa1.center_mass_x_normalized);			
		}
		firstTime++;
		GetWatchdog().Feed();
	} else if (distance <= 6.0){
		robotDrive->Drive(0, 0);
	} else if (distance >= 35.0) {
		robotDrive->Drive(0,0);
	}*/
}

/* Pre: 
 * 		#1:takes in the height of the image in pixels
 * 		#2:the screen size must be:
 * 			medium(k320x240) (<-- look in intializeCamera()) if the constant to the equation is 8520.0
 * 			small(k160x120) (<-- look in initializeCamera()) if the constant to the equation is 4128.0
 * 			large(k640x480) (<-- look in initializeCamera()) if the constant to the equation is 16980.0
 * Post:  returns the distance of the trailer from the robot in feet  */
double RobotBeta1::distanceToTrailor(double pxHeightOfColor) {
	double d = 0;
	double const constant = 8520.0;
	d = (constant/(pxHeightOfColor));
	d = d / 12.0;   //convert inches to feet
	return d; // in feet
}
