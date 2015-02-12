package org.usfirst.frc2876.RecycleRush2876.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomousIdea extends CommandGroup {
    
    public  AutonomousIdea() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	
    	
    	// This just an example of how we can use Commands to make the robot do something
    	// in autonomous mode.  
    	
    	// move elevator above a tote
    	addSequential(new ElevatorToPickup());
    	// drive straight for 2s to hit a tote
    	addSequential(new DriveStraight(), 2);
    	// drop elevator to bottom and then raise it so it can pickup a tote    	
    	addSequential(new ElevatorToBottom());
    	addSequential(new ElevatorToPickup());
    	// drive straight again to carry tote somewhere
    	addSequential(new DriveStraight(), 5);
    }
}
