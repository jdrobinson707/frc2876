// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc2876.RecycleRush2876.subsystems;

import org.usfirst.frc2876.RecycleRush2876.Robot;
import org.usfirst.frc2876.RecycleRush2876.RobotMap;
import org.usfirst.frc2876.RecycleRush2876.commands.*;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Elevator extends PIDSubsystem {
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    AnalogPotentiometer potentiometer = RobotMap.elevatorpotentiometer;
    SpeedController elevatorMotor = RobotMap.elevatorElevatorMotor;
    DigitalInput topLimit = RobotMap.elevatorTopLimit;
    DigitalInput bottomLimit = RobotMap.elevatorBottomLimit;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	private final double elevatorMaxSpeed = 0.6;

	// Initialize your subsystem here
	public Elevator() {
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        super("Elevator", 0.008, 0.0, 0.008);
        setAbsoluteTolerance(0.2);
        getPIDController().setContinuous(true);
        LiveWindow.addActuator("Elevator", "PIDSubsystem Controller", getPIDController());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        SmartDashboard.putData("ElevatorPID", getPIDController());
        
        getPIDController().setOutputRange(-.4, .4);
        
		// Use these to get going:
		// setSetpoint() -  Sets where the PID controller should move the system
		//                  to
		// enable() - Enables the PID controller.

		//POT VALUES (rough estimates as of 2/13/15):
		//bottom/1 bin: 260
		//2 tote: 788
		//3 tote: 1300
		//recycle bin: 955
		//top/max: 1937
	}

	public void initDefaultCommand() {
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        setDefaultCommand(new ElevatorPosition());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
	}

	protected double returnPIDInput() {
		// Return your input value for the PID loop
		// e.g. a sensor, like a potentiometer:
		// yourPot.getAverageVoltage() / kYourMaxVoltage;

		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
        return potentiometer.get();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
	}

	protected void usePIDOutput(double output) {
		// Use output to drive your system, like a motor
		// e.g. yourMotor.set(output);

		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
        elevatorMotor.pidWrite(output);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
        if (topMax() && output > 0) {
        	motorOff();
        }
        if (bottomMax() && output < 0) {
        	motorOff();
        }
	}

	public void motorUp() {
		elevatorMotor.set(.5);
	}

	public void motorOff() {
		elevatorMotor.set(0);
	}

	public void motorDown() {
		elevatorMotor.set(-.3);
	}
	
	public void motorTrigger(double up, double down) {
		if (topMax() && up > 0){
			motorOff();
			return;
		}
		if (bottomMax() && down >0) {
			motorOff();
			return;
		}
		if (up > 0 && down > 0) {
			motorOff();
			return;
		}
		if (up > 0) {
			elevatorMotor.set(Math.min(Math.pow(up , 2), elevatorMaxSpeed));
		}
		if (down > 0) {
			elevatorMotor.set(Math.max(0 - Math.pow(down, 2), 0 - elevatorMaxSpeed));
		}
	}

	public void motorRightTrigger() {
		elevatorMotor.set(Math.min(Math.pow(Robot.oi.getRightTrigger(), 2), elevatorMaxSpeed));
		SmartDashboard.putNumber("Right Trigger", Robot.oi.getRightTrigger());
	}

	public void motorLeftTrigger() {
		elevatorMotor.set(Math.max(0 - Math.pow(Robot.oi.getLeftTrigger(), 2), 0 - elevatorMaxSpeed));
		SmartDashboard.putNumber("Left Trigger", Robot.oi.getLeftTrigger());
	}

	public void enablePID(){
		enable();
	}

	public void disablePID(){
		disable();
	}

	public boolean topMax() {
		return !topLimit.get();  
	}

	public boolean bottomMax() {
		return !bottomLimit.get();
	}

	public void updateDashboard() {
		SmartDashboard.putNumber("PID position", getPosition());
		SmartDashboard.putNumber("PID error", getPIDController().getError());
	}
}
