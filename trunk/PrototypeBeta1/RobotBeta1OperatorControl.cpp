//THIS FILE CONTAINS ALL FUNCTIONS THAT HAVE TO DO WITH USER DRIVING AND OPERATING

//NEil Patel
//Date File Was Made:  2/12/09
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

#define DBG if (dbg_flag)printf 

void RobotBeta1::OperatorControl(void) {
	DBG("\nStart Operator Control...\n");
	
	//resetGyro();
	// conveyor->Set(.5);
	
	GetWatchdog().SetEnabled(true);
	while (IsOperatorControl())  {
		actOnButtons();
		UpdateDashboard();
		GetWatchdog().Feed();
		
		//UpdateDrive();
		
		Wait(0.05);
	}
	conveyor->Set(0);
	DBG("\nEnd Operator Control\n");
}

void RobotBeta1::UpdateDrive() {
	
	if (leftButtons[1] == true || rightButtons[1] == true) {
		stickLeft->GetY();
		stickRight->GetY();
		// accelmonitor();
		float rightYVal = 0.0;
		float leftYVal = 0.0;
		rightYVal = stickRight->GetY();
		leftYVal = stickLeft->GetY();
		accelmonitor(rightYVal);
		accelmonitor(leftYVal);
	}
	if (leftButtons[1] == false && rightButtons[1] == false) {
		robotDrive->TankDrive(stickLeft, stickRight);	
	}
}

void RobotBeta1::accelmonitor (float YVal; ) {
	for(float iCurrentYVal = YVal; iCurrentYVal ; iCurrentYVal =) {
		GetWatchdog().Feed();
		robotDrive->TankDrive()
	}
}

// Read buttons on a joystick to see if any have been pressed.
// stick: left or right joystick
// buttons: array of bools to store button press state
// side: string that indicates left/right used for printing debug msgs
void RobotBeta1::readButtons(Joystick *stick, bool *buttons, char *side)
{
	int i;
	for (i = JOYSTICK_FIRST_BUTTON; i <= JOYSTICK_NUM_BUTTONS; i++) {
		buttons[i] = stick->GetRawButton(i);
		if (buttons[i] == true) {
			// DBG("%s stick button %d pressed\n", side, i);
		}
	}
}

void RobotBeta1::updateConveyor()
{
	float speed;
	if (copilotButtons[7] == true) {
		speed = conveyor->Get();
		speed = speed - .1;
		conveyor->Set(speed);
	}
	if (copilotButtons[6] == true) {
		speed = conveyor->Get();
		speed = speed + .1;
		conveyor->Set(speed);

	}
	if (copilotButtons[1] == true){
		conveyor->Set(0);
	}
}

void RobotBeta1::updateShooter()
{	
	// Check status of copilot joystick buttons
	if (copilotButtons[1] == true) {
		if (shooter->Get() == 0) {
			shooter->Set(.5);
		} else {
			shooter->Set(0);
		}
	}
	if (copilotButtons[4] == true) {
		shooter->Set(.4);
	}
	if (copilotButtons[3] == true) {
		shooter->Set(.6);
	}
	if (copilotButtons[5] == true) {
		shooter->Set(1.0);
	}	
#if 0	
	if (copilotButtons[1] == true && rightButtons[1] == true) {
		shooter->Set(.75);
	} else if (copilotButtons[1] == true && rightButtons[1] == false) {
		shooter->Set(.25);
	} else if (copilotButtons[1] == false && rightButtons[1] == true) {
		shooter->Set(.5);
	} else {
		shooter->Set(0);
	}
#endif
	
}

void RobotBeta1::updatePanTilt()
{
	static float lastRightZ = 0;
	static float lastLeftZ = 0;
	float leftZ, rightZ;
	leftZ = stickLeft->GetZ();
	rightZ = stickRight->GetZ();
	if (lastRightZ != rightZ || lastLeftZ != leftZ) {
		DBG("rightZ=%f leftZ=%f\n", leftZ, rightZ);
		lastLeftZ = leftZ;
		lastRightZ = rightZ;
		pan->Set((leftZ + 1.0) / 2.0);
		tilt->Set((rightZ + 1.0) / 2.0);
	}
}

void RobotBeta1::actOnButtons(void)
{
	readButtons(stickLeft, leftButtons, "left");
	readButtons(stickCopilot, copilotButtons, "copilot");
	readButtons(stickRight, rightButtons, "right");
	now = time(NULL);
	if (now - lastTime >= 2) {
		lastTime = now;
	}
	updateConveyor();	
	updateShooter();
	updatePanTilt();
}
