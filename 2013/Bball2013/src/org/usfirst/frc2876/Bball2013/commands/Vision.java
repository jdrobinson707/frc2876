/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc2876.Bball2013.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc2876.Bball2013.Robot;
import org.usfirst.frc2876.Bball2013.subsystems.Vision2013;
/**
 *
 * @author Student
 */
public class Vision extends Command {
    
    public Vision() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.vision2013);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.vision2013.cameraInit();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.vision2013.findTargets();
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
        end(); //
    }
}
