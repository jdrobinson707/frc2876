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
public class Arm {

    Solenoid s1;
    Solenoid s2;
    AnalogChannel ac;
    PIDController armPid;
    Jaguar armJag;
    double min_volt = .75;
    double max_volt = 3.5;
    double min_pid = 150.0;
    double max_pid = 650.0;

    public String toString() {
        return ""
                + " set:" + armPid.getSetpoint()
                + " err:" + armPid.getError()
                + " get:" + armPid.get()
                + " tar:" + armPid.onTarget()
                + " ac.pidGet" + ac.pidGet();
    }

    public Arm() {
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
        while (!armPid.onTarget()) {
            double volt = ac.getAverageVoltage();
            if (volt < min_volt + .5 || volt > max_volt - .5) {
                System.out.println("OUT OF RANGE: " + volt);
            }
            printPot();
            Timer.delay(0.5);
        }
        armPid.reset();
        printPot();
    }

    public void printPot() {
        double newValue;
        int intValue;
        double finalValue;
        double prevValue;

        newValue = (ac.getVoltage() * 100);
        intValue = (int) (newValue);
        finalValue = (double) (intValue / 100.0);
        // System.out.println("Pot:" + finalValue + "  voltage:" + ac.getVoltage());
        System.out.println("Pot:" + finalValue + "  pidval=" + ac.pidGet());
    }

    public void set(double speed) {
        armJag.set(speed);
    }
}
