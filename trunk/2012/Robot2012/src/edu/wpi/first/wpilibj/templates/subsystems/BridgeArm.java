/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendablePIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.BridgeArmMove;

/**
 *
 * @author User
 */
public class BridgeArm extends Subsystem {

    AnalogChannel pot;
    Jaguar jag;
    private static double bridgeKp = 1.0;
    private static double bridgeKi = 0.0;
    private static double bridgeKd = 0.0;
    SendablePIDController pid;

    // TODO
    // Need to find the pot voltages for arm raised and arm lowered.
    // Manually move the arm with pot attached and print out
    // the voltage when the arm is in desired position
    //
    public BridgeArm() {
        jag = new Jaguar(RobotMap.BRIDGE_ARM_PORT);
        pot = new AnalogChannel(RobotMap.PONTENTIOMETER_PORT);

        pid = new SendablePIDController(bridgeKp, bridgeKi, bridgeKd,
                pot, new PIDOutput() {

            public void pidWrite(double output) {
                jag.set(-output);
            }
        });

        pid.setTolerance(10);
        pid.setOutputRange(-0.3, 0.3);
    }

    public void move(double speed) {
        // THis won't work! Once the arm moves outside the up/down limit the motor won't work.
        // We had the same problem programming the arm last year. Better off leaving out the pot if you want to control
        // by joystick. We will make some kind of thingy on the dashboard tell the driver if arm is lowered or raised.
        // if (pot.pidGet() < RobotMap.BRIDGE_ARM_UP_LIMIT 
        //     && pot.pidGet() > RobotMap.BRIDGE_ARM_DOWN_LIMIT) {
        // if (pot.pidGet() > (RobotMap.BRIDGE_ARM_DOWN_LIMIT - 5)
        //     && pot.pidGet() < (RobotMap.BRIDGE_ARM_UP_LIMIT + 5)) {

        // pid.disable();
        
        jag.set(-speed);

    }

    public void raise() {
        pid.reset();
        pid.setSetpoint(RobotMap.BRIDGE_ARM_UP_LIMIT);
        pid.enable();
        Timer.delay(.1);
    }

    public void lower() {
        pid.reset();
        pid.setSetpoint(RobotMap.BRIDGE_ARM_DOWN_LIMIT);
        pid.enable();
        Timer.delay(.1);
    }

    public void idle() {
        // pid.disable();
        jag.set(0);
    }

    public boolean hasFinished(boolean debug) {
        if (debug) {
            System.out.println("is arm done: " + pid.onTarget()
                    + " volt:" + RobotMap.roundtoTwo(pot.getVoltage())
                    + " pidinput:" + RobotMap.roundtoTwo(pot.pidGet())
                    + " out:" + RobotMap.roundtoTwo(pid.get())
                    + " err:" + RobotMap.roundtoTwo(pid.getError()));
        }
        return pid.onTarget();
    }

    public void initDefaultCommand() {
        // setDefaultCommand(new BridgeArmNothing());
        setDefaultCommand(new BridgeArmMove());
    }

    public void updateDash() {
        // this is the dial that shows arm position.
        SmartDashboard.putDouble("A", RobotMap.roundtoTwo(pot.getVoltage()));

        // This is all debug data
        SmartDashboard.putData("BR_PID", pid);
        SmartDashboard.putBoolean("BR_PID_en", pid.isEnable());
                SmartDashboard.putBoolean("BR_PID_ontarget", pid.onTarget());
        SmartDashboard.putDouble("BR_PID_out", RobotMap.roundtoTwo(pid.get()));
        SmartDashboard.putDouble("BR_PID_err", RobotMap.roundtoTwo(pid.getError()));
        SmartDashboard.putDouble("BR_PID_input", RobotMap.roundtoTwo(pot.pidGet()));
        SmartDashboard.putDouble("BR_motor", RobotMap.roundtoTwo(jag.get()));
    }
}
