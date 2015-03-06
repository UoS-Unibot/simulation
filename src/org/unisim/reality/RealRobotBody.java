/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

import com.google.common.base.Splitter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import org.unisim.simulation.robot.IRobotBody;

public class RealRobotBody implements IRobotBody {

    private SerialCommunicator serial = new SerialCommunicator();
    private static final Logger LOG
            = Logger.getLogger(RealRobotBody.class.getName());
    private double lastRange = Double.NaN;

    public RealRobotBody() throws SerialPortException {
        serial.openSerialPort();
        serial.sendCommand("#t3");
        try {
            serial.readLine();
        } catch (SerialPortTimeoutException ex) {
            Logger.getLogger(RealRobotBody.class.getName()).log(Level.SEVERE,
                    null, ex);
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
        Iterable<String> iterStr = Splitter.on(" ").omitEmptyStrings().
                trimResults().limit(4).split(data.substring(2));
        int i = 0;
        for (String s : iterStr) {
            arrayData[i] = Double.parseDouble(s);
            i++;
        }
        return arrayData;
    }

    private double[] lastSonarReadings;

    public void setMotors(double velocity, double angularVelocity) {
        try {
            //TODO: implement conversion from real world units to command
            serial.sendCommand(String.format("#d%f %f", velocity,
                    angularVelocity));
            readData();
        } catch (SerialPortException ex) {
            LOG.log(Level.SEVERE,
                    null, ex);
            throw new RuntimeException(ex.toString());
        }
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
            } catch (SerialPortException ex) {
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
                s = lastSonarReadings;
                lastSonarReadings = null;
                return s;
            } catch (SerialPortException ex) {
                LOG.log(Level.SEVERE, null, ex);
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

    }

    public void readData() {
        try {
            String data = serial.readLine();
            if (STARTUP_PATTERN.matcher(data).matches()) {
                LOG.info("Unibot startup okay");
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
                        LOG.log(Level.WARNING,
                                "Could not parsed received data: {0}", data);
                    }
                }
            }
        } catch (SerialPortTimeoutException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

    }

    private final Pattern STARTUP_PATTERN = Pattern.compile(
            "\\s*Startup OK!\\s*");
    private final Pattern RANGE_PATTERN = Pattern.compile(
            "#!\\s*(\\d{1,3}.\\d*)\\s*");
    private final Pattern SONAR_PATTERN = Pattern.compile(
            "#!\\s*(\\d{1,3}.\\d*)\\s*(\\d{1,3}.\\d*)\\s*(\\d{1,3}.\\d*)\\s*(\\d{1,3}.\\d*)\\s*");

}
