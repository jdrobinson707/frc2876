/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author User
 */
public class BridgeArmMove extends CommandBase {
    // TODO
    // Make this command accept a value, like turn robot command.
    // The values should be known constants for the arm being in
    // high or low position.

    public BridgeArmMove() {
        // Use requires() here to declare subsystem dependencies
        requires(bridgearm);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        bridgearm.move(oi.getArmStick());
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
