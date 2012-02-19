/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.Pimpbot2011.commands;

/**
 *
 * @author maciej
 */
public class DriveToDistance extends CommandBase {

    private double setpoint;

    public DriveToDistance(double setpoint) {
        requires(drivetrain);
        this.setpoint = setpoint;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drivetrain.distStart(setpoint);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        drivetrain.autoDrive();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return drivetrain.isDistDone();
    }

    // Called once after isFinished returns true
    protected void end() {
        drivetrain.distStop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
