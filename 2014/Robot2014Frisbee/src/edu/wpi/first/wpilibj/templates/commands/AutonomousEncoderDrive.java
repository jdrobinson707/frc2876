/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author student
 */
public class AutonomousEncoderDrive extends CommandBase {

    public AutonomousEncoderDrive() {
        requires(driveTrain);
      
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        driveTrain.startEncoderAutonomous();
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        driveTrain.printEncPIDStatus();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return driveTrain.encoderAutonomousOnTarget(240.0);
    }

    // Called once after isFinished returns true
    protected void end() {
        driveTrain.endEncoderAutonomous();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
