package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maciej
 */
public class LineTracker {

    DigitalInput left;
    DigitalInput middle;
    DigitalInput right;
    RobotDrive drive;
    DriverStation ds;
    
    public LineTracker(RobotDrive d) {
        drive = d;
        left = new DigitalInput(Constants.LINE_TRACKER_LEFT);
        middle = new DigitalInput(Constants.LINE_TRACKER_MIDDLE);
        right = new DigitalInput(Constants.LINE_TRACKER_RIGHT);

    }

    public void ourLine() {
        double defaultSteeringGain = 0.4;

        drive.setExpiration(15);

        ds = DriverStation.getInstance();

        int binaryValue;
        int previousValue = 0;
        double steeringGain;

        double forkProfile[] = {0.70, 0.70, 0.55, 0.60, 0.60, 0.50, 0.40, 0.00};
        //double straightProfile[] = {0.7, 0.7, 0.6, 0.6, 0.35, 0.35, 0.35, 0.0};
        double straightProfile[] = {0.7, 0.7, 0.6, 0.55, 0.4, 0.3, -0.2, -0.4};

        double powerProfile[];
        boolean isStraightLine = ds.getDigitalIn(1);
        powerProfile = (isStraightLine) ? straightProfile : forkProfile;
        double stopTime = (isStraightLine) ? 2.0 : 4.0;

        boolean goLeft = !ds.getDigitalIn(2) && !isStraightLine;
        System.out.println("StraightLine: " + isStraightLine);
        System.out.println("GoingLeft: " + goLeft);


        boolean atCross = false; // if robot has arrived at end

        // time the path over the line
        Timer timer = new Timer();
        timer.start();
        timer.reset();

        int oldTimeInSeconds = -1;
        double time;
        double speed, turn;

        while ((time = timer.get()) < 8.0 && !atCross) {
            int timeInSeconds = (int) time;
            // read the sensors
            int leftValue = left.get() ? 1 : 0;
            int middleValue = middle.get() ? 1 : 0;
            int rightValue = right.get() ? 1 : 0;
            // compute the single value from the 3 sensors. Notice that the bits
            // for the outside sensors are flipped depending on left or right
            // fork. Also the sign of the steering direction is different for left/right.
            if (goLeft) {
                binaryValue = leftValue * 4 + middleValue * 2 + rightValue;
                steeringGain = -defaultSteeringGain;
            } else {
                binaryValue = rightValue * 4 + middleValue * 2 + leftValue;
                steeringGain = defaultSteeringGain;
            }

            if (isStraightLine) {
                defaultSteeringGain = .3;
            } else {
                defaultSteeringGain = .4;
            }

            // get the default speed and turn rate at this time
            speed = powerProfile[timeInSeconds];
            turn = 0;

            // different cases for different line tracking sensor readings
            switch (binaryValue) {
                case 0:  // all sensors off
                    if (previousValue == 0 || previousValue == 1) {
                        turn = steeringGain;
                    } else {
                        turn = -steeringGain;
                    }
                    break;
                case 1:  // on line edge
                    if (!isStraightLine) {
                        turn = 0;
                    } else {
                        turn = -steeringGain;
                    }
                    break;
                case 2: // We added this to turn less if the line is under the middle sensor
                    if (!isStraightLine) {
                        turn = -steeringGain / 2;
                    } else {
                        turn = 0;
                    }
                    break;
                case 3:
                    turn = -steeringGain / 4;
                    break;
                case 4:
                    turn = -steeringGain;
                    break;
                case 6:
                    turn = ((-steeringGain * 3) / 4);
                    break;
                case 7:  // all sensors on (maybe at cross)
                    if (time > stopTime) {
                        atCross = true;
                        speed = 0;
                    }
                    break;
                default:  // all other cases
                    turn = -steeringGain;
            }
            // print current status for debugging
            if (binaryValue != previousValue) {
                System.out.println("Time: " + time
                        + " Sensor: " + binaryValue
                        + " speed: " + speed
                        + " turn: " + turn
                        + " atCross: " + atCross
                        + " lv: " + leftValue
                        + " mv: " + middleValue
                        + " rv: " + rightValue);
            }

            // set the robot speed and direction
            drive.arcadeDrive(speed, turn);

            if (binaryValue != 0) {
                previousValue = binaryValue;
            }
            oldTimeInSeconds = timeInSeconds;

            Timer.delay(0.01);
        }
        // Done with loop - stop the robot. Robot ought to be at the end of the line
        drive.arcadeDrive(0, 0);
    }
}
