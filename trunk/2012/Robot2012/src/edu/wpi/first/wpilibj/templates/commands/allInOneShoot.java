/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author User
 */
public class allInOneShoot extends CommandGroup {

    public allInOneShoot() {

        addParallel(new ConveyorLowOn());
        //addSequential(new Command1());
        //(new Command1());

    }
}