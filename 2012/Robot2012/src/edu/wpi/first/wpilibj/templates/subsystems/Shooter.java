/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendablePIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.ShooterIdle;

/**
 *
 * @author user
 */
public class Shooter extends Subsystem {

    private static final double Kp = 1.0;
    private static final double Ki = 1.0;
    private static final double Kd = .2;
    SendablePIDController pid;
    Jaguar shootjag;
    DigitalInput lm;
    DigitalInput lt;
    Counter c;
    double maxPeriod = .3;
    boolean ball;

    public class ShooterCounter implements PIDSource {

        public double pidGet() {
            return getRPS();
        }
    }

    public Shooter() {
        shootjag = new Jaguar(RobotMap.SHOOTER_PORT);
        lm = new DigitalInput(RobotMap.LM_HIGH);
        lt = new DigitalInput(RobotMap.LINE_TRACKER_PORT);
        c = new Counter(lt);

        pid = new SendablePIDController(Kp, Ki, Kd,
                new ShooterCounter(), shootjag);
        SmartDashboard.putData("SH_PID", pid);
        pid.setOutputRange(0.0, .8);
        pid.setSetpoint(0);
        pid.setTolerance(10.0);
        pid.enable();

    }

    public void initDefaultCommand() {
        setDefaultCommand(new ShooterIdle());
    }

    public boolean hasBallEntered() {
        ball = lm.get();
        return ball;
    }

    public void idle() {
        pid.setSetpoint(0);
    }

    public void stop() {
        pid.disable();
        shoot(0);
        shootjag.set(0);
        c.stop();
    }

    public void start() {
        c.reset();
        c.start();
        pid.enable();
    }

    public void shoot(double rps) {
        pid.setSetpoint(rps);
    }

    public void set(double x) {
        pid.disable();
        c.reset();
        c.start();
        shootjag.set(x);
    }

    public void resetRPS(double x) {
        c.reset();
        c.start();
    }

    public int getCount() {
        int count = c.get();
        return count;
    }

    public double getSpeed() {
        return shootjag.get();
    }

    public double getRPS() {
        double val = 1.0 / (2.0 * c.getPeriod());
        return val;
    }

    public boolean isReady() {
        return pid.onTarget();
    }

    public boolean isDone() {
        return false;
    }

    public void updateDash() {
        // Display driver info
        SmartDashboard.putDouble("RPS", RobotMap.roundtoTwo(getRPS()));
        SmartDashboard.putDouble("SH_speed", RobotMap.roundtoTwo(getSpeed()));

        // This is debug data
        SmartDashboard.putBoolean("LS_H", hasBallEntered());
        SmartDashboard.putInt("SH_counter", getCount());
        SmartDashboard.putDouble("SH_period", c.getPeriod());

        SmartDashboard.putBoolean("SH_PID_en", pid.isEnable());
        SmartDashboard.putBoolean("SH_PID_ontarget", pid.onTarget());
        SmartDashboard.putDouble("SH_PID_err", pid.getError());
        SmartDashboard.putDouble("SH_PID_out", RobotMap.roundtoTwo(pid.get()));
        // SmartDashboard.putData("SH_PID", pid);
    }

    public static double inchesToRps(double inches) {

        
        // TODO ERIC
        // Fill in this function to convert inches to rps.
        // We have been using 17 for shooting from top of key which is...?
        // I don't remember, maybe 10 or 15 ft? Anyhoo... start with setting
        // up the robot at a distance that is top of key and 17 works.
        // Then fill in or add the if/else stmts to adjust rps to any ranges.
        // OR you can make a function like you suggested.
        // Just remember that before you fire a ball you need to press
        // the 'aim or 2' button to acquire an image.
        // If the shooter fails to shoot a ball use the trigger to
        // reverse the upper conveyor and lower the ball so you can shoot again.

        double rps = 0;
        if (inches > 0 && inches <= 24) {
            rps = 15;
        } else if (inches > 24 && inches <= 120) {
            rps = 17;
        } else if (inches > 120) {
            rps = 19;
        }
        return rps;
    }
}
