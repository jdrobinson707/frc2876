/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc2876.Robot2013.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc2876.Robot2013.Robot;
/**
 *
 * @author Administrator
 */
public class TurnRobot extends Command {
    double degrees;
    public TurnRobot(double d) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis)
         requires(Robot.driveTrain);
         degrees = d;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveTrain.setTurn(degrees);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.driveTrain.updateDashboard();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.driveTrain.isTurnDone();
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.endTurn();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
