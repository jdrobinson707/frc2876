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
public class Arm extends Thread {

    DriverStation ds;
    AnalogChannel ac;
    PIDController mc;
    Jaguar armJag;
    Solenoid grip1;
    Solenoid grip2;
    Solenoid armExtension1;
    Solenoid armExtension2;
    double value;
    boolean running;

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

        //grip1.set(true);
        //grip2.set(false);

        //armExtension1.set(true);
        //armExtension2.set(false);
    }

    public void armMovement(double value) {
        // do something with value
        this.value = value;
        if (!running) {
            this.start();
            running = true;
        }
    }

    private boolean runMovement() {

        boolean foundTarget = false;

        if (mc.onTarget()) {
            mc.reset();
            System.out.println("reset mc");
            foundTarget = true;
        }

        mc.setSetpoint(value);
        mc.enable();

        System.out.println("Set: " + value + " Roller Speed: "
                + armJag.getSpeed() + " onTarget: " + mc.onTarget()
                + " Error: " + mc.getError() + " Value: " + ac.pidGet());

        return foundTarget;
    }

    public void OpenClaw(boolean grip1, boolean grip2) {
        this.grip1.set(grip1);
        this.grip2.set(grip2);
    }

    public void ExtendArm(boolean arm1, boolean arm2) {
        armExtension1.set(arm1);
        armExtension2.set(arm2);
    }

    public void set(double value) {
        mc.disable();
        armJag.set(value);
    }

    public void run() {
        while (this.runMovement() && running) {
        }
        running = false;
        mc.reset();
    }

    public void stopArm()
    {
        running = false;
        armJag.set(0.0);
    }
}
