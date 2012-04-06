/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author user
 */
public class cgShootOneBall extends CommandGroup {

    public cgShootOneBall(double rps) {
        //addParallel(new ShooterStart());
        addParallel(new VisionFilter());
        //addParallel(new ShooterSet(.62));
        addParallel(new ShooterRPS(rps));
        addSequential(new ShooterLoad());
        addSequential(new ShooterFire(5));
        addSequential(new ShooterIdle());
    }
}