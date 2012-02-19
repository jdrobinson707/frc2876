/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author User
 */
public class AdjustTurn extends CommandBase {

    public AdjustTurn() {
        // Use requires() here to declare subsystem dependencies
        requires(cameratarget);
        requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drive.resetGyro();
        drive.startTurn(cameratarget.getDifference());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return drive.isTurnFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
        drive.endTurn();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}