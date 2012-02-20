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

/**
 *
 * @author User
 */
public class BridgeArm extends Subsystem {

    AnalogChannel pot;
    Jaguar bridgearmjag;

    // TODO
    // Need to find the pot voltages for arm raised and arm lowered.
    // Manually move the arm with pot attached and print out
    // the voltage when the arm is in desired position
    //
    // Make this class extend PIDSubsystem or create a pid object
    // inside it(like turnPID in drive subsystem).  Use button presses
    // to make the arm go up/down by setting a setpoint.

    public BridgeArm() {
        bridgearmjag = new Jaguar(RobotMap.BRIDGE_ARM_PORT);
        pot = new AnalogChannel(RobotMap.PONTENTIOMETER_PORT);
    }

    public void updateDashboard() {
        SmartDashboard.putDouble("Potentiometer", pot.pidGet());
        SmartDashboard.putDouble("bridge motor", bridgearmjag.get());
    }

    public void move(double speed) {
        bridgearmjag.set(speed);
        updateDashboard();
    }

    public void raise() {
    }

    public void lower() {
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new BridgeArmMove());
        
        // TODO default command should be do nothing/idle.
    }
}
