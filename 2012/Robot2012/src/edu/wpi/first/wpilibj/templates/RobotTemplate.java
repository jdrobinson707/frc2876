/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.commands.cgAutonomous;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // instantiate the command used for the autonomous period
        autonomousCommand = new cgAutonomous();
        // Initialize all subsystems
        CommandBase.init();
        SmartDashboard.putData("SchedulerData", Scheduler.getInstance());
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateDash();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        //autonomousCommand.cancel();
//        CommandBase.shooter.stop();
//        CommandBase.conveyorhigh.idle();
//        CommandBase.conveyorlow.idle();
//        CommandBase.drive.init();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        updateDash();

    }
    int dashctr = 0;

    public void updateDash() {
        CommandBase.shooter.updateDash();
        if (CommandBase.oi.isDebugOn()) {
            dashctr++;
            if (dashctr % 10 == 0) {
                CommandBase.conveyorhigh.updateDash();
                CommandBase.conveyorlow.updateDash();
            } else if (dashctr % 13 == 0) {
            } else if (dashctr % 17 == 0) {
                CommandBase.drive.updateDash();
            }
            SmartDashboard.putDouble("Arm Z", CommandBase.oi.getArmZ());

            CommandBase.bridgearm.updateDash();
            //CommandBase.cameratarget.updateDash();

//        SmartDashboard.putBoolean("Debug", CommandBase.oi.isDebugOn());
//        SmartDashboard.putDouble("Drive Mode", CommandBase.oi.getDriveMode());
        }
    }
}
