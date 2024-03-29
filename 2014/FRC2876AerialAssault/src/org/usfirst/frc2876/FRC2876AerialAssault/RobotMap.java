// RobotBuilder Version: 1.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.
package org.usfirst.frc2876.FRC2876AerialAssault;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static Gyro driveTrainGyro;
    public static Encoder driveTrainLeftEncoder;
    public static Encoder driveTrainRightEncoder;
    public static SpeedController driveTrainLeftSpeedController;
    public static SpeedController driveTrainRightSpeedController;
    public static RobotDrive driveTrainRobotDrive21;
    public static SpeedController armArmBaseController;
    public static SpeedController armArmGrabberController;
    public static DigitalInput armTopLimitSwitch;
    public static DigitalInput armBottomLimitSwitch;
    public static DigitalInput armBallLimitSwitch;
    public static AnalogChannel armArmPot;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public static AnalogSonar leftSonar;
    public static AnalogSonar rightSonar;
    public static AnalogSonar frontSonar;

    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        driveTrainGyro = new Gyro(1, 1);
        LiveWindow.addSensor("DriveTrain", "Gyro", driveTrainGyro);
        driveTrainGyro.setSensitivity(0.007);
        driveTrainLeftEncoder = new Encoder(1, 12, 1, 11, false, EncodingType.k4X);
        LiveWindow.addSensor("DriveTrain", "Left Encoder", driveTrainLeftEncoder);
        driveTrainLeftEncoder.setDistancePerPulse(0.036464914729);
        driveTrainLeftEncoder.setPIDSourceParameter(PIDSourceParameter.kRate);
        driveTrainLeftEncoder.start();
        driveTrainRightEncoder = new Encoder(1, 14, 1, 13, false, EncodingType.k4X);
        LiveWindow.addSensor("DriveTrain", "Right Encoder", driveTrainRightEncoder);
        driveTrainRightEncoder.setDistancePerPulse(0.020943951);
        driveTrainRightEncoder.setPIDSourceParameter(PIDSourceParameter.kRate);
        driveTrainRightEncoder.start();
        driveTrainLeftSpeedController = new Talon(1, 2);
        LiveWindow.addActuator("DriveTrain", "LeftSpeedController", (Talon) driveTrainLeftSpeedController);

        driveTrainRightSpeedController = new Talon(1, 1);
        LiveWindow.addActuator("DriveTrain", "RightSpeedController", (Talon) driveTrainRightSpeedController);

        driveTrainRobotDrive21 = new RobotDrive(driveTrainLeftSpeedController, driveTrainRightSpeedController);

        driveTrainRobotDrive21.setSafetyEnabled(false);
        driveTrainRobotDrive21.setExpiration(0.1);
        driveTrainRobotDrive21.setSensitivity(0.5);
        driveTrainRobotDrive21.setMaxOutput(1.0);
        
        armArmBaseController = new Talon(1, 5);
        LiveWindow.addActuator("Arm", "ArmBaseController", (Talon) armArmBaseController);

        armArmGrabberController = new Talon(1, 4);
        LiveWindow.addActuator("Arm", "ArmGrabberController", (Talon) armArmGrabberController);

        armTopLimitSwitch = new DigitalInput(1, 7);
        LiveWindow.addSensor("Arm", "TopLimitSwitch", armTopLimitSwitch);

        armBottomLimitSwitch = new DigitalInput(1, 9);
        LiveWindow.addSensor("Arm", "BottomLimitSwitch", armBottomLimitSwitch);

        armBallLimitSwitch = new DigitalInput(1, 8);
        LiveWindow.addSensor("Arm", "BallLimitSwitch", armBallLimitSwitch);

        armArmPot = new AnalogChannel(1, 2);
        LiveWindow.addSensor("Arm", "ArmPot", armArmPot);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        leftSonar = new AnalogSonar(1, 3);
        LiveWindow.addSensor("DriveTrain", "leftSonar", leftSonar);
        rightSonar = new AnalogSonar(1, 4);
        LiveWindow.addSensor("DriveTrain", "rightSonar", rightSonar);
        frontSonar = new AnalogSonar(1, 5);
        LiveWindow.addSensor("DriveTrain", "frontSonar", frontSonar);

    }
}
