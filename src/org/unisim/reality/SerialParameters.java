/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

/**
 *
 * @author miles
 */
public class SerialParameters {
    
    private String portName = "COM4";
    private int timeOutMSec = 100;
    private double calibratedForwardVelocityMultiplier = 1.0;
    private double calibratedAngularVelocityMultiplier = 1.0;

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public int getTimeOutMSec() {
        return timeOutMSec;
    }

    public void setTimeOutMSec(int timeOutMSec) {
        this.timeOutMSec = timeOutMSec;
    }

    public double getCalibratedForwardVelocityMultiplier() {
        return calibratedForwardVelocityMultiplier;
    }

    public void setCalibratedForwardVelocityMultiplier(
            double calibratedForwardVelocityMultiplier) {
        this.calibratedForwardVelocityMultiplier
                = calibratedForwardVelocityMultiplier;
    }

    public double getCalibratedAngularVelocityMultiplier() {
        return calibratedAngularVelocityMultiplier;
    }

    public void setCalibratedAngularVelocityMultiplier(
            double calibratedAngularVelocityMultiplier) {
        this.calibratedAngularVelocityMultiplier
                = calibratedAngularVelocityMultiplier;
    }
    
    
    
}
