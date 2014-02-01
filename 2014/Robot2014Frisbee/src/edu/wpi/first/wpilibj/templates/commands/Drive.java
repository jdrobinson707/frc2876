/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author mentor
 */
public class Drive extends CommandBase {
    
    double lDistance = 0.0;
    double rDistance = 0.0;
    
    public Drive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
         
     
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //driveTrain.drive(oi.getLeftStick(), oi.getRightStick());

        int driveState = oi.getDriveState();
        if (driveState == 0) 
            driveTrain.driveXboxTank(-oi.getXboxLeftY() *.8, -oi.getXboxRightY() *.8);
        else
            driveTrain.driveXboxArcade(-oi.getXboxLeftY() *.8, -oi.getXboxRightX() *.8);

        //lDistance += driveTrain.leftEncoder.getDistance();
        //rDistance += driveTrain.rightEncoder.getDistance();
        SmartDashboard.putNumber("Gyro Angle", driveTrain.gyro.getAngle());
        SmartDashboard.putNumber("Gyro Rate", driveTrain.gyro.getRate());
        SmartDashboard.putNumber("Left Encoder Distance",driveTrain.leftEncoder.getDistance());
        SmartDashboard.putNumber("Right Encoder Distance",driveTrain.rightEncoder.getDistance());


        SmartDashboard.putNumber("Left Encoder Distance", driveTrain.leftEncoder.getDistance());
        SmartDashboard.putNumber("Right Encoder Distance", driveTrain.rightEncoder.getDistance());
        SmartDashboard.putNumber("Front Sonar Distance", driveTrain.getFrontSonarDist());
        SmartDashboard.putNumber("Side Sonar Distance", driveTrain.getSideSonarDist());  
        


        
        //System.out.println("Left Encoder Distance: "+driveTrain.leftEncoder.getDistance()+", Right Encoder Distance "+driveTrain.rightEncoder.getDistance());
        //System.out.println("Gyro Angle: "+driveTrain.gyro.getAngle());        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
