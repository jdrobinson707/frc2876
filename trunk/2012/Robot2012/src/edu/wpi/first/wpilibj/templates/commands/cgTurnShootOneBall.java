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
public class cgTurnShootOneBall extends CommandGroup {

    public cgTurnShootOneBall() {
        addSequential(new VisionTurn());
        addSequential(new cgShootOneBall());
    }
}
