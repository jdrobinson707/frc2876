/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author User
 */
public class Arm {

    DriverStation ds;
    AnalogChannel ac;
    PIDController mc;
    Jaguar armJag;
    Solenoid grip;
    Solenoid armExtension1;
    Solenoid armExtension2;
    double armSetpoint, lastArmSetpoint;
    boolean movingArm;
    final double HIGH_LIMIT = 671;
    final double LOW_LIMIT = 130;
    final double ARM_EXTEND_LIMIT = 168;
    // if grip is false claw is open
    // if grip is true claw is closed
    final boolean CLAW_OPEN = false;
    final boolean CLAW_CLOSE = true;
    public boolean ARM_EXTEND = true;
    public boolean ARM_RETRACT = false;

    public String toString() {
        return ""
                + "enabled: " + mc.isEnable()
                + " set:" + mc.getSetpoint()
                + " error:" + mc.getError()
                + " get: " + mc.get()
                + " On target: " + mc.onTarget()
                + " pidGet: " + ac.pidGet()
                + " Speed:" + ((armJag.getSpeed() * 10.0) / 10)
                + " Claw open: " + this.isClawOpen()
                + " Extended: " + this.isArmExtended();
    }

    public Arm() {
        //super("ARM");
        //this.setPriority(this.NORM_PRIORITY / 2);
        //System.out.println("ARM priority: " + this.getPriority());

        ds = ds.getInstance();
        armJag = new Jaguar(Constants.ARM_JAGUAR);
        ac = new AnalogChannel(Constants.ANALOG_CHANNEL_SLOT, Constants.ANALOG_CHANNEL_CHANNEL);
        mc = new PIDController(.25, 0.001, 0, ac, armJag);
        mc.setTolerance(5.0);
        mc.setOutputRange(-.7, .7);
        mc.setInputRange(LOW_LIMIT, HIGH_LIMIT);

        armExtension1 = new Solenoid(8, 1);
        armExtension2 = new Solenoid(8, 2);
        this.RetractArm();

        grip = new Solenoid(8, 3);
        this.OpenClaw();
        movingArm = false;
        //this.start();
    }

    public void run() {
        System.out.println("starting arm thread");
        mc.reset();
        while (true) {
            Timer.delay(.5);

            if (this.lastArmSetpoint != this.armSetpoint) {
                mc.reset();
                this.lastArmSetpoint = this.armSetpoint;
                this.movingArm = true;
                mc.setSetpoint(this.armSetpoint);
                mc.enable();
                // start pid
            } else {
                if (this.movingArm == true && mc.onTarget()) {
                    mc.reset();
                    System.out.println("reached target");
                }
            }
            if (this.movingArm == false) {
                // mc.reset();
                //System.out.println("stop pid");
            }
            //this.yield();
        }
        //        System.out.println("Ending arm thread");
    }

    public void updateSetpoint(double value) {
        this.armSetpoint = value;
        mc.enable();
        //System.out.println(this.toString());
    }

    // use this if not using thread or pidcontroller stuff
    public void moveArmToPeg(double pos) {
        double cur = ac.pidGet();
        if (cur > (pos - 25) && cur < (pos + 25)) {
            this.movingArm = false;
            armJag.set(0);
        } else if (cur < pos) {
            // move arm up
            this.movingArm = true;
            armJag.set(.8);
        } else if (cur > pos) {
            // move arm down
            this.movingArm = true;
            armJag.set(-.8);
        }
        this.checkLimits();
    }

    public void RaiseArmSlightly() {
        if (ac.pidGet() > LOW_LIMIT + 40) {
            set(1);
        } else {
            set(0);
        }
    }

    public void RaiseArmToTop() {
        boolean armIsDone = RobotTemplate.getBooleanProperty("isDone", true);

        if (ac.pidGet() > Constants.MIDDLE_MIDDLE_PEG - 50) {
            set(0);
            System.out.println("Reached peg!");
            this.OpenClaw();
            this.RetractArm();
            armIsDone = true;
             Timer.delay(.5);
        } else {
            set(1);
            armIsDone = false;
        }

    }

    public void set(double value) {
        this.movingArm = false;
        mc.disable();
        armJag.set(-value);
        if (value > 0.1 && value < -.1) {
            System.out.println("set: " + this.toString());
        }
    }

    public boolean isArmTooLow() {
        double pos = ac.pidGet();
        return (pos < LOW_LIMIT) ? true : false;
    }

    public boolean isArmTooLowExtended() {
        double pos = ac.pidGet();
        return (pos < ARM_EXTEND_LIMIT) ? true : false;
    }

    public boolean isArmTooHigh() {
        double pos = ac.pidGet();
        return (pos > HIGH_LIMIT) ? true : false;
    }

    boolean checkLimits() {
        this.movingArm = false;
        mc.disable();

        double pos = ac.pidGet();
        boolean moving = false;
        if (pos > HIGH_LIMIT) {
            // move arm down
            armJag.set(.4);
            moving = true;
        } else if (pos < LOW_LIMIT) {
            // move arm up
            armJag.set(-.4);
            moving = true;
        } else {
            // stop arm
            armJag.set(0);
        }
        if (this.isArmExtended()) {
            if (pos < ARM_EXTEND_LIMIT) {
                this.RetractArm();
            }
        }
        if (moving) {
            //Timer.delay(.3);
            System.out.println("checkLimits: " + this.toString());
        }

        return moving;
    }

    public boolean isClawOpen() {
        boolean b = grip.get();
        if (b == CLAW_OPEN) {
            return true;
        }
        return false;
    }

    public void ToggleClaw() {
        boolean a1 = grip.get();
        System.out.println("Grip a1=" + a1);
        grip.set(!a1);
    }

    public void OpenClaw() {
        grip.set(this.CLAW_OPEN);
    }

    public void CloseClaw() {
        grip.set(this.CLAW_CLOSE);
    }

    public boolean isArmExtended() {
        boolean b = this.armExtension1.get();
        if (b == this.ARM_EXTEND) {
            return true;
        }
        return false;
    }

    public void ToggleArmExtend() {
        boolean a1 = armExtension1.get();
        if (this.isArmExtended()) {
            this.RetractArm();
        } else {
            this.ExtendArm();
        }
        //System.out.println("Extend Arm a1=" + a1);
        //armExtension1.set(!a1);
        //armExtension2.set(a1);
    }

    public void ExtendArm() {
        if (isArmTooLowExtended()) {
            System.out.println("ARM TOO LOW, can't extend");
        } else {
            System.out.println("ARM extend");
            armExtension1.set(this.ARM_EXTEND);
            armExtension2.set(!this.ARM_EXTEND);
        }
    }

    public void RetractArm() {
        System.out.println("ARM retract");
        armExtension1.set(this.ARM_RETRACT);
        armExtension2.set(!this.ARM_RETRACT);
    }
}
