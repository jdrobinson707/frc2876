package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.templates.commands.AdjustTurn;
import edu.wpi.first.wpilibj.templates.commands.CollectBall;
import edu.wpi.first.wpilibj.templates.commands.ConveyorHighIdle;
import edu.wpi.first.wpilibj.templates.commands.ConveyorHighOn;
import edu.wpi.first.wpilibj.templates.commands.ConveyorHighReverse;
import edu.wpi.first.wpilibj.templates.commands.ConveyorLowIdle;
import edu.wpi.first.wpilibj.templates.commands.ConveyorLowOn;
import edu.wpi.first.wpilibj.templates.commands.ConveyorLowReverse;
import edu.wpi.first.wpilibj.templates.commands.ShootOneBall;
import edu.wpi.first.wpilibj.templates.commands.ShooterShoot;
import edu.wpi.first.wpilibj.templates.commands.ShooterUpdate;
import edu.wpi.first.wpilibj.templates.commands.VisionFiltering;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    // Process operator interface input here.

    Joystick armstick;
    Joystick leftstick;
    Joystick rightstick;
    JoystickButton armb1;
    JoystickButton armb2;
    JoystickButton armb3;
    JoystickButton armb4;
    JoystickButton armb5;
    JoystickButton armb6;
    JoystickButton armb7;
    JoystickButton armb8;
    JoystickButton armb9;
    JoystickButton armb10;
    JoystickButton armb11;
    JoystickButton rightb8, rightb9;

    public OI() {
        armstick = new Joystick(RobotMap.JOYSTICK_EXTRA);
        leftstick = new Joystick(RobotMap.JOYSTICK_LEFT);
        rightstick = new Joystick(RobotMap.JOYSTICK_RIGHT);
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
        rightb9 = new JoystickButton(rightstick, 9);
        rightb8 = new JoystickButton(rightstick, 8);

        armb2.whenPressed(new ConveyorLowOn());
        armb3.whenPressed(new ConveyorLowIdle());
        armb4.whenPressed(new ConveyorHighOn());
        armb5.whenPressed(new ConveyorHighIdle());
        armb6.whenPressed(new ConveyorLowReverse());
        armb7.whenPressed(new ConveyorHighReverse());

        armb1.whileHeld(new ShooterShoot(1.0));
        armb1.whenReleased(new ShooterShoot(0.0));
        //armb6.whileHeld(new ShooterShoot(0.57));
        //armb6.whenReleased(new ShooterShoot(0.0));
        //armb7.whileHeld(new ShooterShoot(0.58));
        //armb7.whenReleased(new ShooterShoot(0.0));
        //armb8.whileHeld(new ShooterShoot(0.59));
        //armb8.whenReleased(new ShooterShoot(0.0));
        //armb10.whileHeld(new ShooterShoot(0.60));
        //armb10.whenReleased(new ShooterShoot(0.0));
        //armb11.whileHeld(new ShooterShoot(0.61));
        //armb11.whenReleased(new ShooterShoot(0.0));

        //armb8.whenPressed(new TurnRobot(-45));
        //armb9.whenPressed(new TurnRobot(45));

        armb8.whenPressed(new CollectBall());
        armb9.whenPressed(new ShootOneBall());
        armb10.whenPressed(new AdjustTurn());
        armb11.whenPressed(new VisionFiltering());

        rightb8.whenPressed(new ShooterUpdate(-0.1));
        rightb9.whenPressed(new ShooterUpdate(0.1));
    }

    public Joystick getLeftStick() {
        return leftstick;
    }

    public Joystick getRightStick() {
        return rightstick;
    }

    public double getArmStick() {
        return armstick.getY();
    }
}
