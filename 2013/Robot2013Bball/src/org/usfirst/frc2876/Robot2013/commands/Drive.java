// Generated with RobotBuilder version 0.0.1
package org.usfirst.frc2876.Robot2013.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc2876.Robot2013.Robot;

/**
 *
 */
public class Drive extends Command {

    public Drive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);

        // The following variables are automatically assigned by
        // robotbuilder and will be updated the next time you export to
        // Java from robot builder. Do not put any code or make any change
        // in the following block or it will be lost on an update. To
        // prevent this subsystem from being automatically updated, delete
        // the following line.
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }
    // Called just before this Command runs the first time

    protected void initialize() {
        Robot.driveTrain.resetSensors();
        System.out.println("leftz " + Robot.oi.getLeftStick().getZ());
    }
    // Called repeatedly when this Command is scheduled to run

    protected void execute() {
        if (Robot.oi.getLeftStick().getZ() > 0) {
            Robot.driveTrain.drive(Robot.oi.getLeftStick(), Robot.oi.getRightStick());
        } else {
            Robot.driveTrain.driveSmooth(Robot.oi.getLeftStick(), Robot.oi.getRightStick());
        }
        Robot.driveTrain.updateDashboard();
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
        end();
    }
}
