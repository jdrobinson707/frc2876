/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author student
 */
public class PIDController extends Subsystem {
    SpeedController motor = RobotMap.DRIVETRAIN_LEFTDRIVE_JAGUAR;
    DigitalChannel gyro = RobotMap.DRIVETRAIN_GYRO;
    
    public PIDController() {

    }
    
    public void initDefaultCommand() {
        
    }
    
    protected double returnPIDInput() {
        return pot.getAverageVoltage(); 
    }
    
    protected void usePIDOutput(double output) {
        
    }
}
