/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc2876.Robot2013.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc2876.Robot2013.RobotMap;
import org.usfirst.frc2876.Robot2013.commands.AngleShooterJoystick;

/**
 *
 * @author Student
 */
public class AngleShooter extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    DigitalInput lowlm = RobotMap.LOW_LM;
    DigitalInput highlm = RobotMap.HIGH_LM;
    AnalogChannel ac = RobotMap.SHOOTER_POT;
    Jaguar shootingAngleJaguar = RobotMap.SHOOTER_SHOOTINGANGLE_JAGUAR;
    // final double LOW_LIMIT = 1.032;
    //final double HIGH_LIMIT = .636;
    final double LOW_LIMIT = 0.87;
    final double HIGH_LIMIT = 4.0;
    PIDController scPID;

    public AngleShooter() {
        potInit();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new AngleShooterJoystick());
    }

    final void potInit() {
        scPID = new PIDController(.01, 0, 0, ac, new PIDOutput() {
            public void pidWrite(double output) {
                if (lowlm.get()) {
                    scPID.disable();
                    SmartDashboard.putString("angle LM", "LOW HIT");
                } else if (highlm.get()) {
                    scPID.disable();
                    SmartDashboard.putString("angle LM", "HIGH HIT");
                } else {
                    shootingAngleJaguar.set(-output);
                    //shootingAngleJaguar.set(output);
                }
            }
        });
        scPID.setPercentTolerance(5.0);
        //scPID.setOutputRange(-.5, .5);
        scPID.setOutputRange(-.2, .2);
        LiveWindow.addActuator("AngleShooter", "angle PID", scPID);

    }

    public void setPotSetpoint(int i) {
        scPID.setSetpoint(i);
        SmartDashboard.putNumber("anglePID setpoint", scPID.getSetpoint());
    }

    public boolean isFinishedAdjustingShooter() {
        SmartDashboard.putNumber("anglePID Error", scPID.getError());
        SmartDashboard.putBoolean("anglePID ontarget", scPID.onTarget());
        //return scPID.onTarget();
        return (Math.abs(scPID.getError()) < 2);
    }

    public boolean isShooterPIDOn() {
        return scPID.isEnable();
    }

    public void startShooterPID() {
        SmartDashboard.putString("angle LM", "OK");
        scPID.enable();
    }

    public void endShooterPID() {
        scPID.reset();
        scPID.disable();
        shootingAngleJaguar.set(0);
    }

    public void jaguarAngle(double speed) {
        if (highlm.get() && speed > 0) {
            shootingAngleJaguar.set(0);
        } else if (lowlm.get() && speed < 0) {
            shootingAngleJaguar.set(0);
        } else {
            shootingAngleJaguar.set(speed);
        }
    }

    public void endjaguarAngle() {
        shootingAngleJaguar.set(0);
    }

    public void updateDashboard() {
        SmartDashboard.putBoolean("angleShooter lowlm", lowlm.get());
        SmartDashboard.putBoolean("angleShooter highlm", highlm.get());
        SmartDashboard.putNumber("angleShooter jag", shootingAngleJaguar.get());
        SmartDashboard.putNumber("angleShooter pidGet", ac.pidGet());
        SmartDashboard.putBoolean("angleShooter isPIDOn", scPID.isEnable());
    }

    public boolean lmtest() {
        if (highlm.get() || lowlm.get()) {
            return true;
        } else {
            return false;
        }
    }
}
