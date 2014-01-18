
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
     Joystick leftStick; 
     Joystick rightStick;

     XboxController xbox;
     
     int driveState;
     
     public OI() {
        leftStick = new Joystick(RobotMap.JOYSTICK_LEFT);
        rightStick = new Joystick(RobotMap.JOYSTICK_RIGHT);
        xbox = new XboxController(1);
        driveState = 0;
        //xbox.back.whenPressed(new Drive(0));
    }
     
     public boolean isStartPressed() {
         return xbox.getButton(8); //start
     }
     
     public boolean isBackPressed() {
         return xbox.getButton(7); //back
     }
     
     public int getDriveState() {
         if (isStartPressed())
            driveState = 0;
         else if (isBackPressed())
            driveState = 1;
         
        return driveState;
     }
     
     
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // Another type of button you can create is a DigitalIOButton, which is
    // a button or switch hooked up to the cypress module. These are useful if
    // you want to build a customized operator interface.
    // Button button = new DigitalIOButton(1);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
     public Joystick getRightStick() {
        return rightStick;
    }

    public Joystick getLeftStick() {
        return leftStick;
    }
    
    public XboxController getXbox(){
        return xbox;
    }
    
    public double getXboxLeftY() {
        return xbox.getLeftY();
    }
    
    public double getXboxLeftX() {
        return xbox.getLeftX();
    }
    
    public double getXboxRightY() {
        return xbox.getRightY();
    }
    
    public double getXboxRightX() {
        return xbox.getRightX();
    }
}



