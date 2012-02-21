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
public class BridgeArmManual extends Subsystem {

    Jaguar bridgearmjag = new Jaguar(RobotMap.BRIDGE_ARM_PORT);
    AnalogChannel pot = new AnalogChannel(RobotMap.PONTENTIOMETER_PORT);

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new BridgeArmMove());
    }

    public void nothing() {
    }

    public void updateDashboard() {
        SmartDashboard.putDouble("Potentiometer", pot.pidGet());
        SmartDashboard.putDouble("bridge motor", RobotMap.roundtoTwo(bridgearmjag.get()));
    }

    public void move(double speed) {
        if (pot.pidGet() > (RobotMap.BRIDGE_ARM_DOWN_LIMIT - 5) && pot.pidGet() < (RobotMap.BRIDGE_ARM_UP_LIMIT + 5)) {
            bridgearmjag.set(-speed);
        } else {
            bridgearmjag.set(0);
        }
        updateDashboard();
    }

}
