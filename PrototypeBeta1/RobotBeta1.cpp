//Contributers:  Neil Patel, Maciej, Eddy Louis, Gerry Porthier
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
//self made libraries
#include "Math2.h"
#include "RobotBeta1.h"

using namespace std;

// Watchdog expiration in seconds
#define WATCHDOG_EXPIRATION 5

#define JOYSTICK_LEFT 1
#define JOYSTICK_RIGHT 2

// Which pwm inputs/outputs the drive motors are plugged into.
#define DRIVE_MOTOR_LEFT_PWM 1
#define DRIVE_MOTOR_RIGHT_PWM 2

// First analog module is plugged into slot 1 of cRIO
#define ANALOG_MODULE_SLOT 1

#define DIGITAL_MODULE_SLOT 4

// Gyro sensor has two outputs, angle and temp.
#define GYRO_ANGLE_CHANNEL 1
#define GYRO_TEMP_CHANNEL 2

static int dbg_flag = 0;
#define DBG if (dbg_flag)dprintf 


RobotBeta1::RobotBeta1(void)
{
	DBG("Initializing RobotBeta1...\n");

	leftMotor = new Jaguar(DIGITAL_MODULE_SLOT, DRIVE_MOTOR_LEFT_PWM);
	rightMotor = new Jaguar(DIGITAL_MODULE_SLOT, DRIVE_MOTOR_RIGHT_PWM);
	robotDrive = new RobotDrive(leftMotor, rightMotor);
	stickLeft = new Joystick(JOYSTICK_LEFT);
	stickRight = new Joystick(JOYSTICK_RIGHT);
	gyro = new Gyro(ANALOG_MODULE_SLOT, GYRO_ANGLE_CHANNEL);
	
	initializeColors();
	initializeButtons();
	initializeCamera();
	
	GetWatchdog().SetExpiration(WATCHDOG_EXPIRATION);
	
	DBG("Done\n");
}

RobotBeta1::~RobotBeta1(void)
{
	StopCameraTask();
	pc->Stop();
	delete pc;
	delete gyro;
	delete stickRight;
	delete stickLeft;
	delete robotDrive;
	delete rightMotor;
	delete leftMotor;
}

void RobotBeta1::initializeColors() {
	/* image data for tracking - override default parameters if needed */
	/* recommend making PINK the first color because GREEN is more 
	 * subsceptible to hue variations due to lighting type so may
	 * result in false positives */
	// PINK
	sprintf (tt1.name, "PINK");
	tt1.hue.minValue = 220;   
	tt1.hue.maxValue = 255;  
	tt1.saturation.minValue = 75;   
	tt1.saturation.maxValue = 255;      
	tt1.luminance.minValue = 85;  
	tt1.luminance.maxValue = 255;
	// GREEN
	sprintf (tt2.name, "GREEN");
	tt2.hue.minValue = 55;   
	tt2.hue.maxValue = 125;  
	tt2.saturation.minValue = 58;   
	tt2.saturation.maxValue = 255;    
	tt2.luminance.minValue = 92;  
	tt2.luminance.maxValue = 255;	
}

void RobotBeta1::initializeButtons(void)
{
	int i;
	for (i = JOYSTICK_FIRST_BUTTON; i <= JOYSTICK_NUM_BUTTONS; i++) {
		leftButtons[i] = rightButtons[i] = false;
	}
}

void RobotBeta1::initializeCamera(void)
{
	if (StartCameraTask() == -1) { 
		DBG("Failed to spawn camera task; Error code %s\n", 
				GetVisionErrorText(GetLastVisionError()) ); 
	} else {
		pc = new PCVideoServer();
	}
}

void RobotBeta1::Autonomous(void) {
	DBG("Starting Autonomous...Trial 15\n");
	while(IsAutonomous()) {
#if 0
		GetWatchdog().Feed();
		driveStrait(500);
		GetWatchdog().Feed();
		turn130Left();
		robotDrive.Drive(0, 0);
#endif
		GetWatchdog().Feed();
		Wait(0.4);

	}
	DBG("\nEnd Autonomous Mode\n");	
}

void RobotBeta1::OperatorControl(void) {
	int slowDownProccessing = 0;
	DBG("\nStart Operator Control...\n");
	
	resetGyro();
	
	GetWatchdog().SetEnabled(true);
	while (IsOperatorControl())  {
		GetWatchdog().Feed();
		robotDrive->TankDrive(stickLeft,stickRight);
		actOnButtons();
		if ((slowDownProccessing++ % 25) == 0) {
			DBG("\r\t\tGyro Angle:\t%f", gyro->GetAngle());
		}
		Wait(0.006);
	}
	DBG("\nEnd Operator Control\n");
}

void RobotBeta1::recieveAndReactToCameraData(void) {
	
}

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

void RobotBeta1::UpdateDashboard(void) 
{
	GetWatchdog().Feed();

	dashboardDataFormat->m_AnalogChannels[0][GYRO_ANGLE_CHANNEL] = gyro->GetAngle();
    dashboardDataFormat->m_AnalogChannels[0][GYRO_TEMP_CHANNEL] = 0;
    dashboardDataFormat->m_PWMChannels[0][DRIVE_MOTOR_LEFT_PWM] = leftMotor->GetRaw();
    dashboardDataFormat->m_PWMChannels[0][DRIVE_MOTOR_RIGHT_PWM] = rightMotor->GetRaw();

    dashboardDataFormat->PackAndSend();
}

//
// Read buttons on a joystick to see if any have been pressed.
// stick: left or right joystick
// buttons: array of bools to store button press state
// side: string that indicates left/right used for printing debug msgs
//
void RobotBeta1::readButtons(Joystick *stick, bool *buttons, char *side)
{
	int i;
	for (i = JOYSTICK_FIRST_BUTTON; i <= JOYSTICK_NUM_BUTTONS; i++) {
		buttons[i] = stick->GetRawButton(i);
		if (buttons[i] == true) {
			DBG("%s stick button %d pressed", side, i);
		}
	}
}

void RobotBeta1::actOnButtons(void)
{
	readButtons(stickLeft, leftButtons, "left");
	readButtons(stickRight, leftButtons, "right");
	DBG("rightZ=%f leftZ=%f\n", stickRight->GetZ(), stickLeft->GetZ());
}

START_ROBOT_CLASS(RobotBeta1);
