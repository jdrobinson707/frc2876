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
    Relay conveyrelayhigh = new Relay(RobotMap.CONVYOR_HIGH_PORT);
     DigitalInput lm = new DigitalInput(RobotMap.LM_MIDDLE);


    public void initDefaultCommand() {
        conveyrelayhigh.setDirection(Relay.Direction.kBoth);
        // Set the default command for a subsystem here.
        setDefaultCommand(new ConveyorHighIdle());
    }
    public void idle(){
    //conveyjaghigh.set(0);
    conveyrelayhigh.set(Relay.Value.kOff);
    
    }

    public boolean hasBallEntered() {
        boolean b = lm.get();
        SmartDashboard.putBoolean("middle lm: ", b);
        return b;
    }


    public void on(){
    //conveyjaghigh.set(1);
    conveyrelayhigh.set(Relay.Value.kReverse);
    }
    public void reverse(){
    //conveyjaghigh.set(-1);
    conveyrelayhigh.set(Relay.Value.kForward);
    }
}