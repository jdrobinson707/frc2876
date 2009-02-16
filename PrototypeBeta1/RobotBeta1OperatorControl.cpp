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

using namespace std;

void RobotBeta1::OperatorControl(void) {
	DBG("\nStart Operator Control...version 4\n");
	
	//resetGyro();
	// conveyor->Set(.5);
	leftEncoder->Start();
	rightEncoder->Start();
	GetWatchdog().SetEnabled(true);
	while (IsOperatorControl())  {
		actOnButtons();
		GetWatchdog().Feed();		
		
		if (accelbutton == 8) {
			UpdateDrive_Eddy();
		} else if (accelbutton == 9) {
			UpdateDrive_Neil();
		} else if (accelbutton == 10) {
			robotDrive->TankDrive(stickLeft->GetY() / 1.5 , stickRight->GetY() / 1.5);
		} else if (accelbutton == 4) {
			robotDrive->TankDrive(stickLeft->GetY() / 1.8 , stickRight->GetY() / 2.0);			
		} else if (accelbutton == 3) {
			robotDrive->TankDrive(stickLeft->GetY() / 1.4 , stickRight->GetY() / 1.4);			
		} else if (accelbutton == 5) {
			robotDrive->TankDrive(stickLeft->GetY() / 1.1 , stickRight->GetY() / 1.1);			
		}
		UpdateDashboard();
		Wait(0.05);
	}
	shooter->Set(0);
	leftMotor->Set(0);
	rightMotor->Set(0);
	conveyor->Set(0);
	leftEncoder->Stop();
	rightEncoder->Stop();
	DBG("\nEnd Operator Control\n");
}

void RobotBeta1::UpdateDrive_Neil() {
	if (leftButtons[1] == true || rightButtons[1] == true) {
		Wait(.1);
		accelmonitor_Neil(stickLeft->GetY(), stickRight->GetY(), false);
	} else {
		robotDrive->TankDrive((stickLeft->GetY()), (stickRight->GetY()));
		accelmonitor_Neil(0.0, 0.0, true);
	}
}


// if the joystick is pushed beyond .6 then accelmonior lowers speed to .4
float RobotBeta1::accelmonitor_Eddy (float YVal) {
	if (rightButtons[1] == true || leftButtons[1] == true) {
		if (YVal >= .8 && YVal <= 1) {
			return .8;
		}
		if (YVal <= -.8 && YVal >= -1) {
			return -.8;
		}
	}
	if (YVal >= .8 && YVal <= 1) {
		return .5;
	}
	if (YVal <= -.8 && YVal >= -1) {
		return -.5;
	}
	return YVal;	
}

// 
void RobotBeta1::UpdateDrive_Eddy() {
	
	//if (leftButtons[1] == true && rightButtons[1] == true) {
		float rightYVal;
		float leftYVal;
		rightYVal = stickRight->GetY();
		leftYVal = stickLeft->GetY();
		rightYVal = accelmonitor_Eddy(rightYVal); 
		leftYVal = accelmonitor_Eddy(leftYVal);
		robotDrive->TankDrive(leftYVal, rightYVal);	
	//}
}

void RobotBeta1::accelmonitor_Neil (float jStickY1, float jStickY2, bool reset) {
	float iCurrentYVal = 0.0;
		if (jStickY1 >= jStickY2) iCurrentYVal = jStickY2;
		else iCurrentYVal = jStickY1;
	float rampupVal = 0.0;
		if (iCurrentYVal >= 0.0) rampupVal = .05;
		else rampupVal = -.05;
	static int firstTime = 0;
	
	cout << "YVal:  "; cout << iCurrentYVal;
	
	if (!reset) { 
		while (!RobotIsAtHighestSpeed(iCurrentYVal, jStickY1, jStickY2) && IsOperatorControl() && firstTime < 1 && 
				(leftButtons[1] == true || rightButtons[1] == true)) {
			//if all of the above is true then you may execute this code which ramps up speed slowly
			GetWatchdog().Feed();
			robotDrive->TankDrive((jStickY1 - iCurrentYVal), (jStickY2 - iCurrentYVal));
			Wait(.8);
			iCurrentYVal -= rampupVal;
			cout << "first";
		}
		if (firstTime >= 1) {
			robotDrive->TankDrive((jStickY1), (jStickY2));
			cout << "second";
		}
	}
	
	firstTime++;
	if ((!IsOperatorControl()) || !(leftButtons[1] == true || rightButtons[1] == true) || reset) {
		firstTime = 0;
	}
}

bool RobotBeta1::RobotIsAtHighestSpeed(float currentYVal, float jy1, float jy2) {
	if (currentYVal <= 0.01 || currentYVal >= -0.01) { // of robot is approximately 
		return true; //at the highest speed return true
	} else return false;
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
	if (copilotButtons[2] == true) {
			shooter->Set(0);
	}
	if (copilotButtons[4] == true) {
		shooter->Set(.5);
	}
	if (copilotButtons[3] == true) {
		shooter->Set(.6);
	}
	if (copilotButtons[5] == true) {
		shooter->Set(.8);
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
//	updatePanTilt();
	if (leftButtons[8] == true) {
		accelbutton = 8;
	}
	if (leftButtons[9] == true) {
		accelbutton = 9;
	}
	if (leftButtons[10] == true) {
		accelbutton = 10;
	}
	if (leftButtons[4] == true) {
		accelbutton = 4;
	}
	if (leftButtons[3] == true) {
		accelbutton = 3;
	}
	if (leftButtons[5] == true) {
		accelbutton = 5;
	}	
}
