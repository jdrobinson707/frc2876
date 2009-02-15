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
	DBG("Starting Autonomous...Trial 80\n");
	//conveyor->Set(.5);
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
}

/************************************************************
NOTE:  FindTwoColors takes its own raw images so if
	   the camera is upside down then FindTwoColors will
	   see that pink is above even though it really is below
************************************************************/
void RobotBeta1::recieveAndReactToCameraData(void) {
	bool foundTrailor = false;
	
	if (ourAlliance == DriverStation::kRed) {
		//if we are red, they are blue, and their trailor is pink above green
		foundTrailor = FindTwoColors(tt1, tt2, ABOVE, &pa1, &pa2);
	} else {
		//if we are blue, they are red, and their trailor is pink below green
		foundTrailor = FindTwoColors(tt1, tt2, BELOW, &pa1, &pa2);		
		cout << "Running"; cout << foundTrailor;
	}
	
	if (foundTrailor) { //if the trailor's beam is found
		//STEP#1:	measure distance to trailer's beam
		double dToTrailor = 0;
		dToTrailor = distanceToTrailor((double)(pa1.boundingRect.height + pa2.boundingRect.height));
		cout << "\nDistance To Trailor:  ";  cout << dToTrailor; cout << "\n";
		//STEP#2:	align
		if (dToTrailor > 6.0) {
			moveToTrailor(dToTrailor);
		}
		//STEP#3:	shoot
		if (dToTrailor >= 5.0 && dToTrailor <= 6.0) {
			shooter->Set(.6);
			Wait(2.0);
			shooter->Set(0.0);
		}
	} else {
		cout << "Else------------------";
	}
}

void RobotBeta1::moveToTrailor(double distance) {
	if (distance >= 5.0 && distance <= 35.0) {
		GetWatchdog().Feed();
		cout << pa1.center_mass_x_normalized;
		robotDrive->Drive(-.5, -pa1.center_mass_x_normalized);
		GetWatchdog().Feed();
	} else {
		robotDrive->Drive(0, 0);
	}
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
