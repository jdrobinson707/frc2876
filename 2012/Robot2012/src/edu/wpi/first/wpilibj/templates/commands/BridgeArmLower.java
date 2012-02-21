/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author User
 */
public class BridgeArmLower extends CommandBase {

    public BridgeArmLower() {
        // Use requires() here to declare subsystem dependencies
        requires(bridgearm);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        bridgearm.lower();
        bridgearm.enablePID();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return bridgearm.hasFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
        bridgearm.idle();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    end();
    }
}