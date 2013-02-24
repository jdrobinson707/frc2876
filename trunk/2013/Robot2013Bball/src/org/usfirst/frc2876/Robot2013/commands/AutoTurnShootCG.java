/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc2876.Robot2013.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Student
 */
public class AutoTurnShootCG extends CommandGroup {

    public AutoTurnShootCG(boolean find3ptr) {
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
        //
        // use camera to find target
        addSequential(new Find2PtTarget());
        // turn robot to aim at target
        addSequential(new TurnRobotVision());
        // start the shooter wheel
        addSequential(new Shoot());
        // at the same time shooter starts adjust the angle of the shooter
        //addSequential(new AdjustShooterVision());
        // Need to add command to active thingy that will push frisbee into
        // shooter.

        //we need a way to stop the shooter when it is done shooting frisbee.
        // probably use a timer since we have no way to detect if  
        // we have any frisbees loaded.
        //addSequential(new ShootIdle());
    }
}
