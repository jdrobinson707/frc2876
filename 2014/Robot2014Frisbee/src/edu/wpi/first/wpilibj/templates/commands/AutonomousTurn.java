/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author student
 */
public class AutonomousTurn extends CommandBase {
    
    public AutonomousTurn() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        driveTrain.resetEncoder();
        driveTrain.gyro.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    //Turns right until it makes a 90 degree angle
    protected void execute() {
        driveTrain.driveXboxTank(0.4, -.4);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (driveTrain.gyro.getAngle() >= 90.0) {
            return true;
        }
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
