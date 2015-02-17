// Generated with RobotBuilder version 0.0.1
/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.usfirst.frc2876.Robot2013;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc2876.Robot2013.commands.*;
import org.usfirst.frc2876.Robot2013.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    Command autonomousCommand;
    public static OI oi;
    // The following variables are automatically assigned by
    // robotbuilder and will be updated the next time you export to
    // Java from robot builder. Do not put any code or make any change
    // in the following block or it will be lost on an update. To
    // prevent this subsystem from being automatically updated, delete
    // the following line.
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static Shooter shooter;
    public static DriveTrain driveTrain;
    public static Vision vision;
    public static AngleShooter angleShooter;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        RobotMap.init();
        // The following variables are automatically assigned by
        // robotbuilder and will be updated the next time you export to
        // Java from robot builder. Do not put any code or make any change
        // in the following block or it will be lost on an update. To
        // prevent this subsystem from being automatically updated, delete
        // the following line.
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        shooter = new Shooter();
        driveTrain = new DriveTrain();
        vision = new Vision();
        angleShooter = new AngleShooter();
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        // instantiate the command used for the autonomous period
        // The following variables after automatically assigned by
        // robotbuilder and will be updated the next time you export to
        // Java from robot builder. Do not put any code or make any change
        // in the following block or it will be lost on an update. To
        // prevent this subsystem from being automatically updated, delete
        // the following line.
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        //autonomousCommand = new AutoTurnShootCG(true);
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        SmartDashboard.putData(Scheduler.getInstance());
        SmartDashboard.putData(angleShooter);
        SmartDashboard.putData(shooter);
        SmartDashboard.putData(driveTrain);
        SmartDashboard.putData(vision);

        testDs();
        //setupAutoChooser();
    }

    public void setupAutoChooser() {
        //System.out.println("setting up autochooser");
        //autoChooser = new SendableChooser();
        //autoChooser.addDefault("AutoTurnShoot", new AutoTurnShootCG(true));
        //autoChooser.addObject("Auto0", new cgAuto0());
        //autoChooser.addObject("Auto1", new cgAuto1());
        //SmartDashboard.putData("autonomous chooser", autoChooser);
    }

    public void testDs() {
        // Print out all the digital inputs. Try changing them 
        // in the driver station before enabling auto mode.
        DriverStation ds = DriverStation.getInstance();
        for (int i = 1; i <= 8; i++) {
            System.out.println("Digital Input " + i + " " + ds.getDigitalIn(i));
        }

        // Set the digital outputs on driver station.  Every time 
        // auto mode is enabled the even/odd outputs will toggle.
        /*boolean dioVal = ds.getDigitalOut(0);
        for (int i = 1; i <= 8; i++) {
            if (i % 2 == 0) {
                ds.setDigitalOut(i, !dioVal);
            } else {
                ds.setDigitalOut(i, dioVal);
            }
            System.out.println("Digital Output " + i + " " + ds.getDigitalOut(i));
        }*/
        boolean s = ds.getDigitalIn(0);
        if (s) {
            autonomousCommand = new Autonomous2FrisbeeCG(); //goes for 2 pt targets
        }
        else {
            autonomousCommand = new Autonomous3FrisbeeCG(); //goes for 3 pt targets   
        }
    }

    public void autonomousInit() {
        //testDs();
        // schedule the autonomous command (example)
        /*if (autoChooser != null) {
            autonomousCommand = (Command) autoChooser.getSelected();
            if (autonomousCommand != null) {
                System.out.println("Running auto " + autonomousCommand.getName());
                autonomousCommand.start();
            } else {
                System.out.println("auto command is null");
            }
        } else {
            System.out.println("autochooser is null");
        }*/
        
        autonomousCommand.start();
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
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        LiveWindow.run();
        SmartDashboard.putData(Scheduler.getInstance());
        SmartDashboard.putData(vision);
        driveTrain.updateDashboard();
                
    }

    public void testPeriodic() {
        LiveWindow.run();
    }
}