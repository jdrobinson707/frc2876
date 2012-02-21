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
import edu.wpi.first.wpilibj.templates.commands.BridgeArmNothing;

/**
 *
 * @author User
 */
public class BridgeArm extends Subsystem {

    //AnalogChannel pot;
    //Jaguar bridgearmjag;
    private static double bridgeKp = 1.0;
    private static double bridgeKi = 0.0;
    private static double bridgeKd = 0.0;
    SendablePIDController bridgePID;
    SendablePIDController testPID;

    // TODO
    // Need to find the pot voltages for arm raised and arm lowered.
    // Manually move the arm with pot attached and print out
    // the voltage when the arm is in desired position
    //
    // Make this class extend PIDSubsystem or create a pid object
    // inside it(like turnPID in drive subsystem).  Use button presses
    // to make the arm go up/down by setting a setpoint.
    public BridgeArm() {
       //bridgearmjag = new Jaguar(6);
       // pot = new AnalogChannel(6);
      //  bridgePID = new SendablePIDController(bridgeKp, bridgeKi, bridgeKd, pot, new PIDOutput() {

     //       public void pidWrite(double output) {
     //           bridgearmjag.set(-output);
     //           SmartDashboard.putDouble("LoweringPID Output", RobotMap.roundtoTwo(output));
     //           SmartDashboard.putDouble("LoweringPID Error", RobotMap.roundtoTwo(bridgePID.getError()));
    //        }
     //   });

//        bridgePID.setOutputRange(-0.3, 0.3);
//        Timer.delay(.3);
//        bridgePID.setInputRange(RobotMap.BRIDGE_ARM_DOWN_LIMIT, RobotMap.BRIDGE_ARM_UP_LIMIT);
//
//        SmartDashboard.putData("BridgePID Object", bridgePID);
    }

    public void updateDashboard() {
//        SmartDashboard.putDouble("Potentiometer", pot.pidGet());
//        SmartDashboard.putDouble("bridge motor", RobotMap.roundtoTwo(bridgearmjag.get()));
    }

    public void move(double speed) {
//        if (pot.pidGet() > (RobotMap.BRIDGE_ARM_DOWN_LIMIT) && pot.pidGet() < (RobotMap.BRIDGE_ARM_UP_LIMIT)) {
//            bridgearmjag.set(speed);
//        } else {
//            bridgearmjag.set(0);
//        }
//        updateDashboard();
    }

    public void raise() {
//        bridgePID.setSetpoint(RobotMap.BRIDGE_ARM_UP_LIMIT);
//        bridgePID.enable();
    }

    public void lower() {
//        bridgePID.setSetpoint(RobotMap.BRIDGE_ARM_DOWN_LIMIT);
//        bridgePID.enable();
    }

    public void enablePID()
    {
//        bridgePID.enable();
    }

    public void idle() {
//        bridgePID.disable();
//        bridgePID.reset();
    }

    public boolean hasFinished() {
//        if (bridgePID.getError() > -200 && bridgePID.getError() < 200) {
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    public void resetPID()
    {
//        bridgePID.reset();
    }

    public void lockArm()
    {

    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new BridgeArmNothing());

        // TODO default command should be do nothing/idle.
    }
}
