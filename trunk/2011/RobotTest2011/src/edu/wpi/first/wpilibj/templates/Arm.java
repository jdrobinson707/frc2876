/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author User
 */
public class Arm {

    DriverStation ds;
    AnalogChannel ac;
    PIDController mc;
    Jaguar armJag;
    Solenoid grip;
    Solenoid armExtension1;
    Solenoid armExtension2;

    public String toString() {
        return ""
                + " set:" + mc.getSetpoint()
                + " error:" + mc.getError()
                + " get: " + mc.get()
                + " On target: " + mc.onTarget()
                + " pidGet: " + ac.pidGet()
                + " Speed:" + armJag.getSpeed();
    }

    public Arm() {
        ds = ds.getInstance();
        armJag = new Jaguar(Constants.ARM_JAGUAR);
        ac = new AnalogChannel(Constants.ANALOG_CHANNEL_SLOT, Constants.ANALOG_CHANNEL_CHANNEL);
        mc = new PIDController(.25, 0.001, 0, ac, armJag);
        mc.setTolerance(5.0);
        mc.setOutputRange(-.7, .7);
        mc.setInputRange(150, 650);

        armExtension1 = new Solenoid(8, 1);
        armExtension2 = new Solenoid(8, 2);
        grip = new Solenoid(8, 3);

        grip.set(true);

        armExtension1.set(true);
        armExtension2.set(false);
    }

    public void runMovement(double value) {

        if (mc.onTarget()) {
            mc.reset();
            System.out.println("reset mc");
        }

        mc.setSetpoint(value);
        mc.enable();

        System.out.println("Set: " + value + " Roller Speed: "
                + armJag.getSpeed() + " onTarget: " + mc.onTarget()
                + " Error: " + mc.getError() + " Value: " + ac.pidGet());

    }

    public void OpenClaw(boolean grip1) {
        grip.set(grip1);
        System.out.println("Grip");
    }

    public void ExtendArm(boolean arm1, boolean arm2) {
        armExtension1.set(arm1);
        armExtension2.set(arm2);
        System.out.println("Extend Arm");
    }

    public void set(double value) {
        mc.disable();
        armJag.set(value);
    }
}
