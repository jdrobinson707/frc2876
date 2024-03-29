/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.subsystems.Shooter;

/**
 *
 * @author user
 */
public class ShooterStart extends CommandBase {

    public ShooterStart() {
        requires(shooter);
        requires(cameratarget);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        //shooter.shoot(RobotMap.KEY_TOP_SHOOT_RPS);
        double d = cameratarget.getDistance();
        double rps = Shooter.inchesToRps(d);
        System.out.println("distance " + d + "  rps " + rps);
        shooter.start();
        shooter.shoot(rps);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //return shooter.isReady();
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
