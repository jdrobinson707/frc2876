/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author maciej
 */
public class ShooterRPS extends CommandBase {
    double rps;
    public ShooterRPS(double rps) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(shooter);
        this.rps = rps;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        shooter.start();
        shooter.shoot(rps);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        //shooter.shoot(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}