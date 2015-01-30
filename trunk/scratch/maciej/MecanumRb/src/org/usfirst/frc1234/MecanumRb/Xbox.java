//http://www.chiefdelphi.com/forums/showthread.php?t=110777
package org.usfirst.frc1234.MecanumRb;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author mentor
 */
public class Xbox extends Joystick {

	public static final int LEFT_Y = 0,
			LEFT_X = 1,
			TRIGGERS = 3,
			RIGHT_X = 4,
			RIGHT_Y = 5,
			DPAD_X = 6,
			DPAD_Y = 7; // Is this right?

    // Button mappings:
    public static final int A = 1,
            B = 2,
            X = 3,
            Y = 4,
            BUMPER_L = 5,
            BUMPER_R = 6,
            BACK = 7,
            START = 8,
            LEFT_STICK = 9,
            RIGHT_STICK = 10;


    //public Joystick m_pad;

    public Xbox(int port) {
    	super(port);
    }

    /**
     * Read the value of the right joystick's X axis.
     * @return the value of the right joystick's X axis.
     */
    public double getRightStickX() {
        return getRawAxis(RIGHT_X);
    }

    /**
     * Read the value of the right joystick's Y axis.
     * @return the value of the right joystick's Y axis.
     */
    public double getRightStickY() {
        return getRawAxis(RIGHT_Y);
    }

    /**
     * Read the value of the left joystick's X axis.
     * @return the value of the left joystick's X axis.
     */
    public double getLeftStickX() {
        return getRawAxis(LEFT_X);
    }

    /**
     * Read the value of the left joystick's Y axis.
     * @return the value of the left joystick's Y axis.
     */
    public double getLeftStickY() {
        return getRawAxis(LEFT_Y);
    }
    
    /**
     * Read the value of the d-pad's X axis.
     * @return the value of the d-pad's X axis.
     */
    public double getDPadX() {
        return getRawAxis(DPAD_X);
    }
    
    /**
     * Read the value of the d-pad's Y axis.
     * @return the value of the d-pad's Y axis.
     */
    public double getDPadY() {
        return getRawAxis(DPAD_Y);
    }

    /**
     * Read the state of the A button.
     * @return the state of the A button.
     */
    public boolean getAButton() {
        return getRawButton(A);
    }

    /**
     * Read the state of the B button.
     * @return the state of the B button.
     */
    public boolean getBButton() {
        return getRawButton(B);
    }

    /**
     * Read the state of the X button.
     * @return the state of the X button.
     */
    public boolean getXButton() {
        return getRawButton(X);
    }

    /**
     * Read the state of the Y button.
     * @return the state of the Y button.
     */
    public boolean getYButton() {
        return getRawButton(Y);
    }

    /**
     * Read the state of the back button.
     * @return the state of the back button.
     */
    public boolean getBackButton() {
        return getRawButton(BACK);
    }

    /**
     * Read the state of the start button.
     * @return the state of the start button.
     */
    public boolean getStartButton() {
        return getRawButton(START);
    }

    /**
     * Read the state of the right bumper button.
     * @return the state of the right bumper button.
     */
    public boolean getRightBumperButton() {
        return getRawButton(BUMPER_R);
    }

    /**
     * Read the state of the left bumper button.
     * @return the state of the left bumper button.
     */
    public boolean getLeftBumperButton() {
        return getRawButton(BUMPER_L);
    }

    /**
     * Read the state of the left stick button.
     * @return the state of the left stick button.
     */
    public boolean getLeftStickButton() {
        return getRawButton(LEFT_STICK);
    }

    /**
     * Read the state of the right stick button.
     * @return the state of the right stick button.
     */
    public boolean getRightStickButton() {
        return getRawButton(RIGHT_STICK);
    } 
        
    /**
     * Read the state of the right trigger.
     * @return the state of the right trigger.
     */
    public double getRightTrigger() {
        return -Math.min(getRawAxis(TRIGGERS), 0);
    }

    /**
     * Read the state of the left trigger.
     * @return the state of the left trigger.
     */
    public double getLeftTrigger() {
        return Math.max(getRawAxis(TRIGGERS), 0);
    }

    

//    public double getLeftX() {
//        return m_pad.getRawAxis(LEFT_X_AXIS);
//    }
//
//    public double getLeftY() {
//        return m_pad.getRawAxis(LEFT_Y_AXIS);
//    }
//
//    public double getRightX() {
//        return m_pad.getRawAxis(RIGHT_X_AXIS);
//    }
//
//    public double getRightY() {
//        return m_pad.getRawAxis(RIGHT_Y_AXIS);
//    }
//
//    public double getTriggers() {
//        return m_pad.getRawAxis(TRIGGERS);
//    }
//
//    public double getDpadX() {
//        return m_pad.getRawAxis(DPAD_LR);
//    }
//
//    public double applyDeadband(int axis) {
//        if (Math.abs(m_pad.getRawAxis(axis)) < .1) {
//            return 0;
//        } else {
//            return axis;
//        }
//    }

    // Creates buttons
//    public Button X;
//    public Button Y;
//    public Button A;
//    public Button B;
//    public Button lBumper;
//    public Button rBumper;
//    public Button start;
//    public Button back;
//    public Button lStick;
//    public Button rStick;
//
//    public boolean getButton(int btn) {
//        return m_pad.getRawButton(btn);
//    }
//
//    public Button getLBump() {
//        return lBumper;
//    }
    // Axis indexes:
}
