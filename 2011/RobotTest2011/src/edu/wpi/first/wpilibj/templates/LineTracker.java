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
public class LineTracker {

    RobotDrive drive;
    DriverStation ds;
    DigitalInput left;
    DigitalInput middle;
    DigitalInput right;
    Encoder leftEncoder;
    Encoder rightEncoder;
    double defaultSteeringGain = 0.4;
    final int STRAIGHT = 1;
    final int LEFT = 2;
    final int RIGHT = 3;
    int linePath;
    boolean isStraightLine;
    boolean goLeft;

    public LineTracker(RobotDrive drive, DriverStation ds) {
        this.drive = drive;

        this.ds = DriverStation.getInstance();

        left = new DigitalInput(Constants.LINE_TRACKER_LEFT);
        middle = new DigitalInput(Constants.LINE_TRACKER_MIDDLE);
        right = new DigitalInput(Constants.LINE_TRACKER_RIGHT);

        leftEncoder = new Encoder(7, 8, false, CounterBase.EncodingType.k1X);
        rightEncoder = new Encoder(9, 10, false, CounterBase.EncodingType.k1X);

        leftEncoder.start();
        rightEncoder.start();
        updateDsReading();
    }

    public String toString() {
        return "Left Tracker: " + left.get() + " Middle Tracker: " + middle.get() + " right tracker: " + right.get() + " left encoder: " + leftEncoder.get() + " right encoder: " + rightEncoder.get();
    }

    public void updateDsReading() {
        isStraightLine = ds.getDigitalIn(1);
        goLeft = !ds.getDigitalIn(2) && !isStraightLine;
        System.out.println("StraightLine: " + isStraightLine);
        System.out.println("GoingLeft: " + goLeft);

        // figure out which line to follow, default to straight now.

        // linePath = this.RIGHT;
        // linePath = this.LEFT;

        linePath = this.STRAIGHT;
    }

    public int getLinePath() {
        return linePath;
    }

    public void printPath() {

        String str = "";
        switch (this.linePath) {
            case RIGHT:
                str = "right";
                break;
            case LEFT:
                str = "left";
                break;
            case STRAIGHT:
            default:
                str = "straight";
        }
        System.out.println("LINE: " + str);
    }
    

     void FollowLine() {
        leftEncoder.reset();
        rightEncoder.reset();
        leftEncoder.setDistancePerPulse(.03275);
        rightEncoder.setDistancePerPulse(.03275);

        System.out.println("Starting Line Tracker");
        int i = 0;
        int c = 0;

        double currentEncoderR = rightEncoder.getDistance();
        double currentEncoderL = leftEncoder.getDistance();

        drive.setExpiration(15);

        int binaryValue;
        int previousValue = 0;
        double steeringGain;

        double forkProfile[] = {0.7, 0.65, 0.6, 0.49, 0.45, 0.42, 0.432, .45, 0.43, .41, 0.42, 0.4};
        //double straightProfile[] = {0.7, 0.7, 0.6, 0.6, 0.35, 0.35, 0.35, 0.0};
        //double straightProfile[] = {0.7, 0.7, 0.7, 0.68, 0.63, 0.6, 0.53, 0.47, 0.44, 0.42, 0.41, 0.0};
        double straightProfile[] = {0.65, 0.65, 0.65, 0.64, 0.6, 0.54, 0.47, -0.1, 0.44, 0.42, 0.41, 0.0};
        double turnArrayFork[] = {0.35, 0.43, 0.51, 0.57, 0.62, .65};
        double turnArrayStraight[] = {0.4, 0.44, 0.48, 0.51, .38, .34};

        double powerProfile[];
        double stopTime;
        switch (this.linePath) {
            case RIGHT:
                powerProfile = forkProfile;
                stopTime = 4.0;
                break;
            case LEFT:
                powerProfile = forkProfile;
                stopTime = 4.0;
                break;
            case STRAIGHT:
            default:
                // do straight line as default
                powerProfile = straightProfile;
                stopTime = 2.0;
                break;
        }
        this.printPath();

        boolean atCross = false; // if robot has arrived at end

        // time the path over the line
        Timer timer = new Timer();
        timer.start();
        timer.reset();

        int oldTimeInSeconds = -1;
        double time = 0;
        double speed, turn;
        turn = 0;
        while (!atCross) {
            int timeInSeconds = (int) time;
            // read the sensors
            int leftValue = left.get() ? 1 : 0;
            int middleValue = middle.get() ? 1 : 0;
            int rightValue = right.get() ? 1 : 0;
            // compute the single value from the 3 sensors. Notice that the bits
            // for the outside sensors are flipped depending on left or right
            // fork. Also the sign of the steering direction is different for left/right.
            if (this.linePath != STRAIGHT) {
                binaryValue = leftValue * 4 + middleValue * 2 + rightValue;
                steeringGain = -defaultSteeringGain;
            } else {
                binaryValue = rightValue * 4 + middleValue * 2 + leftValue;
                steeringGain = defaultSteeringGain;
            }

            if (this.linePath == STRAIGHT) {
                defaultSteeringGain = .45;
            } else {
                defaultSteeringGain = .50;
            }

            currentEncoderR = rightEncoder.getDistance();
            currentEncoderL = leftEncoder.getDistance();

            if (currentEncoderR >= (24 * (i + 1)) || currentEncoderL >= (24 * (i + 1))) {

                i++;
                //   speed = powerProfile[i];
            }

            if (currentEncoderR >= (48 * (c + 1)) || currentEncoderL >= (48 * (c + 1))) {

                c++;
                //   speed = powerProfile[i];
            }

            // get the default speed and turn rate at this time
            speed = powerProfile[i];

            if (!isStraightLine) {
                steeringGain = turnArrayFork[c];
            } else {

                steeringGain = turnArrayStraight[c];
            }

            // different cases for different line tracking sensor readings
            if (!isStraightLine) {
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
                        if (i > 9) {
                            atCross = true;
                            speed = 0;
                        }
                        break;
                    default:  // all other cases
                        turn = -steeringGain;
                }
            } else {
                switch (binaryValue) {
                    case 1:  // on line edge
                        turn = 0;
                        break;
                    case 7:  // all sensors on (maybe at cross)
                        if (time > stopTime) {
                            atCross = true;
                            speed = 0;
                        }
                        break;
                    case 0:  // all sensors off
                        if (previousValue == 0 || previousValue == 1) {
                            turn = steeringGain;
                        } else {
                            turn = -steeringGain;
                        }
                        break;
                    default:  // all other cases
                        turn = -steeringGain;
                }
            }

            time = timer.get();

            // print current status for debugging
            if (binaryValue != previousValue) {
                System.out.println("i: " + i
                        + "  Sensor:" + binaryValue
                        + "  speed:" + speed
                        + "  turn:" + turn
                        + "  atCross:" + atCross
                        + "  lv:" + leftValue
                        + "  mv:" + middleValue
                        + "  rv:" + rightValue
                        + "  distance:" + leftEncoder.getDistance()
                        + " time: " + time);
            }

            // set the robot speed and direction
            drive.arcadeDrive(speed, turn);

            if (binaryValue != 0) {
                previousValue = binaryValue;
            }
            //oldTimeInSeconds = timeInSeconds;



            Timer.delay(0.01);
        }
        // Done with loop - stop the robot. Robot ought to be at the end of the line
        drive.arcadeDrive(0, 0);
        System.out.println("Done with Line Code!");

    }
}
