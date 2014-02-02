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

    double left = .6;
    double right = .6;
    
    double leftDistance = 0.0;
    double rightDistance = 0.0;
    
    double leftDistancePerTick = 0.0;
    double rightDistancePerTick = 0.0;

    public AutonomousEncoderDrive() {
        requires(driveTrain);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        driveTrain.startEncoderAutonomous();
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        driveTrain.driveXboxTank(left, right);
        //driveTrain.driveXboxTank(left, right);
        
        /*double rightEncoderCompensation = driveTrain.leftEncoder.getDistance() - driveTrain.rightEncoder.getDistance();
        
        double increaseFactor = rightEncoderCompensation/200.0;
        
        if (rightEncoderCompensation > 0)
        {// 0.5 .... 5 .... 0.0025
            if (right >= 0.5) {
                left -= increaseFactor;
            } else {
                right += increaseFactor;
            }
        } else if (rightEncoderCompensation < 0) {
            if (left >= 0.5) {
                right -= increaseFactor;
            } else {
                left += increaseFactor;
            }
        }*/
        
        //if (driveTrain.gyro.getAngle() > 0.5) {
          //  if (right == 1.0) {
            //    left -= .0025;
           // } else {
             //   right += .0025;
         //   }
        //} else if (driveTrain.gyro.getAngle() < -0.5) {
          //  if (left == 1.0) {
            //    right -= .0025;
           // } else {
             //   left += .0025;
            //}
       // }
       
        
      
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
