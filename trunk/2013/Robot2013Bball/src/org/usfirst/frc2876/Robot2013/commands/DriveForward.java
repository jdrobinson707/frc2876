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
public class DriveForward extends Command {
    double dist;
    public DriveForward(double d) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.driveTrain);
        dist = d;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveTrain.leftEncoder.reset();
        Robot.driveTrain.rightEncoder.reset();
        Robot.driveTrain.startEncoder(Robot.driveTrain.rightEncoder);
        Robot.driveTrain.startEncoder(Robot.driveTrain.leftEncoder);
         double z = Robot.oi.getRightStick().getZ();
        z = (((z + 1) / 2) * 1000)+25;
        Robot.driveTrain.setDriveDistance(z);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.driveTrain.isDistanceDone();
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.endDistance();
        Robot.driveTrain.stopEncoder(Robot.driveTrain.rightEncoder);
        Robot.driveTrain.stopEncoder(Robot.driveTrain.leftEncoder);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
