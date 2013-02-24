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
    final double LOW_LIMIT = 1.032;
    final double HIGH_LIMIT = .636;
    PIDController scPID;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        //setDefaultCommand(new AdjustShooterAngle(ac.pidGet()));
        // setDefaultCommand(new AngleShooterIdle());
        //setDefaultCommand(new AdjustShooterAngle(0.934));
        setDefaultCommand(new AngleShooterJoystick());
    }

    public void potInit() {
        scPID = new PIDController(.01, 0, 0, ac, new PIDOutput() {
            public void pidWrite(double output) {
                shootingAngleJaguar.set(-output);
            }
        });
        scPID.setPercentTolerance(5.0);
        scPID.setOutputRange(-.5, .5);
        //scPID.setInputRange(HIGH_LIMIT, LOW_LIMIT);
        //scPID.enable();
        LiveWindow.addActuator("AngleShooter", "angle PID", scPID);

    }

    public void setPotSetpoint(double v) {
        scPID.setSetpoint(v);
    }

    public boolean isFinishedAdjustingShooter() {
        SmartDashboard.putNumber("angle shooter PID Error", scPID.getError());
        SmartDashboard.putBoolean("is angle shooter PID ontarget", scPID.onTarget());
        //eturn scPID.onTarget();
        return (Math.abs(scPID.getError()) < 2);
    }

    public void endShooterPID() {
        //scPID.reset();
        shootingAngleJaguar.set(0);
    }

    public void jaguarAngle(double speed) {
        if(highlm.get() && speed > 0){
            shootingAngleJaguar.set(0);
        }
        if(lowlm.get() && speed < 0){
            shootingAngleJaguar.set(0);
        }else{
            shootingAngleJaguar.set(speed);
        }
    }

    public void endjaguarAngle() {
        shootingAngleJaguar.set(0);
    }
    
    public void updateDashboard() {
        SmartDashboard.putBoolean("lowlm", lowlm.get());
        SmartDashboard.putBoolean("highlm", highlm.get());
        SmartDashboard.putNumber("angleJag", shootingAngleJaguar.get());
        SmartDashboard.putNumber("anglePot", ac.getVoltage());
    }

    public boolean lmtest() {
        if (highlm.get() || lowlm.get()) {
            return true;
        } else {
            return false;
        }
    }
}
