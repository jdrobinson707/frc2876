/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.Pimpbot2011;

import edu.wpi.first.Pimpbot2011.commands.ClawOpen;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.Pimpbot2011.commands.CommandBase;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Pimpbot2011 extends IterativeRobot {

    Command autonomousCommand;
    Compressor compressor;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // instantiate the command used for the autonomous period
        // autonomousCommand = new ExampleCommand();

        CommandBase.init();

        SmartDashboard.putData("SchedulerData", Scheduler.getInstance());

        // compressor = new Compressor(11, 2);
        // compressor.start();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        // autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        // autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        //Command c = new ClawOpen();
        //c.start();
    }

    public void printToClassmate() {

        DriverStationLCD driverStation = DriverStationLCD.getInstance();
        driverStation.println(DriverStationLCD.Line.kUser2, 1, "");
        driverStation.println(DriverStationLCD.Line.kUser3, 1, "");
        driverStation.println(DriverStationLCD.Line.kUser4, 1, "");
        driverStation.println(DriverStationLCD.Line.kUser5, 1, "");
        driverStation.println(DriverStationLCD.Line.kUser6, 1, "");

        driverStation.updateLCD();
    }
}
