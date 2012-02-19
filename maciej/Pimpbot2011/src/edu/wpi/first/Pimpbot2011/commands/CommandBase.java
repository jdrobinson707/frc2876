package edu.wpi.first.Pimpbot2011.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.Pimpbot2011.OI;
import edu.wpi.first.Pimpbot2011.subsystems.Arm;
import edu.wpi.first.Pimpbot2011.subsystems.CamTarget;
import edu.wpi.first.Pimpbot2011.subsystems.Claw;
import edu.wpi.first.Pimpbot2011.subsystems.DriveTrain;
import edu.wpi.first.Pimpbot2011.subsystems.Shoulder;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    
    public static Claw claw = new Claw();
    public static Arm arm = new Arm();
    public static Shoulder shoulder = new Shoulder();
    public static DriveTrain drivetrain = new DriveTrain();
    public static CamTarget camtarget = new CamTarget();

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        // Show what command your subsystem is running on the SmartDashboard
        SmartDashboard.putData(claw);
        SmartDashboard.putData(arm);
        SmartDashboard.putData(shoulder);
        SmartDashboard.putData(drivetrain);
        SmartDashboard.putData(camtarget);


    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
