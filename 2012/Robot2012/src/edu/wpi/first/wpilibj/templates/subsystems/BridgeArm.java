/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.BridgeArmMove;

/**
 *
 * @author User
 */
public class BridgeArm extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    AnalogChannel ac = new AnalogChannel(RobotMap.PONTENTIOMETER_PORT);
    Jaguar bridgearmjag = new Jaguar(RobotMap.BRIDGE_ARM_PORT);

    public void move(double speed)
    {
        bridgearmjag.set(speed);
        SmartDashboard.putDouble("Potentiometer", ac.pidGet());
    }

    public void raise()
    {

    }

    public void lower()
    {

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new BridgeArmMove());
    }
}