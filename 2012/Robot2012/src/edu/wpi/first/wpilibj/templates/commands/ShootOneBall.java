/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author User
 */
public class ShootOneBall extends CommandBase {

    boolean wasPressed = false;
    boolean whenReleased = false;
    double rps = 0.0;

    public ShootOneBall(double r) {
        // Use requires() here to declare subsystem dependencies
        requires(conveyorlow);
        requires(conveyorhigh);
        requires(shooter);
        wasPressed = false;
        whenReleased = false;
        rps = r;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        conveyorlow.on();
        //conveyorhigh.on
        //shooter.set(.61);
        wasPressed = false;
        whenReleased = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

        double x = RobotMap.START_SPEED_RPS;
        boolean ready = false;
        if (shooter.rps() <= rps) {
            shooter.set(x);
            if (x <= 1) {
                x += .08;
            }
            if (shooter.rps() <= rps - 1.3)
            {
                conveyorhigh.on();
            }
            SmartDashboard.putDouble("x", x);
        } else {
            ready = true;
        }

        if (conveyorhigh.hasBallEntered() == true) {
            conveyorlow.idle();
        }

        boolean b = shooter.hasBallEntered();
        if (b == true) {
            wasPressed = true;
        }
        if (wasPressed == true && !shooter.hasBallEntered()) {
            if (ready) {
                whenReleased = true;
                Timer.delay(.5);
                end();
            }
        }
        SmartDashboard.putBoolean("shooterPressed: ", wasPressed);
        SmartDashboard.putBoolean("shooterReleased: ", whenReleased);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return whenReleased;
    }



    // Called once after isFinished returns true
    protected void end() {
        conveyorlow.idle();
        conveyorhigh.idle();
        shooter.set(0);

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
