/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author User
 */
public class VisionAim extends CommandBase {

    double angle;
    boolean onTarget = false;

    public VisionAim() {
        requires(drive);
        requires(cameratarget);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        cameratarget.resetLastAngle();
        cameratarget.findTargets(oi.isDebugOn());
        angle = cameratarget.getTurnAmount();
        drive.startTurn(angle);
        onTarget = false;
    }

    // Called repeatedly when this Command is scheduled to run
    //TODO
    //adjust timers (drive, here), change pid values
    protected void execute() {
        if (drive.isTurnFinished()) {
            drive.endTurn();
            Timer.delay(.5);
            cameratarget.findTargets(oi.isDebugOn());
            angle = cameratarget.getTurnAmount();
            System.out.println("Angle: " + angle);
            if (Math.abs(angle) > 2.0) {
                //drive.setTurnSetPoint(angle)
                drive.startTurn(angle);
            } else {
                onTarget = true;
            }
        }

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return onTarget;
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
