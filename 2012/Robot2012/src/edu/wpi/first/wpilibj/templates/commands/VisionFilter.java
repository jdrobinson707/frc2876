/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author User
 */
public class VisionFilter extends CommandBase {

    public VisionFilter() {
        // Use requires() here to declare subsystem dependencies
        requires(cameratarget);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        cameratarget.findTargets(oi.isDebugOn());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !cameratarget.isFilterRunning();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
