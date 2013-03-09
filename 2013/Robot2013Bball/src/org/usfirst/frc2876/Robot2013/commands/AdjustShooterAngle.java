// Generated with RobotBuilder version 0.0.1
package org.usfirst.frc2876.Robot2013.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc2876.Robot2013.Robot;

/**
 *
 */
public class AdjustShooterAngle extends Command {

    int setpoint = 0;

    public AdjustShooterAngle(int i) {
        requires(Robot.angleShooter);
	setpoint = i;
    }
    // Called just before this Command runs the first time

    protected void initialize() {
        // Robot.angleShooter.setPotSetpoint(volts);
        // scale z button from -1 to 1 to 200 to 800
        double z = Robot.oi.armStick.getZ();
        setpoint = ((((int)(z * 100)) + 100)/2)*6;
        setpoint += 200;
        
        Robot.angleShooter.setPotSetpoint(setpoint);
        Robot.angleShooter.enable();
    }
    // Called repeatedly when this Command is scheduled to run

    protected void execute() {
        Robot.angleShooter.updateDashboard();
    }
    // Make this return true when this Command no longer needs to run execute()

    protected boolean isFinished() {
        return Robot.angleShooter.lmtest() ||
	    Robot.angleShooter.isFinishedAdjustingShooter();
    }
    // Called once after isFinished returns true

    protected void end() {
        Robot.angleShooter.endShooterPID();
    }
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run

    protected void interrupted() {
        end();
    }
}
