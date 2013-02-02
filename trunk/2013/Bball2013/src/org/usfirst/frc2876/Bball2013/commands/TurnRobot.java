/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc2876.Bball2013.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc2876.Bball2013.Robot;

/**
 *
 * @author Student
 */
public class TurnRobot extends Command {
    
    double offset = 0.0;
    
    public TurnRobot() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrain);
        requires(Robot.vision2013);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        offset = Robot.vision2013.getTurnOff();
        //System.out.println("Offset - " + offset);
        //Robot.driveTrain.resetGyro();

        Robot.driveTrain.init();
        Robot.driveTrain.startTurn(offset);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.driveTrain.getGyro();
//        if (offset < 0)  // turn right
//        {
//            Robot.driveTrain.drive(.5, -.5);
//        } else {
//            Robot.driveTrain.drive(-.5, .5);
//        }

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //return (Math.abs(Robot.driveTrain.getGyro()) > Math.abs(offset));
        return Robot.driveTrain.isTurnFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
        //Robot.driveTrain.drive(0, 0);
        Robot.driveTrain.endTurn();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end(); //
    }
}
