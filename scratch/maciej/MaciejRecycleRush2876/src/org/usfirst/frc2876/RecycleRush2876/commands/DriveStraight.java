package org.usfirst.frc2876.RecycleRush2876.commands;

import org.usfirst.frc2876.RecycleRush2876.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraight extends Command {

    public DriveStraight() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveTrain.mecanumDrive_PID(0, .5, 0, 0);

    	// If we really want to drive in a straight line, we need to use gyro.
    	// And we should use a PID controller that uses gyro as input.
    	// Just setting motors to some speed isn't going to work well.

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
