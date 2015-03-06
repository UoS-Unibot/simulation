/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.simulation.robot;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import org.unisim.reality.SerialCommunicator;


public class RealRobotBody implements IRobotBody {
    
    SerialCommunicator serial = new SerialCommunicator();

    public RealRobotBody() throws SerialPortException {
        serial.openSerialPort();
        serial.sendCommand("#t3");
        try {
            serial.readLine();
        } catch (SerialPortTimeoutException ex) {
            Logger.getLogger(RealRobotBody.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void closePort() throws SerialPortException {
        serial.sendCommand("#d0");
        serial.closeSerialPort();
    }
    
    private double[] parseSonarData(String data) {
//        if(!data.startsWith("#!"))
//            throw new IllegalArgumentException("Data '" + data + " is not valid sonar data");
        double[] arrayData = new double[4];
        Iterable<String> iterStr = Splitter.on(" ").omitEmptyStrings().trimResults().limit(4).split(data.substring(2));
        int i = 0;
        for(String s: iterStr) {
            arrayData[i] = Double.parseDouble(s);
            i++;
        }
        return arrayData;
    }

    private double[] lastSonarReadings;
    
    
    public void setMotors(double velocity, double angularVelocity) {
        try {
            //TODO: implement conversion from real world units to command
            serial.sendCommand(String.format("#d%f %f", velocity, angularVelocity));
            lastSonarReadings = parseSonarData(serial.readLine());
        } catch (SerialPortException | SerialPortTimeoutException ex) {
            Logger.getLogger(RealRobotBody.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.toString());
        } 
    }

    @Override
    public double getRange() {
        try {
            serial.sendCommand("#q");
            String data = serial.readLine();
            return Double.parseDouble(data.substring(2));
        } catch (SerialPortException | SerialPortTimeoutException ex) {
            Logger.getLogger(RealRobotBody.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Double.NaN;
    }

    @Override
    public double[] getSonars() {
        if(lastSonarReadings == null) {
            try {
                serial.sendCommand("#!3");
                lastSonarReadings = parseSonarData(serial.readLine());
            } catch (SerialPortException | SerialPortTimeoutException ex) {
                Logger.getLogger(RealRobotBody.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lastSonarReadings;
    }

    @Override
    public boolean isLive() {
        return true;
    }

    @Override
    public void step(double velocity, double angularVelocity) {
        lastSonarReadings = null;
    }
    
}
