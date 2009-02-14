//THIS FILE CONTAINS ALL FUNCTIONS THAT HAVE TO DO WITH UPDATE DASHBOARD & INITIALIZATION OF ROBOTBETA1

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

using namespace std;

// Watchdog expiration in seconds
#define WATCHDOG_EXPIRATION 5

#define JOYSTICK_LEFT 1
#define JOYSTICK_RIGHT 2
#define JOYSTICK_COPILOT 3

// Which pwm inputs/outputs the drive motors are plugged into.
#define DRIVE_MOTOR_RIGHT_PWM 1
#define DRIVE_MOTOR_LEFT_PWM 2
#define SHOOTER_MOTOR_PWM 4
#define CONVEYOR_MOTOR_PWM 3

// What is plugged into the Digital I/O (also called GPIO) slots
#define ALLIANCE_SWITCH_GPIO 1

// First analog module is plugged into slot 1 of cRIO
#define ANALOG_MODULE_SLOT 1

#define DIGITAL_MODULE_SLOT 4

// Gyro sensor has two outputs, angle and temp.
#define GYRO_ANGLE_CHANNEL 1
#define GYRO_TEMP_CHANNEL 2

int dbg_flag = 1;
#define DBG if (dbg_flag)printf 
#define TRACE_ENTER printf("%s Enter\n", __func__);
#define TRACE_EXIT printf("%s Exit\n", __func__);

RobotBeta1::RobotBeta1(void) {
	SetDebugFlag(DEBUG_SCREEN_ONLY);
	DBG("Initializing RobotBeta1...\n");

	leftMotor = new Jaguar(DIGITAL_MODULE_SLOT, DRIVE_MOTOR_LEFT_PWM);
	rightMotor = new Jaguar(DIGITAL_MODULE_SLOT, DRIVE_MOTOR_RIGHT_PWM);
	robotDrive = new RobotDrive(leftMotor, rightMotor);
	stickLeft = new Joystick(JOYSTICK_LEFT);
	stickRight = new Joystick(JOYSTICK_RIGHT);
	stickCopilot = new Joystick(JOYSTICK_COPILOT);
	gyro = new Gyro(ANALOG_MODULE_SLOT, GYRO_ANGLE_CHANNEL);
	shooter = new Jaguar(DIGITAL_MODULE_SLOT, SHOOTER_MOTOR_PWM);
	conveyor = new Jaguar(DIGITAL_MODULE_SLOT, CONVEYOR_MOTOR_PWM);
	dashboard = new DashboardDataFormat();
	pan = new Servo(DIGITAL_MODULE_SLOT, 10);
	tilt = new Servo(DIGITAL_MODULE_SLOT, 9);
	//encoder = new Encoder(DIGITAL_MODULE_SLOT, 1);
	driverStation = DriverStation::GetInstance();
	allianceSwitch = new DigitalInput(DIGITAL_MODULE_SLOT, ALLIANCE_SWITCH_GPIO);
	// initializeAlliance();
	initializeColors();
	initializeButtons();
	initializeCamera();
	initializeAlliance();
	lastTime = time(NULL);
	accelbutton = 10;
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
	delete dashboard;
	delete shooter;
	delete conveyor;
	delete pan;
	delete tilt;
	// delete encoder;
	delete stickCopilot;
	delete allianceSwitch;
}

void RobotBeta1::initializeAlliance() {
	TRACE_ENTER;
	
	//---->initialize by switch
		if (allianceSwitch->Get() == 1) { //if the alliance switch is "ON"
			ourAlliance = DriverStation::Alliance(DriverStation::kBlue);
		} else {
			ourAlliance = DriverStation::Alliance(DriverStation::kRed);
		}
	//---->initialize by driver station
		/*ourAlliance = driverStation->GetAlliance(); */
	
		
	//---->Output display	
	if (ourAlliance == DriverStation::kRed) {
		DBG("WE ARE RED TEAM\n");
	} else if (ourAlliance == DriverStation::kBlue) {
		DBG("WE ARE BLUE TEAM\n");
	} else {
		DBG("Invalid team color");
	}
	TRACE_EXIT;
}

void RobotBeta1::initializeColors() {
	TRACE_ENTER;
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
	DBG("Exit\n");
}

void RobotBeta1::initializeButtons(void)
{
	TRACE_ENTER;
	int i;
	for (i = JOYSTICK_FIRST_BUTTON; i <= JOYSTICK_NUM_BUTTONS; i++) {
		leftButtons[i] = rightButtons[i] = false;
	}
	TRACE_EXIT;
}

void RobotBeta1::initializeCamera(void)
{
	TRACE_ENTER;
	if (StartCameraTask(10, 0, k160x120, ROT_180) == -1) { 
		DBG("Failed to spawn camera task; Error code %s\n", 
				GetVisionErrorText(GetLastVisionError()) ); 
	} else {
		pc = new PCVideoServer();
	}
	TRACE_EXIT;
}

void RobotBeta1::UpdateDashboard(void) 
{
	GetWatchdog().Feed();
	dashboard->m_PWMChannels[0][0] = leftMotor->GetRaw();
	dashboard->m_PWMChannels[0][1] = rightMotor->GetRaw();
	dashboard->m_PWMChannels[0][2] = shooter->GetRaw();
	dashboard->m_PWMChannels[0][3] = conveyor->GetRaw();
	dashboard->m_PWMChannels[0][8] = pan->GetRaw();
	dashboard->m_PWMChannels[0][9] = tilt->GetRaw();
	//dashboard->m_DIOChannels[1] = encoder->GetRaw();
	//dashboard->m_DIOChannels[2] = encoder->GetRaw();

	DBG("\rPWM 1-%d 2-%d 3-%d 4-%d 8-%d 9-%d %d %d now=%d last=%d",
		dashboard->m_PWMChannels[0][0],
		dashboard->m_PWMChannels[0][1],
		dashboard->m_PWMChannels[0][2],
		dashboard->m_PWMChannels[0][3],
		dashboard->m_PWMChannels[0][8],
		dashboard->m_PWMChannels[0][9],
		dashboard->m_DIOChannels[1],
	    dashboard->m_DIOChannels[2],
	    (int)now, (int)lastTime);
	
	// Call this last to send data to dashboard.
    dashboard->PackAndSend();
}

START_ROBOT_CLASS(RobotBeta1);
