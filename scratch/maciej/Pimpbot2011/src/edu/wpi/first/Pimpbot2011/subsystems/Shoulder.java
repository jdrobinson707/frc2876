/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.Pimpbot2011.subsystems;

import edu.wpi.first.Pimpbot2011.RobotMap;
import edu.wpi.first.Pimpbot2011.commands.ShoulderWithJoystick;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author maciej
 */
public class Shoulder extends PIDSubsystem {

    private static final double Kp = 0.25;
    private static final double Ki = 0.001;
    private static final double Kd = 0.0;
    public static final double LOW = .68, HIGH = 3.0;
    Jaguar jaguar;
    AnalogChannel pot;

    // Initialize your subsystem here
    public Shoulder() {
        super("Shoulder", Kp, Ki, Kd);

        jaguar = new Jaguar(RobotMap.ARM_JAGUAR);
        pot = new AnalogChannel(RobotMap.ANALOG_SLOT,
            RobotMap.POT_CHANNEL);
        SmartDashboard.putData("ShoulderPid", this);
        setSetpointRange(LOW, HIGH);
        // setSetpoint(LOW);
        // enable();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        
        setDefaultCommand(new ShoulderWithJoystick());
    }

    protected double returnPIDInput() {
        return pot.getVoltage();
    }

    protected void usePIDOutput(double output) {
        jaguar.set(output);
        SmartDashboard.putDouble("shoulder pid output", output);
    }

    public void set(double output) {
        jaguar.set(output);
        SmartDashboard.putDouble("arm speed", output);
        SmartDashboard.putDouble("ShoulderPot", RobotMap.roundtoTwo(pot.getVoltage()));
    }
}
