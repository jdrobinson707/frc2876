/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.templates.commands.ConveyorLowIdle;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author User
 */
public class ConveyorLow extends Subsystem {
    //Jaguar conveyjaglow = new Jaguar(RobotMap.CONVEYOR_LOW_PORT);

    Relay conveyrelaylow = new Relay(RobotMap.CONVEYOR_LOW_PORT);
    DigitalInput lm = new DigitalInput(RobotMap.LM_LOW);
    int counter = 0;
   
    boolean previous = false;

    public void Count() {
        boolean b = lm.get();
        if (lm.get()) {
            if (b != previous) {
                counter++;
                SmartDashboard.putInt("Shooter Spins", counter);
            }
        }

    }

    public boolean hasBallEntered() {
        boolean b = lm.get();
        SmartDashboard.putBoolean("low lm: ", b);
        return b;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ConveyorLowIdle());
    }

    public void idle() {
        //conveyjaglow.set(0);
        conveyrelaylow.set(Relay.Value.kOff);
    }

    public void on() {
        //conveyjaglow.set(1);
        conveyrelaylow.set(Relay.Value.kForward);
    }

    public void reverse() {
        //conveyjaglow.set(-1);
        conveyrelaylow.set(Relay.Value.kReverse);
    }
}
