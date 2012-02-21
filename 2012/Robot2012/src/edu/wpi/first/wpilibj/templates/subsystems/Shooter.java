/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.ShooterIdle;

/**
 *
 * @author User
 */
public class Shooter extends Subsystem {

    Jaguar shootjag;
    DigitalInput lm;
    DigitalInput lt;
    Counter c;

    public Shooter() {
        shootjag = new Jaguar(RobotMap.SHOOTER_PORT);
        lm = new DigitalInput(RobotMap.LM_HIGH);
        lt = new DigitalInput(RobotMap.LINE_TRACKER_PORT);
        c = new Counter(lt);
        c.setMaxPeriod(.3);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ShooterIdle());
    }

    public boolean hasBallEntered() {
        boolean b = lm.get();
        SmartDashboard.putBoolean("high lm: ", b);
        return b;
    }

    public void idle() {
        shootjag.set(0);
        c.stop();
    }

    public void set(double x) {
        c.reset();
        c.start();
        shootjag.set(x);
        SmartDashboard.putDouble("Shooter Speed", RobotMap.roundtoTwo(shootjag.get()));
    }

    public void setRPS(double x) {
        c.reset();
        c.start();

    }

    public int getCount() {
        int count = c.get();
        SmartDashboard.putInt("counter", count);
        SmartDashboard.putDouble("period", c.getPeriod());
        rps();
        return count;
    }

    public double get() {
        return shootjag.get();
    }

    public double rps() {
        double val = 1.0 / (2.0 * c.getPeriod());
        SmartDashboard.putDouble("rps", RobotMap.roundtoTwo(val));
        return val;
    }
}
