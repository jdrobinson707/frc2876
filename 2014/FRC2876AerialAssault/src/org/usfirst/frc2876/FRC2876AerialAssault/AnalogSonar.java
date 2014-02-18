/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.usfirst.frc2876.FRC2876AerialAssault;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author maciej
 */
public class AnalogSonar extends AnalogChannel {
    private static final double scalingFactor = 0.009766;
    
    public AnalogSonar(int slot, int channel) {
        super(slot, channel);
    }
    
    public double getDistance() {
        double inches = this.getVoltage() / scalingFactor;
        return inches;
    }
}
