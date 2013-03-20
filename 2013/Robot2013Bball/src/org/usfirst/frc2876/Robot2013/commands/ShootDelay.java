/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc2876.Robot2013.commands;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc2876.Robot2013.Robot;

/**
 *
 * @author maciej
 */
public class ShootDelay extends Command {

    boolean isdone;
    double startTime;

    public ShootDelay() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        isdone = false;
        startTime = Timer.getFPGATimestamp();
        Robot.shooter.endFeeder();
        Robot.shooter.startShooter();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double now = Timer.getFPGATimestamp();
        double currentTime = now - startTime;
        SmartDashboard.putNumber("Timer", currentTime);

        if (currentTime >= 2 && currentTime < 3) {
            Robot.shooter.startFeeder();
        } else if (currentTime >= 3 && currentTime < 4) {
            Robot.shooter.endFeeder();
        } else if (currentTime >= 4 && currentTime < 5) {
            Robot.shooter.startFeeder();
        } else if (currentTime >= 5 && currentTime < 6) {
            Robot.shooter.endFeeder();
        } else if (currentTime >= 6 && currentTime < 7) {
            Robot.shooter.startFeeder();
        } else if (currentTime >= 7 && currentTime < 8) {
            Robot.shooter.endFeeder();
        } else if (currentTime >= 8 && currentTime < 9) {
            Robot.shooter.startFeeder();
        } else if (currentTime >= 9 && currentTime < 10) {
            Robot.shooter.endFeeder();
        } else if (currentTime >= 10 && currentTime < 11){
            isdone = true;
        }
        SmartDashboard.putBoolean("isFeederOn", Robot.shooter.isFeederOn());
    }

// Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {

        return isdone;
    }

    // Called once after isFinished returns true
    protected void end() {
        // Don't turn the shooter off here. The feeder might be loading
        // a frisbee at the same time so we need to let shooter run.
        // Turn off shooter in ShooterIdle
        //Robot.shooter.endShooter();
        Robot.shooter.endFeeder();
        SmartDashboard.putBoolean("isFeederOn", Robot.shooter.isFeederOn());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
