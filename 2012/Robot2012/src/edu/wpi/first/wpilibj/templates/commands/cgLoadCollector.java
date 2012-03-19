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
public class cgLoadCollector extends CommandGroup {

    public cgLoadCollector() {
        addSequential(new CollectorIntakeBall());
        addSequential(new CollectorMoveBallUp());
    }
}