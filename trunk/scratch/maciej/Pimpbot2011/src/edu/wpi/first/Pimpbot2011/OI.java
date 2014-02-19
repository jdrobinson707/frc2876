package edu.wpi.first.Pimpbot2011;

import edu.wpi.first.Pimpbot2011.commands.ArmExtend;
import edu.wpi.first.Pimpbot2011.commands.ArmRetract;
import edu.wpi.first.Pimpbot2011.commands.CameraAction;
import edu.wpi.first.Pimpbot2011.commands.ClawClose;
import edu.wpi.first.Pimpbot2011.commands.ClawOpen;
import edu.wpi.first.Pimpbot2011.commands.SetShoulderSetpoint;
import edu.wpi.first.Pimpbot2011.commands.TurnRobot;
import edu.wpi.first.Pimpbot2011.subsystems.Shoulder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI {
    // Process operator interface input here.

    Joystick stickr = new Joystick(1);
    Joystick stickl = new Joystick(2);
    Joystick armJoy = new Joystick(3);
    Button armbl1 = new JoystickButton(armJoy, 1),
            armbl2 = new JoystickButton(armJoy, 2),
            armbl3 = new JoystickButton(armJoy, 3),
            armbl4 = new JoystickButton(armJoy, 4),
            armbl5 = new JoystickButton(armJoy, 5),
            armbl6 = new JoystickButton(armJoy, 6);
    Button br1 = new JoystickButton(stickr, 1),
            br2 = new JoystickButton(stickr, 2),
            br3 = new JoystickButton(stickr, 3),
            br5 = new JoystickButton(stickr, 5),
            br8 = new JoystickButton(stickr, 8),
            br9 = new JoystickButton(stickr, 9);
    Button bl1 = new JoystickButton(stickl, 1),
            bl2 = new JoystickButton(stickl, 2),
            bl3 = new JoystickButton(stickl, 3),
            bl5 = new JoystickButton(stickl, 5),
            bl8 = new JoystickButton(stickl, 8),
            bl9 = new JoystickButton(stickl, 9);


    public OI() {
        armbl1.whenPressed(new ClawOpen());
        armbl2.whenPressed(new ClawClose());
        armbl3.whenPressed(new ArmExtend());
        armbl4.whenPressed(new ArmRetract());
        armbl5.whenPressed(new SetShoulderSetpoint(Shoulder.HIGH));
        armbl6.whenPressed(new SetShoulderSetpoint(Shoulder.LOW));

        // br1.whenPressed(new DriveToDistance(100));
        br2.whenPressed(new CameraAction(1));
        br3.whenPressed(new CameraAction(2));
        br5.whenPressed(new CameraAction(3));
        bl2.whenPressed(new CameraAction(1));
        bl3.whenPressed(new CameraAction(2));
        bl5.whenPressed(new CameraAction(3));

        br8.whenPressed(new TurnRobot(-45));
        br9.whenPressed(new TurnRobot(45));
    }

    public double getLeftSpeed() {
        return stickl.getY();
    }

    public double getRightSpeed() {
        return stickr.getY();
    }

    public double getArmSpeed() {
        return armJoy.getY();
    }
}
