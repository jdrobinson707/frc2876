/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.Pimpbot2011.commands;

/**
 *
 * @author maciej
 */
public class TurnRobot extends CommandBase {

    double degrees;

    public TurnRobot(double degrees) {
        requires(drivetrain);
        this.degrees = degrees;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drivetrain.turnStart(degrees);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // drivetrain.autoDrive();

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return drivetrain.isTurnDone();
    }

    // Called once after isFinished returns true
    protected void end() {
        drivetrain.turnStop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
