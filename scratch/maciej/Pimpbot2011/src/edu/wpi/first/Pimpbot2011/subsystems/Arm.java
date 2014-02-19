/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.Pimpbot2011.subsystems;

import edu.wpi.first.Pimpbot2011.RobotMap;
import edu.wpi.first.Pimpbot2011.commands.ArmDoNothing;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author maciej
 */
public class Arm extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    Solenoid extend, retract;

    public Arm() {
        extend = new Solenoid(RobotMap.SOLENOID_SLOT, RobotMap.ARM_SOLENOID_1_CHANNEL);
        retract = new Solenoid(RobotMap.SOLENOID_SLOT, RobotMap.ARM_SOLENOID_2_CHANNEL);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ArmDoNothing());
    }
    public void extend() {
        //extend.set(false);
        //retract.set(true);
    }
    public void retract() {
        //extend.set(true);
        //retract.set(false);
    }
    public void doNothing() {
        return;
    }
}
