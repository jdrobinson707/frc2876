/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc2876.Robot2013.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc2876.Robot2013.Robot;

/**
 *
 * @author Student
 */
public class Find2PtTarget extends Command {
    
    public Find2PtTarget() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.vision);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.vision.cameraInit();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //finds 2 pt. targets:
        Robot.vision.findTargets(false);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
