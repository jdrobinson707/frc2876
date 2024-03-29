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
public class AngleShooterJoystick extends Command {

    public AngleShooterJoystick() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.angleShooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

//        double z = Robot.oi.armStick.getZ();
        double z =0;
        int setpoint = (int) ((300.0 * z) + 500.0);
        Robot.angleShooter.setPotSetpoint(setpoint);
        if (Robot.angleShooter.isShooterPIDOn()) {
            // do nothing. PID controller is moving shooter
        } else {
            //Robot.angleShooter.jaguarAngle(Robot.oi.getArmStick().getY());
            Robot.angleShooter.jaguarAngle(0);
            
        }
        Robot.angleShooter.updateDashboard();
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
