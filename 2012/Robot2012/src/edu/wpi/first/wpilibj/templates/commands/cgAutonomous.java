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
public class cgAutonomous extends CommandGroup {

    public cgAutonomous() {
        addSequential(new cgTurnShootTwoBall(), 14);
    }
}