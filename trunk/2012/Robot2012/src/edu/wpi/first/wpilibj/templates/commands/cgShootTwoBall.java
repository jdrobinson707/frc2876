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
public class cgShootTwoBall extends CommandGroup {

    public cgShootTwoBall() {
        addParallel(new ShooterStart());
        addSequential(new ShooterLoad());
        addSequential(new ShooterFire());
        addSequential(new ShooterLoad());
        addSequential(new ShooterFire());
        addSequential(new ShooterIdle());
    }
}
