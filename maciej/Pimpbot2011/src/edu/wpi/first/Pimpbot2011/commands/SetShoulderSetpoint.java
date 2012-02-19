/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.Pimpbot2011.commands;


/**
 *
 * @author maciej
 */
public class SetShoulderSetpoint extends CommandBase {
    private double setpoint;

    public SetShoulderSetpoint(double setpoint) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shoulder);
        this.setpoint = setpoint;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        shoulder.setSetpoint(setpoint);
        shoulder.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(shoulder.getPosition() - setpoint) < .1;
    }

    // Called once after isFinished returns true
    protected void end() {
        shoulder.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}