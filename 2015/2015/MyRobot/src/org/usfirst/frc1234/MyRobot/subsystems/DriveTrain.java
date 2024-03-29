// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1234.MyRobot.subsystems;

import org.usfirst.frc1234.MyRobot.RobotMap;
import org.usfirst.frc1234.MyRobot.commands.*;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class DriveTrain extends Subsystem {
	boolean togFOV = false;
	double gyroAngle = 0;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    Gyro gyro = RobotMap.driveTrainGyro;
    SpeedController speedControllerBackLeft = RobotMap.driveTrainSpeedControllerBackLeft;
    SpeedController speedControllerBackRight = RobotMap.driveTrainSpeedControllerBackRight;
    SpeedController speedControllerFrontLeft = RobotMap.driveTrainSpeedControllerFrontLeft;
    SpeedController speedControllerFrontRight = RobotMap.driveTrainSpeedControllerFrontRight;
    RobotDrive robotDrive = RobotMap.driveTrainRobotDrive;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        setDefaultCommand(new DrivewithLStick());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
	
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void drive(double x, double y, double rotation){
    	if (togFOV == true) {
    		gyroAngle = gyro.getAngle();
    		robotDrive.mecanumDrive_Cartesian(y, x, rotation, gyroAngle);
    	}
    	else{
    		robotDrive.mecanumDrive_Cartesian(y, x, rotation, 0);
    	}
    }
    public void toggleFOV(){
    	togFOV = !togFOV;
    }
}

