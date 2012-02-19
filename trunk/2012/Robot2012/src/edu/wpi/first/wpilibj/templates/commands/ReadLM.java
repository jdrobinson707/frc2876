/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 * @author User
 */
public class ReadLM extends CommandBase {

    public ReadLM() {
        // Use requires() here to declare subsystem dependencies
        requires(shooter);
        requires(conveyorlow);
        requires(conveyorhigh);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        conveyorlow.hasBallEntered();
        conveyorhigh.hasBallEntered();
        shooter.hasBallEntered();

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}