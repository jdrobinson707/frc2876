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

    Relay conveyrelaylow;
    DigitalInput lm;
    int counter;
    boolean previous;
    Relay.Value relay_val = Relay.Value.kOff;

    public ConveyorLow() {
        conveyrelaylow = new Relay(RobotMap.CONVEYOR_LOW_PORT);
        conveyrelaylow.setDirection(Relay.Direction.kBoth);
        lm = new DigitalInput(RobotMap.LM_LOW);
        counter = 0;
        previous = false;
    }

    public void initDefaultCommand() {
        setDefaultCommand(new ConveyorLowIdle());
    }

    public boolean hasBall() {
        return lm.get();
    }

    public boolean hasBallEntered() {
        boolean b = lm.get();
        if (b && !previous) {
            // ball just hit lim
            previous = true;
            return true;
        } else if (b) {
            // ball is hitting lim
            return true;
        }
        // ball hasn't hit lim switch
        return false;
    }

    public boolean hasBallExited() {
        boolean b = lm.get();
        if (!b && previous) {
            // ball was pressing lim and has gone past lim
            previous = false;
            return true;
        } else if (b) {
            // ball is still pressing lim
            return false;
        }
        // ball is still hitting lim switch or never entered?
        return false;
    }

    public void incrCounter() {
        counter++;
        if (counter > 3) {
            counter = 3;
        }
    }
    public void decrCounter() {
        counter--;
        if (counter < 0) {
            counter = 0;
        }
    }
    public int getCounter() {
        return counter;
    }

    public void idle() {
        relay_val = Relay.Value.kOff;
        conveyrelaylow.set(relay_val);
    }

    public void forward() {
        relay_val = Relay.Value.kForward;
        conveyrelaylow.set(relay_val);
    }

    public void reverse() {
        relay_val = Relay.Value.kReverse;
        conveyrelaylow.set(relay_val);
    }

    public void updateDash() {
        SmartDashboard.putBoolean("LS_L", lm.get());
        SmartDashboard.putInt("lowconveyor", RobotMap.relayValToInt(relay_val));
        SmartDashboard.putInt("counter", counter);
        SmartDashboard.putBoolean("previous", previous);
    }
}
