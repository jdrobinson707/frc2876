/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author user
 */
public class ShooterSet extends CommandBase {

    double speed;

    public ShooterSet(double s) {
        requires(shooter);
        speed = s;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        
        shooter.set(speed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//        double z = oi.getArmZ();
//        z = RobotMap.roundtoTwo(z);
//        z += 1;
//        z = z / 2;
//        shooter.set(z);
//        speed = z;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //return (speed < .1);
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        shooter.set(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
