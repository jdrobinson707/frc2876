/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc2876.FRC2876AerialAssault.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc2876.FRC2876AerialAssault.Robot;

/**
 *
 * @author maciej
 */
public class DriveWithGain extends Command {
    
    public DriveWithGain() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveTrain.driveStraightStop();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.driveTrain.tankDriveGain(Robot.oi.getXboxLeftX(), Robot.oi.getXboxRightX());
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
