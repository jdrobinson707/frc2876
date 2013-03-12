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
public class DriveForwardStraight extends Command {
    double dist;
    public DriveForwardStraight(double d) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.driveTrain);
        dist = d;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//        Robot.driveTrain.startEncoder(Robot.driveTrain.rightEncoder);
//        Robot.driveTrain.startEncoder(Robot.driveTrain.leftEncoder);
        double z = Robot.oi.getRightStick().getZ();
        z = (((z + 1) / 2) * 1000) + 25;
        Robot.driveTrain.setDriveDistanceStraight(z);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.driveTrain.driveDistanceStraight();
        Robot.driveTrain.updateDashboard();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        // call isTurnDone just to get dashboard to update.
        // dont care about return value.
        boolean b = Robot.driveTrain.isTurnDone();
        return Robot.driveTrain.isDistanceDone();
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.endDistance();
        Robot.driveTrain.endTurn();
//        Robot.driveTrain.stopEncoder(Robot.driveTrain.rightEncoder);
//        Robot.driveTrain.stopEncoder(Robot.driveTrain.leftEncoder);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
