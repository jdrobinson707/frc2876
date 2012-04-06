/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author user
 */
public class cgShootTwoBall extends CommandGroup {

    public cgShootTwoBall(double rps) {
        // addParallel(new ShooterStart());
        //addParallel(new VisionFilter());
        //addParallel(new ShooterSet(RobotMap.START_SPEED_RPS));
        addParallel(new ShooterRPS(rps));

        //addSequential(new ShooterLoad());
        addParallel(new ShooterLoad());
        addSequential(new ShooterFire(4));

        //addSequential(new ConveyorHighOn());
        //addSequential(new WaitCommand("wait for shot", 3));
        //addSequential(new ConveyorLowOn());

        addSequential(new ShooterLoad());
        addSequential(new ShooterFire(9));
        //addSequential(new WaitCommand("wait for shot", 5));
        addSequential(new ShooterIdle());
        // addSequential(new ConveyorHighIdle());

    }
}
