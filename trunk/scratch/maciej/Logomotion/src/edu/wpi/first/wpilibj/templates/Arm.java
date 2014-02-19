/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author maciej
 */
public class Arm extends Thread {

    Solenoid s1;
    Solenoid s2;
    AnalogChannel ac;
    PIDController armPid;
    Jaguar armJag;
    DriverStation ds;
    public static final double min_volt = .75;
    public static final double max_volt = 3.5;
    public static final double min_pid = 150.0;
    public static final double max_pid = 650.0;
    public static final double setpoint;

    public String toString() {
        return ""
                + " set:" + armPid.getSetpoint()
                + " err:" + armPid.getError()
                + " get:" + armPid.get()
                + " tar:" + armPid.onTarget()
                + " isPidEnable:" + armPid.isEnable()
                + " ac.pidGet" + ac.pidGet();
    }

    public Arm() {
        ds = DriverStation.getInstance();
        armJag = new Jaguar(Constants.ARM_MOTOR);
        ac = new AnalogChannel(1, Constants.POT_CHANNEL);
        armPid = new PIDController(.25, 0.001, 0, ac, armJag);
        armPid.setTolerance(5);
        armPid.setInputRange(min_pid, max_pid);
        armPid.setOutputRange(-1, 1);

        s1 = new Solenoid(8, 8);
        s2 = new Solenoid(8, 7);

    }

    public void height(double setpoint) {

        if (setpoint != 0.0) {
            armPid.setSetpoint(setpoint);
        } else {
            return;
        }
        armPid.enable();
        while (armPid.isEnable() && !armPid.onTarget() && ds.isEnabled()) {
            double volt = ac.getAverageVoltage();
            if (volt < min_volt || volt > max_volt) {
                System.out.println("OUT OF RANGE: " + volt);
                armPid.disable();
                break;
            }
            printPot();
            Timer.delay(0.5);
        }
        armPid.reset();
        printPot();
    }

    public void printPot() {

        int newValue = (int) (ac.getVoltage() * 100);
        double finalValue = (double) (newValue / 100.0);
        
        System.out.println("Pot:" + finalValue + "  pidval=" + ac.pidGet());
    }

    public void set(double speed) {
        armJag.set(speed);
    }

    public void run() {

        while (armPid.isEnable()) {
            Timer.delay(.05);
        }

    }

    public void moveArm(double setpoint) {
        if () {
            this.setpoint = setpoint;
        }

        this.start();

    }

    public void stopArm() {
        armPid.disable();
        armJag.set(0);
    }
}
