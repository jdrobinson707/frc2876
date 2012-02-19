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
public class CollectBall extends CommandBase {

    boolean wasPressed = false;
    boolean whenReleased = false;

    public CollectBall() {
        // Use requires() here to declare subsystem dependencies
        requires(conveyorlow);
        wasPressed = false;
        whenReleased = false;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        wasPressed = false;
        whenReleased = false;
        conveyorlow.on();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

        boolean b = conveyorlow.hasBallEntered();
        if (b == true) {
            wasPressed = true;
        }
        if (wasPressed == true && b == false) {
            whenReleased = true;
        }
        SmartDashboard.putBoolean("wasPressed: ", wasPressed);
        SmartDashboard.putBoolean("whenReleased: ", whenReleased);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return whenReleased;
    }

    // Called once after isFinished returns true
    protected void end() {
        conveyorlow.idle();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
