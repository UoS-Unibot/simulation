/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.unibot.serial;

/**
 *
 * @author miles
 */
public class SerialParameters {
    
    private final String portName;
    private final int timeOutMSec;
    private final double calibratedForwardVelocityMultiplier;
    private final double calibratedAngularVelocityMultiplier;

    public SerialParameters() {
        this("COM4",100,1.0,1.0);
    }

    public SerialParameters(String portName, int timeOutMSec,
            double calibratedForwardVelocityMultiplier,
            double calibratedAngularVelocityMultiplier) {
        this.portName = portName;
        this.timeOutMSec = timeOutMSec;
        this.calibratedForwardVelocityMultiplier
                = calibratedForwardVelocityMultiplier;
        this.calibratedAngularVelocityMultiplier
                = calibratedAngularVelocityMultiplier;
    }

    
    

    public String getPortName() {
        return portName;
    }


    public int getTimeOutMSec() {
        return timeOutMSec;
    }


    public double getCalibratedForwardVelocityMultiplier() {
        return calibratedForwardVelocityMultiplier;
    }

    public double getCalibratedAngularVelocityMultiplier() {
        return calibratedAngularVelocityMultiplier;
    }

    
    
    
}
