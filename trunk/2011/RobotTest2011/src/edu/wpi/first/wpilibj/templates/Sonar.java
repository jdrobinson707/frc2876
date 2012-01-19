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
public class Sonar implements PIDSource {

    AnalogChannel sonar;
    double lastValue = 0.0;

    public Sonar() {
        sonar = new AnalogChannel(Constants.ANALOG_CHANNEL_SLOT, Constants.SONAR_CHANNEL);
    }

    public double pidGet() {
        return getDistanceF1();
    }

    public double getDistanceRaw() {
        double sonarDistanceRaw = 0.0;
        sonarDistanceRaw = sonar.getVoltage() * 1000 / 9.8;
        return sonarDistanceRaw;
    }

    public double getDistanceF1() {
        double sonarDistanceF1 = 0.0;
        sonarDistanceF1 = sonar.getVoltage() * 1000 / 9.8;
        if (lastValue != 0) {
            if (sonarDistanceF1 > lastValue + 1 || sonarDistanceF1 < lastValue - 1) {
                sonarDistanceF1 = lastValue;
            } else {
                lastValue = sonarDistanceF1;
            }
        }
        return sonarDistanceF1;
    }
}
