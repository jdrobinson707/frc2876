//http://www.chiefdelphi.com/forums/showthread.php?t=110777
package org.usfirst.frc2876.RecycleRush2876;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author mentor
 */
public class XboxController {

    public Joystick m_pad;

    public XboxController(int port) {
        m_pad = new Joystick(port);
        X = new JoystickButton(m_pad, BUTTON_X);
        Y = new JoystickButton(m_pad, BUTTON_Y);
        A = new JoystickButton(m_pad, BUTTON_A);
        B = new JoystickButton(m_pad, BUTTON_B);
        lBumper = new JoystickButton(m_pad, BUMPER_L);
        rBumper = new JoystickButton(m_pad, BUMPER_R);
        start = new JoystickButton(m_pad, BUTTON_START);
        back = new JoystickButton(m_pad, BUTTON_BACK);
        lStick = new JoystickButton(m_pad, LEFT_STICK_PRESS);
        rStick = new JoystickButton(m_pad, RIGHT_STICK_PRESS);
        lTrigger = new JoystickButton(m_pad, LEFT_TRIGGER);
        rTrigger = new JoystickButton(m_pad, RIGHT_TRIGGER);
    }
  

    public double getLeftX() {
        return m_pad.getRawAxis(LEFT_X_AXIS);
    }

    public double getLeftY() {
        return m_pad.getRawAxis(LEFT_Y_AXIS);
    }

    public double getRightX() {
        return m_pad.getRawAxis(RIGHT_X_AXIS);
    }

    public double getRightY() {
        return m_pad.getRawAxis(RIGHT_Y_AXIS);
    }

    public double getLeftTrigger() {
        return m_pad.getRawAxis(LEFT_TRIGGER);
    }
    
    public double getRightTrigger() {
        return m_pad.getRawAxis(RIGHT_TRIGGER);
    }

    public double getDpadX() {
        return m_pad.getRawAxis(DPAD_LR);
    }

    public double applyDeadband(int axis) {
        if (Math.abs(m_pad.getRawAxis(axis)) < .1) {
            return 0;
        } else {
            return axis;
        }
    }

    // Creates buttons
    public Button X;
    public Button Y;
    public Button A;
    public Button B;
    public Button lBumper;
    public Button rBumper;
    public Button start;
    public Button back;
    public Button lStick;
    public Button rStick;
    public Button lTrigger;
    public Button rTrigger;

    public boolean getButton(int btn) {
        return m_pad.getRawButton(btn);
    }

    public Button getLBump() {
        return lBumper;
    }
    // Axis indexes:
    public static final int LEFT_X_AXIS = 1,
            LEFT_Y_AXIS = 0,
            LEFT_TRIGGER = 2,
            RIGHT_TRIGGER = 3,
            RIGHT_X_AXIS = 4,
            RIGHT_Y_AXIS = 5,
            DPAD_LR = 6;

    // Button mappings:
    public static final int BUTTON_A = 1,
            BUTTON_B = 2,
            BUTTON_X = 3,
            BUTTON_Y = 4,
            BUMPER_L = 5,
            BUMPER_R = 6,
            BUTTON_BACK = 7,
            BUTTON_START = 8,
            LEFT_STICK_PRESS = 9,
            RIGHT_STICK_PRESS = 10;

}
