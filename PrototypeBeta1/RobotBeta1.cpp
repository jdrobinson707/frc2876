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
//Libraries provided by other Demos
#include "Target.h"
//self made libraries
#include "Math2.h"
#include "RobotBeta1.h"

/***************************************************************************/
/*                     CHANGE THIS DURING THE COMPETITION                  */
// Ummmm... you cannot change code during competition.  A #define is a constant
// You need to find another way to determine team color/alliance. -Maciej
/*     #1*																   */
			#define OUR_TEAM 	WE_ARE_RED_TEAM                          /**/
/*     #2*/																 /**/
/***************************************************************************/

#define WE_ARE_RED_TEAM		BELOW  //if the color of the opposite team is pink above green then they are the red team
#define WE_ARE_BLUE_TEAM	ABOVE  //^--and therefore our color is pink BELOW green and therefore we are the blue team

using namespace std;

// Watchdog expiration in seconds
#define WATCHDOG_EXPIRATION 5

#define JOYSTICK_LEFT 1
#define JOYSTICK_RIGHT 2

// Which pwm inputs/outputs the drive motors are plugged into.
#define DRIVE_MOTOR_LEFT_PWM 1
#define DRIVE_MOTOR_RIGHT_PWM 2
#define SHOOTER_MOTOR_PWM 3
#define CONVEYOR_MOTOR_PWM 4


// First analog module is plugged into slot 1 of cRIO
#define ANALOG_MODULE_SLOT 1

#define DIGITAL_MODULE_SLOT 4

// Gyro sensor has two outputs, angle and temp.
#define GYRO_ANGLE_CHANNEL 1
#define GYRO_TEMP_CHANNEL 2

static int dbg_flag = 1;
#define DBG if (dbg_flag)printf 


RobotBeta1::RobotBeta1(void)
{
	SetDebugFlag(DEBUG_SCREEN_ONLY);
	DBG("Initializing RobotBeta1...\n");

	leftMotor = new Jaguar(DIGITAL_MODULE_SLOT, DRIVE_MOTOR_LEFT_PWM);
	rightMotor = new Jaguar(DIGITAL_MODULE_SLOT, DRIVE_MOTOR_RIGHT_PWM);
	robotDrive = new RobotDrive(leftMotor, rightMotor);
	stickLeft = new Joystick(JOYSTICK_LEFT);
	stickRight = new Joystick(JOYSTICK_RIGHT);
	gyro = new Gyro(ANALOG_MODULE_SLOT, GYRO_ANGLE_CHANNEL);
	shooter = new Jaguar(DIGITAL_MODULE_SLOT, SHOOTER_MOTOR_PWM);
	conveyor = new Jaguar(DIGITAL_MODULE_SLOT, CONVEYOR_MOTOR_PWM);
	dashboard = new DashboardDataFormat();
	pan = new Servo(DIGITAL_MODULE_SLOT, 10);
	tilt = new Servo(DIGITAL_MODULE_SLOT, 9);
	
	
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
	delete dashboard;
	delete shooter;
	delete conveyor;
	delete pan;
	delete tilt;
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
	if (StartCameraTask(10, 0, k160x120, ROT_180) == -1) { 
		DBG("Failed to spawn camera task; Error code %s\n", 
				GetVisionErrorText(GetLastVisionError()) ); 
	} else {
		pc = new PCVideoServer();
	} 	
}

static int fake_range = 10;
static int getRange()
{
	return fake_range;
}


void RobotBeta1::Autonomous(void) {
	DBG("Starting Autonomous...Trial 15\n");
	int slowDownProccessing = 0;
	conveyor->Set(.1);
	
	while(IsAutonomous()) {
#if 0
		GetWatchdog().Feed();
		driveStrait(500);
		GetWatchdog().Feed();
		turn130Left();
		robotDrive.Drive(0, 0);
#endif
		 
		   
		if (getRange() == 5) {
			shooter->Set(1);
		} else if  (getRange() == 4) {
			shooter->Set(.75);
		}	else if (getRange() == 3) {
			shooter->Set(.75);
		}	else if (getRange() ==2) {
			shooter->Set(.5);
		} else {
			shooter->Set(0);
		}
		UpdateDashboard();
		if (slowDownProccessing % 1000) {
			recieveAndReactToCameraData();
		}
		slowDownProccessing++;
		GetWatchdog().Feed();
		Wait(1.0);
		if (fake_range == 0) {
			fake_range = 10;
		} else {
			fake_range = fake_range - 1;
		}
	} 
	conveyor->Set(0);
	DBG("\nEnd Autonomous Mode\n");	
}

void RobotBeta1::OperatorControl(void) {
	DBG("\nStart Operator Control...\n");
	
	resetGyro();
	conveyor->Set(.1);
	
	GetWatchdog().SetEnabled(true);
	while (IsOperatorControl())  {
		GetWatchdog().Feed();
		robotDrive->TankDrive(stickLeft, stickRight);
		actOnButtons();
		UpdateDashboard();
		Wait(0.05);
	}
	conveyor->Set(0);
	DBG("\nEnd Operator Control\n");
}
/************************************************************
NOTE:  FindTwoColors takes its own raw images so if
	   the camera is upside down then FindTwoColors will
	   see that pink is above even though it really is below
************************************************************/
void RobotBeta1::recieveAndReactToCameraData(void) {
	if (FindTwoColors(tt1, tt2, ABOVE, &pa1, &pa2)) { //if the trailor's beam is found
		//STEP#1:	measure distance to trailer's beam
		double dToTrailor = 0;
		dToTrailor = distanceToTrailor((double)(pa1.boundingRect.height + pa2.boundingRect.height));
		cout << "\nDistance To Trailor:  ";  cout << dToTrailor; cout << "\n";
		//STEP#2:	align
		//STEP#3:	shoot
	}
}

double RobotBeta1::distanceToTrailor(double pxHeightOfColor) {
	double d = 0;
	d = (344.0/(pxHeightOfColor));
	return d;
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

	// TODO EddyL add dashboard data here.  How?
	//
	// Look at dashboard in labview.  
	// Look at what sensors/motors we have hooked up to cRIO on robot.
	// Look at the DashboardDataFormat class and see what member variables are.
	// Make sure the member variable type in DashboardDataFormat  matches the
	// sensor reading function return type.
	
	// Here's an example.  Ok, we don't have anything hooked up to solenoid.
	// But if we did we would have to get a reading from sensor hooked up to solenoid 
	// on cRIO.  
	// Solenoid *s = new Solenoid(1);
	// dashboard->m_SolenoidChannels = s->Get(); 
	//
	// End of TODO section
	
	dashboard->m_PWMChannels[0][0] = leftMotor->GetRaw();
	dashboard->m_PWMChannels[0][1] = rightMotor->GetRaw();
	dashboard->m_PWMChannels[0][2] = shooter->GetRaw();
	dashboard->m_PWMChannels[0][3] = conveyor->GetRaw();
	dashboard->m_PWMChannels[0][8] = pan->GetRaw();
	dashboard->m_PWMChannels[0][9] = tilt->GetRaw();

	DBG("\r%d %d %d %d %d %d range=%d     ",
		dashboard->m_PWMChannels[0][0],
		dashboard->m_PWMChannels[0][1],
		dashboard->m_PWMChannels[0][2],
		dashboard->m_PWMChannels[0][3],
		dashboard->m_PWMChannels[0][8],
		dashboard->m_PWMChannels[0][9],
		fake_range);
	
	// Call this last to send data to dashboard.
    dashboard->PackAndSend();
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
			// DBG("%s stick button %d pressed\n", side, i);
			
		}
	}
}


void RobotBeta1::actOnButtons(void)
{
	
	readButtons(stickLeft, leftButtons, "left");
	readButtons(stickRight, rightButtons, "right");
	if (leftButtons[1] == true && rightButtons[1] == true) {
		shooter->Set(.75);
	} else if (leftButtons[1] == true && rightButtons[1] == false) {
		shooter->Set(1);
	} else if (leftButtons[1] == false && rightButtons[1] == true) {
		shooter->Set(.5);
	} else {
		shooter->Set(0);
	}
	
	// Eddy.  Add servo control here. I(maciej) added code to print the 
	// value of the z button to show you how to get it.
    
	static float lastRightZ = 0;
	static float lastLeftZ = 0;
	float leftZ, rightZ;
	leftZ = stickLeft->GetZ();
	rightZ = stickRight->GetZ();
	if (lastRightZ != rightZ || lastLeftZ != leftZ) {
		DBG("rightZ=%f leftZ=%f\n", leftZ, rightZ);
		lastLeftZ = leftZ;
		lastRightZ = rightZ;
		// Eddy: set servo pwm here.
			 
		pan->Set((leftZ + 1.0) / 2.0);
		tilt->Set((rightZ + 1.0) / 2.0);
			 	 
	}
}
	


START_ROBOT_CLASS(RobotBeta1);
