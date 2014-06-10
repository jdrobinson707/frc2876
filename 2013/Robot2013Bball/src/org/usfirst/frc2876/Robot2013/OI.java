// Generated with RobotBuilder version 0.0.1
package org.usfirst.frc2876.Robot2013;

import edu.wpi.first.wpilibj.buttons.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc2876.Robot2013.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    XboxController xbox;
    int driveState;

    public Button leftBump;
    public Button rightBump;

    public OI() {

//        SmartDashboard.putData("ShooterDelay", new ShootDelay());
//        SmartDashboard.putData("Shoot", new Shoot());
//
//        SmartDashboard.putData("AngleShooterPID", new AdjustShooterAngle(0));
//        SmartDashboard.putData("AngleShooterPIDOn", new AngleShooterPIDOn());
//        SmartDashboard.putData("AngleShooterPIDOff", new AngleShooterPIDOff());
//
//        SmartDashboard.putData("DriveForward", new DriveForward(35));
//        SmartDashboard.putData("TurnRobot90", new TurnRobot(90));
//        SmartDashboard.putData("DriveStraight", new DriveForwardStraight(70));
        xbox = new XboxController(1);
        //xbox.lBumper.whileHeld(new ShooterRaise());
        //xbox.rBumper.whileHeld(new ShooterLower());
        xbox.A.whenPressed(new ShooterStart());
        xbox.B.whenPressed(new ShooterStop());
        xbox.X.whenPressed(new FireStart());
        xbox.Y.whenPressed(new FireStop());
        xbox.rBumper.whileHeld(new FireFrisbee());
        
    }

    public int getDriveState() {
        if (isStartPressed()) {
            driveState = 0;
        } else if (isBackPressed()) {
            driveState = 1;
        }
        SmartDashboard.putNumber("Drive State", driveState);
        return driveState;
    }

    public boolean isStartPressed() {
        return xbox.getButton(8); //start
    }

    public boolean isBackPressed() {
        return xbox.getButton(7); //back
    }

    public XboxController getXbox() {
        return xbox;
    }

    public double getXboxLeftY() {
        return xbox.getLeftY();
    }

    public double getXboxLeftX() {
        return xbox.getLeftX();
    }

    public double getXboxRightY() {
        return xbox.getRightY();
    }

    public double getXboxRightX() {
        return xbox.getRightX();
    }

}
