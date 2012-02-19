/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author User
 */
public class ShooterUpdate extends CommandBase {

    double speed = 0.0;
    double update;
    boolean finished;

    public ShooterUpdate(double s) {

        requires(shooter);
        update = s;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        speed = shooter.get() + update;
        shooter.set(speed);
        finished = false;

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //shooter.set(speed);
        shooter.getCount();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        finished = true;
    }
}
