/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author User
 */
public class TurnRobot extends CommandBase {

    double angleAmount = 0.0;

    public TurnRobot(double degrees) {
        // Use requires() here to declare subsystem dependencies
        requires(drive);
        angleAmount = degrees;
        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drive.startTurn(angleAmount);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return drive.isTurnFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
        drive.endTurn();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}