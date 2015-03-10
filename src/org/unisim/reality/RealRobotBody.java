package org.unisim.reality;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import org.unisim.io.Loggable;
import org.unisim.simulation.robot.IRobotBody;

/**
 * The RealRobotBody implements the IRobotBody interface representing a real
 * robot with serial communication.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class RealRobotBody implements IRobotBody,Loggable {

    private final SerialCommunicator serial;
    private final SerialParameters parameters;

    private double lastRange = Double.NaN;
    private double[] lastSonarReadings;
    private boolean live = true;

    /**
     * Instantiates a new RealRobotBody with default parameters. Sends a command
     * to the Unibot specifying that data packet 3 (sonar) should be returned
     * after sending motor commands.
     *
     * @throws SerialPortException If an error occurs opening the serial port.
     * @throws SerialPortTimeoutException If the data packet command times out.
     */
    public RealRobotBody() throws SerialPortException,
            SerialPortTimeoutException {
        this(new SerialParameters());
    }

    /**
     * Instantiates a new RealRobotBody with specified parameters. Sends a
     * command to the Unibot specifying that data packet 3 (sonar) should be
     * returned after sending motor commands.
     *
     * @param parameters SerialParameters specifying port name, time out length
     * and robot calibration data.
     *
     * @throws SerialPortException If an error occurs opening the serial port.
     * @throws SerialPortTimeoutException If the data packet command times out.
     */
    public RealRobotBody(SerialParameters parameters) throws SerialPortException,
            SerialPortTimeoutException {
        this.parameters = parameters;
        serial = new SerialCommunicator(parameters);
        serial.openSerialPort();
        serial.sendCommand("#t3");
        serial.readLine();
    }

    /**
     * Closes the serial port, also sending a halt command to the robot. Should
     * be called on close down.
     *
     * @throws SerialPortException
     */
    public void closePort() throws SerialPortException {
        serial.sendCommand("#d0");
        serial.closeSerialPort();
    }

    /**
     * Directly sets the motor speed of the robot, and reads the returned data
     * packet from the serial port.
     *
     * @param velocity Forward velocity to send to the robot in m/s.
     * @param angularVelocity Angular velocity to send to the robot in radians/s
     * @throws SerialPortException If an error with the serial port occurs.
     * @throws SerialPortTimeoutException If the motor command sent times out.
     */
    public void setMotors(double velocity, double angularVelocity) throws
            SerialPortException, SerialPortTimeoutException {
        serial.sendCommand(String.format("#d %f %f", parameters.
                getCalibratedForwardVelocityMultiplier() * velocity,
                parameters.getCalibratedAngularVelocityMultiplier()
                * angularVelocity));
        readData();
    }

    /**
     * 
     * @return 
     */
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
            Logger.getLogger(RealRobotBody.class.getName()).info(
                    "Unibot startup okay");
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
                    Logger.getLogger(RealRobotBody.class.getName()).log(
                            Level.WARNING,
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

    @Override
    public List getHeaders() {
        return Lists.newArrayList(
                "Rangefinder",
                "Sonar1",
                "Sonar2",
                "Sonar3",
                "Sonar4"
        );
    }

    @Override
    public List getDataRow() {
        return Lists.newArrayList(
                getRange(),
                getSonars()[0],
                getSonars()[1],
                getSonars()[2],
                getSonars()[3]
        );
    }

}
