/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.Pimpbot2011.subsystems;

import edu.wpi.first.Pimpbot2011.RobotMap;
import edu.wpi.first.Pimpbot2011.commands.ClawDoNothing;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author maciej
 */
public class Claw extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    
    Solenoid solenoid;

    public Claw() {
        solenoid = new Solenoid(RobotMap.SOLENOID_SLOT,
                RobotMap.GRIP_SOLENOID_1_CHANNEL);
        
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ClawDoNothing());
    }

    public void open() {
        solenoid.set(false);
    }

    public void close() {
        solenoid.set(true);
    }

    public void doNothing() {
        return;
    }
}
