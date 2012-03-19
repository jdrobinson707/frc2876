/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.ConveyorHighIdle;

/**
 *
 * @author User
 */
public class ConveyorHigh extends Subsystem {
    //Jaguar conveyjaghigh = new Jaguar(RobotMap.CONVYOR_HIGH_PORT);

    Relay conveyrelayhigh;
    DigitalInput lm;
    Relay.Value relay_val = Relay.Value.kOff;

    public ConveyorHigh() {
        conveyrelayhigh = new Relay(RobotMap.CONVYOR_HIGH_PORT);
        conveyrelayhigh.setDirection(Relay.Direction.kBoth);
        lm = new DigitalInput(RobotMap.LM_MIDDLE);
    }

    public void initDefaultCommand() {

        // Set the default command for a subsystem here.
        setDefaultCommand(new ConveyorHighIdle());
    }

    public void idle() {
        relay_val = Relay.Value.kOff;
        conveyrelayhigh.set(relay_val);
    }

    public boolean hasBall() {
        return lm.get();
    }

    public void forward() {
        relay_val = Relay.Value.kReverse;
        conveyrelayhigh.set(relay_val);
    }

    public void reverse() {
        relay_val = Relay.Value.kForward;
        conveyrelayhigh.set(relay_val);
    }

    public void updateDash() {
        // driver info
        SmartDashboard.putInt("highconveyor", RobotMap.relayValToInt(relay_val));

        // debug data
        SmartDashboard.putBoolean("LS_M", lm.get());
    }
}
