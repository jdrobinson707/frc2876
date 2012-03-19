/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author user
 */
public class VisionTurn extends CommandBase {
    double angle, lastAngle;
    boolean onTarget = false;

    public VisionTurn() {
        requires(drive);
        requires(cameratarget);
        angle = cameratarget.getTurnAmount();
        lastAngle = angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drive.startTurn(angle);
        cameratarget.resetLastAngle();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (drive.isTurnFinished()) {
            cameratarget.findTargets(oi.isDebugOn());
            angle = cameratarget.getTurnAmount();
            if (Math.abs(angle - lastAngle) < 5) {
                onTarget = true;
            } else {
                cameratarget.addLastAngle(angle);
                drive.startTurn(angle);
                lastAngle = angle;
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
