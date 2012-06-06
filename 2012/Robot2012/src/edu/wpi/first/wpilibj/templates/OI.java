package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.commands.BridgeArmLower;
import edu.wpi.first.wpilibj.templates.commands.BridgeArmRaise;
import edu.wpi.first.wpilibj.templates.commands.CollectorIntakeBall;
import edu.wpi.first.wpilibj.templates.commands.CollectorMoveBallUp;
import edu.wpi.first.wpilibj.templates.commands.ConveyorHighOn;
import edu.wpi.first.wpilibj.templates.commands.ConveyorHighReverse;
import edu.wpi.first.wpilibj.templates.commands.ConveyorLowOn;
import edu.wpi.first.wpilibj.templates.commands.ConveyorLowReverse;
import edu.wpi.first.wpilibj.templates.commands.Drive;
import edu.wpi.first.wpilibj.templates.commands.ShooterFire;
import edu.wpi.first.wpilibj.templates.commands.ShooterIdle;
import edu.wpi.first.wpilibj.templates.commands.ShooterLoad;
import edu.wpi.first.wpilibj.templates.commands.ShooterSet;
import edu.wpi.first.wpilibj.templates.commands.ShooterStart;
import edu.wpi.first.wpilibj.templates.commands.VisionAim;
import edu.wpi.first.wpilibj.templates.commands.VisionFilter;
import edu.wpi.first.wpilibj.templates.commands.VisionIdle;
import edu.wpi.first.wpilibj.templates.commands.VisionTurn;
import edu.wpi.first.wpilibj.templates.commands.cgAutonomous;
import edu.wpi.first.wpilibj.templates.commands.cgLoadCollector;
import edu.wpi.first.wpilibj.templates.commands.cgShootOneBall;
import edu.wpi.first.wpilibj.templates.commands.cgShootTwoBall;
import edu.wpi.first.wpilibj.templates.commands.cgTurnShootOneBall;
import edu.wpi.first.wpilibj.templates.commands.cgTurnShootTwoBall;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    // Process operator interface input here.

    Joystick armstick, lstick, rstick;
    JoystickButton armb1, armb2, armb3, armb4, armb5,
            armb6, armb7, armb8, armb9, armb10, armb11;
    JoystickButton rb1, rb2, rb3, rb4, rb5,
            rb6, rb7, rb8, rb9, rb10, rb11;
    JoystickButton lb1, lb2, lb3, lb4, lb5,
            lb6, lb7, lb8, lb9, lb10, lb11;
    boolean isArmLocked = false;
    InternalButton ibCollectorIntakeBall, ibCollectorMoveBallUp;
    InternalButton ibShooterStart, ibShooterIdle, ibShooterFire,
            ibShooterLoad, ibShooterSet;
    InternalButton ibVisionFilter, ibVisionTurn;
    InternalButton ibArmLower, ibArmRaise;
    InternalButton ibcgAutonomous, ibcgLoadCollector, ibcgShootOneBall,
            ibcgShootTwoBall, ibcgTurnShootOne, ibcgTurnShootTwo;

    public OI() {
        armstick = new Joystick(RobotMap.JOYSTICK_EXTRA);
        lstick = new Joystick(RobotMap.JOYSTICK_LEFT);
        rstick = new Joystick(RobotMap.JOYSTICK_RIGHT);

        armb1 = new JoystickButton(armstick, 1);
        armb2 = new JoystickButton(armstick, 2);
        armb3 = new JoystickButton(armstick, 3);
        armb4 = new JoystickButton(armstick, 4);
        armb5 = new JoystickButton(armstick, 5);
        armb6 = new JoystickButton(armstick, 6);
        armb7 = new JoystickButton(armstick, 7);
        armb8 = new JoystickButton(armstick, 8);
        armb9 = new JoystickButton(armstick, 9);
        armb10 = new JoystickButton(armstick, 10);
        armb11 = new JoystickButton(armstick, 11);

        lb1 = new JoystickButton(lstick, 1);
        lb2 = new JoystickButton(lstick, 2);
        lb3 = new JoystickButton(lstick, 3);
        lb4 = new JoystickButton(lstick, 4);
        lb5 = new JoystickButton(lstick, 5);
        lb6 = new JoystickButton(lstick, 6);
        lb7 = new JoystickButton(lstick, 7);
        lb8 = new JoystickButton(lstick, 8);
        lb9 = new JoystickButton(lstick, 9);
        lb10 = new JoystickButton(lstick, 10);
        lb11 = new JoystickButton(lstick, 11);

        rb1 = new JoystickButton(rstick, 1);
        rb2 = new JoystickButton(rstick, 2);
        rb3 = new JoystickButton(rstick, 3);
        rb4 = new JoystickButton(rstick, 4);
        rb5 = new JoystickButton(rstick, 5);
        rb6 = new JoystickButton(rstick, 6);
        rb7 = new JoystickButton(rstick, 7);
        rb8 = new JoystickButton(rstick, 8);
        rb9 = new JoystickButton(rstick, 9);
        rb10 = new JoystickButton(rstick, 10);
        rb11 = new JoystickButton(rstick, 11);

        lb3.whileHeld(new Drive(RobotMap.DRIVE_REVERSE));

        //armb1.whenPressed(new ShooterRPS(17));
        armb1.whileHeld(new ConveyorHighReverse());

        armb3.whenPressed(new cgLoadCollector());
        armb2.whenPressed(new VisionTurn());
        //armb4.whenPressed(new cgShootOneBall(.61));
        armb4.whenPressed(new cgShootOneBall(17));
        //armb5.whenPressed(new cgShootOneBall(19));
        armb5.whenPressed(new VisionFilter());
        armb6.whenPressed(new VisionAim());
        armb7.whenPressed(new VisionIdle());
        armb8.whenPressed(new VisionIdle());
        armb9.whenPressed(new VisionIdle());
        armb10.whenPressed(new VisionIdle());
        armb11.whenPressed(new VisionIdle());

//        armb9.whenPressed(new ShooterIdle());
//        armb10.whenPressed(new ShooterIdle());
//        armb11.whenPressed(new ShooterIdle());


        lb6.whileHeld(new ConveyorLowOn());
        lb7.whileHeld(new ConveyorLowReverse());
        lb10.whileHeld(new ConveyorHighOn());
        lb11.whileHeld(new ConveyorHighReverse());
        
        //        armb6.whenPressed(new ConveyorLowReverse());
//        armb7.whenPressed(new ConveyorLowIdle());
//        armb8.whenPressed(new ConveyorLowOn());
//        armb11.whenPressed(new ConveyorHighReverse());
//        armb10.whenPressed(new ConveyorHighIdle());
//        armb9.whenPressed(new ConveyorHighOn());

        //armb1.whenPressed(new cgTurnShootOneBall());


        //rb10.whenPressed(new BridgeArmLower());
        //rb11.whenPressed(new BridgeArmRaise());

        //lb7.whenPressed(new cgShootOneBall());
        //lb6.whenPressed(new cgShootTwoBall());

//        lb11.whenPressed(new VisionTurn());
//        lb10.whenPressed(new VisionFilter());
//        lb9.whenPressed(new VisionIdle());
        // initInternalButtons();

    }

    private void initInternalButtons() {
        ibCollectorIntakeBall = new InternalButton();
        ibCollectorIntakeBall.whenPressed(new CollectorIntakeBall());
        SmartDashboard.putData("CollectorIntakeBall", ibCollectorIntakeBall);

        ibCollectorMoveBallUp = new InternalButton();
        ibCollectorMoveBallUp.whenPressed(new CollectorMoveBallUp());
        SmartDashboard.putData("CollectorMoveBallUp", ibCollectorMoveBallUp);

        ibShooterStart = new InternalButton();
        ibShooterStart.whenPressed(new ShooterStart());
        SmartDashboard.putData("ShooterStart", ibShooterStart);

        ibShooterIdle = new InternalButton();
        ibShooterIdle.whenPressed(new ShooterIdle());
        SmartDashboard.putData("ShooterIdle", ibShooterIdle);

        ibShooterFire = new InternalButton();
        ibShooterFire.whenPressed(new ShooterFire(5));
        SmartDashboard.putData("ShooterFire", ibShooterFire);

        ibShooterLoad = new InternalButton();
        ibShooterLoad.whenPressed(new ShooterLoad());
        SmartDashboard.putData("ShooterLoad", ibShooterLoad);

        ibShooterSet = new InternalButton();
        ibShooterSet.whenPressed(new ShooterSet(1));
        SmartDashboard.putData("ShooterSet", ibShooterSet);

        ibArmRaise = new InternalButton();
        ibArmRaise.whenPressed(new BridgeArmRaise());
        SmartDashboard.putData("ArmRaise", ibArmRaise);

        ibArmLower = new InternalButton();
        ibArmLower.whenPressed(new BridgeArmLower());
        SmartDashboard.putData("ArmLower", ibArmLower);

        ibVisionFilter = new InternalButton();
        ibVisionFilter.whenPressed(new VisionFilter());
        SmartDashboard.putData("VisionFilter", ibVisionFilter);

        ibVisionTurn = new InternalButton();
        ibVisionTurn.whenPressed(new VisionTurn());
        SmartDashboard.putData("VisionTurn", ibVisionTurn);

        ibcgAutonomous = new InternalButton();
        ibcgAutonomous.whenPressed(new cgAutonomous());
        SmartDashboard.putData("Autonomous", ibcgAutonomous);

        ibcgLoadCollector = new InternalButton();
        ibcgLoadCollector.whenPressed(new cgLoadCollector());
        SmartDashboard.putData("LoadCollector", ibcgLoadCollector);

        ibcgShootOneBall = new InternalButton();
        ibcgShootOneBall.whenPressed(new cgShootOneBall(17.2));
        SmartDashboard.putData("ShootOneBall", ibcgShootOneBall);

        ibcgShootTwoBall = new InternalButton();
        ibcgShootTwoBall.whenPressed(new cgShootTwoBall(17));
        SmartDashboard.putData("ShootTwoBall", ibcgShootTwoBall);

        ibcgTurnShootOne = new InternalButton();
        ibcgTurnShootOne.whenPressed(new cgTurnShootOneBall());
        SmartDashboard.putData("TurnShootOne", ibcgTurnShootOne);

        ibcgTurnShootTwo = new InternalButton();
        ibcgTurnShootTwo.whenPressed(new cgTurnShootTwoBall());
        SmartDashboard.putData("TurnShootTwo", ibcgTurnShootTwo);
    }

    public Joystick getLeftStick() {
        return lstick;
    }

    public Joystick getRightStick() {
        return rstick;
    }

    public double getArmStick() {
        return armstick.getY();
    }

    public double getArmZ() {
        return armstick.getZ();
    }

    public boolean isDebugOn() {
        return rstick.getZ() > 0;
    }

    public double getDriveMode() {
        if (lstick.getZ() < 0) {
            return RobotMap.DRIVE_REVERSE;
        }
        return RobotMap.DRIVE_FORWARD;
    }
}
