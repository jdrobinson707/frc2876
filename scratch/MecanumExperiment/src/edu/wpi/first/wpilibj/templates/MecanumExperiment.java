/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class MecanumExperiment extends SimpleRobot {

    RobotDrive drive;
    Joystick leftStick, rightStick;
    // These values need to be adjusted to match the actual wiring.
    public final static int MOTOR_FRONT_L = 1;
    public final static int MOTOR_REAR_L = 2;
    public final static int MOTOR_FRONT_R = 3;
    public final static int MOTOR_REAR_R = 4;

    public MecanumExperiment() {
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        drive = new RobotDrive(MOTOR_FRONT_L, MOTOR_REAR_L,
                MOTOR_FRONT_R, MOTOR_REAR_R);
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
            double magnitude = leftStick.getMagnitude();
            double direction = leftStick.getDirectionDegrees();
            double rotation = rightStick.getX();
            drive.mecanumDrive_Polar(magnitude, direction, rotation);
        }
    }
}
