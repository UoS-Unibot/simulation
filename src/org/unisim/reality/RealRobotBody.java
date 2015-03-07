/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import org.unisim.simulation.robot.IRobotBody;

public class RealRobotBody implements IRobotBody {

    private double lastRange = Double.NaN;
    private final SerialCommunicator serial;

    private final SerialParameters parameters;

    public RealRobotBody(SerialParameters parameters) throws SerialPortException,
            SerialPortTimeoutException {
        this.parameters = parameters;
        serial = new SerialCommunicator(parameters);
        serial.openSerialPort();
        serial.sendCommand("#t3");
        serial.readLine();
    }

    public RealRobotBody() throws SerialPortException,
            SerialPortTimeoutException {
        this(new SerialParameters());
    }

    public void closePort() throws SerialPortException {
        serial.sendCommand("#d0");
        serial.closeSerialPort();
    }

    private double[] lastSonarReadings;

    public void setMotors(double velocity, double angularVelocity) throws
            SerialPortException, SerialPortTimeoutException {
        //TODO: implement conversion from real world units to command
        serial.sendCommand(String.format("#d %f %f", velocity,
                angularVelocity));
        readData();
    }

    @Override
    public double getRange() {
        Double d = lastRange;
        if (d.isNaN()) {
            try {
                serial.sendCommand("#q");
                readData();
                d = lastRange;
                lastRange = Double.NaN;
                return d;
            } catch (SerialPortException | SerialPortTimeoutException ex) {
                Logger.getLogger(RealRobotBody.class.getName()).
                        log(Level.SEVERE,
                                null, ex);
            }
        }
        return lastRange;
    }

    @Override
    public double[] getSonars() {
        double[] s;
        if (lastSonarReadings == null) {
            try {
                serial.sendCommand("#!3");
                readData();
            } catch (SerialPortException | SerialPortTimeoutException ex) {
                Logger.getLogger(RealRobotBody.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }

        s = lastSonarReadings;
        lastSonarReadings = null;
        return s;
    }

    private boolean live = true;

    @Override
    public boolean isLive() {
        return live;
    }

    @Override
    public void step(double velocity, double angularVelocity) {
        try {
            setMotors(velocity, angularVelocity);
        } catch (SerialPortException | SerialPortTimeoutException ex) {
            Logger.getLogger(RealRobotBody.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    public void readData() throws SerialPortTimeoutException {
            String data = serial.readLine();
            if (STARTUP_PATTERN.matcher(data).matches()) {
                Logger.getLogger(RealRobotBody.class.getName()).info("Unibot startup okay");
            } else {
                Matcher range = RANGE_PATTERN.matcher(data);
                if (range.matches()) {
                    lastRange = Double.parseDouble(range.group(1));
                } else {
                    Matcher sonar = SONAR_PATTERN.matcher(data);
                    if (sonar.matches()) {
                        lastSonarReadings = new double[]{
                            Double.parseDouble(sonar.group(1)),
                            Double.parseDouble(sonar.group(2)),
                            Double.parseDouble(sonar.group(3)),
                            Double.parseDouble(sonar.group(4))
                        };
                    } else {
                        Logger.getLogger(RealRobotBody.class.getName()).log(Level.WARNING,
                                "Could not parsed received data: {0}", data);
                    }
                }
            }
    }

    public void halt() throws SerialPortException {
        serial.sendCommand("#d0");
        live = false;
    }

    private final Pattern STARTUP_PATTERN = Pattern.compile(
            "\\s*Startup OK!\\s*");
    private final Pattern RANGE_PATTERN = Pattern.compile(
            "#q\\s*(\\d{1,3}.\\d*)\\s*");
    private final Pattern SONAR_PATTERN = Pattern.compile(
            "#!\\s*(\\d{1,3}.\\d*)\\s*(\\d{1,3}.\\d*)\\s*(\\d{1,3}.\\d*)\\s*(\\d{1,3}.\\d*)\\s*");

}
