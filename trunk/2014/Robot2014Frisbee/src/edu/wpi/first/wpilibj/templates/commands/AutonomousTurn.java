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
        // TODO add code to start PID turner
    }

    // Called repeatedly when this Command is scheduled to run
    //Turns right until it makes a 90 degree angle
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        // TODO check PID turner
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
           // TODO Turn off PID turner

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
            end();
    }
}
